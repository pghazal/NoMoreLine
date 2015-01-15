package fr.ece.pfe_project.algo;

import fr.ece.pfe_project.database.DatabaseHelper;
import fr.ece.pfe_project.model.AlgoResult;
import fr.ece.pfe_project.model.FrequentationJournaliere;
import fr.ece.pfe_project.model.JourFerie;
import fr.ece.pfe_project.utils.GlobalVariableUtils;
import fr.ece.pfe_project.utils.ParametersUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author pierreghazal
 */
public class Algorithm {

    // Moyenne avec les annees precedentes
    public static AlgoResult process1(Date dateSelected) {
        AlgoResult algoResult = new AlgoResult();

        int yearSelected = getYear(dateSelected);
        int currentMonth = getMonth(dateSelected);
        int currentDay = getDayOfMonth(dateSelected);

        int YEARS_TO_COMPARE = DatabaseHelper.getYearsComplete().size();
        System.out.println("NB ANNEE : " + YEARS_TO_COMPARE);

        if (YEARS_TO_COMPARE > 0) {

            if (YEARS_TO_COMPARE == 1 || YEARS_TO_COMPARE == 2) {
                algoResult.setPrevisionPassager(DatabaseHelper
                        .getLastFrequentationOfCompleteYear(dateSelected));
                return algoResult;
            }

            int nbSemaine = getWeekOfYear(dateSelected);
            int jour = getDayOfWeek(dateSelected);

            Calendar cal = Calendar.getInstance();
            cal.setTime(dateSelected);

            /**
             * CALCUL JOURNALIER
             */
            ArrayList<Double> variationsJournaliere = new ArrayList<Double>();
            Date firstDate;
            Date secondDate;
            for (int i = YEARS_TO_COMPARE; i > 1; i--) {

                cal.set(Calendar.YEAR, yearSelected - i);
                cal.set(Calendar.WEEK_OF_YEAR, nbSemaine);
                cal.set(Calendar.DAY_OF_WEEK, jour);

                //cal.set(yearSelected - i, currentMonth, currentDay);
                firstDate = cal.getTime();

                cal.set(Calendar.YEAR, yearSelected - (i - 1));
                cal.set(Calendar.WEEK_OF_YEAR, nbSemaine);
                cal.set(Calendar.DAY_OF_WEEK, jour);
                //cal.set(yearSelected - (i - 1), currentMonth, currentDay);
                secondDate = cal.getTime();

                System.out.println("First Date : " + firstDate);
                System.out.println("Second Date : " + secondDate);

                Double var = getVariation(firstDate, secondDate, true);
                System.out.println("Variation : " + var);

                variationsJournaliere.add(var);
            }

            // calcul moyenne journaliere
            double moyVariationJournaliere;
            double sommeJournaliere = 0;
            for (int i = 0; i < YEARS_TO_COMPARE - 1; i++) {
                sommeJournaliere += variationsJournaliere.get(i);
            }
            // Représente le Alpha sur le papier
            moyVariationJournaliere = sommeJournaliere / (YEARS_TO_COMPARE - 1);
            System.out.println("Alpha = " + moyVariationJournaliere);

            // Réinitialisation de la date
            cal.setTime(dateSelected);
            // Initialisation à l'année précédente
            cal.set(Calendar.YEAR, yearSelected - 1);
            cal.set(Calendar.WEEK_OF_YEAR, nbSemaine);
            cal.set(Calendar.DAY_OF_WEEK, jour);

            // représente le J1 sur le papier
            double nbPassagerJournalier = getFrequentationJournaliere(cal.getTime())
                    + moyVariationJournaliere * getFrequentationJournaliere(cal.getTime());
            System.out.println("J1 = " + nbPassagerJournalier);

            // Réinitialisation pour l'année
            cal.setTime(dateSelected);

            /**
             * CALCUL ANNUEL
             */
            ArrayList<Double> variationsAnnuelle = new ArrayList<Double>();
            for (int i = YEARS_TO_COMPARE; i > 1; i--) {

                cal.set(yearSelected - i, currentMonth, currentDay);
                firstDate = cal.getTime();
                cal.set(yearSelected - (i - 1), currentMonth, currentDay);
                secondDate = cal.getTime();

                System.out.println("First Date : " + firstDate);
                System.out.println("Second Date : " + secondDate);

                Double var = getVariation(firstDate, secondDate, false);
                System.out.println("Variation : " + var);

                variationsAnnuelle.add(var);
            }

            // Calcul moyenne annuelle
            double moyVariationAnnuelle;
            double sommeAnnuelle = 0;
            for (int i = 0; i < YEARS_TO_COMPARE - 1; i++) {
                sommeAnnuelle += variationsAnnuelle.get(i);
            }
            // Représente le Beta sur le papier
            moyVariationAnnuelle = sommeAnnuelle / (YEARS_TO_COMPARE - 1);
            System.out.println("Beta = " + moyVariationAnnuelle);

            // représente le A1 sur le papier
            double nbPassagerAnnuelle = getFrequentationAnnuelle(yearSelected - 1)
                    + moyVariationAnnuelle * getFrequentationAnnuelle(yearSelected - 1);
            System.out.println("A1 = " + nbPassagerAnnuelle);

            // Réinitialisation de la date
            cal.setTime(dateSelected);
            // Initialisation à l'année précédente
            cal.set(Calendar.YEAR, yearSelected - 1);
            cal.set(Calendar.WEEK_OF_YEAR, nbSemaine);
            cal.set(Calendar.DAY_OF_WEEK, jour);

            /**
             * CALCUL ASSOCIE JOURNALIER/ANNUEL
             */
            // Pondéré par Beta : représente NBP14 sur le papier
            double nbPassagerPondere = getFrequentationJournaliere(cal.getTime())
                    + moyVariationAnnuelle * getFrequentationJournaliere(cal.getTime());
            System.out.println("NBP14 = " + nbPassagerPondere);

            // Représente le M14 sur le papier
            double moyenneJournalierAnnuelle = (nbPassagerJournalier + nbPassagerPondere) / 2;
            System.out.println("M14 = " + moyenneJournalierAnnuelle);

            /**
             * CALCUL PROFIL GLOBAL
             */
            ArrayList<Double> calculPourcentageAnnuelle = new ArrayList<Double>();
            for (int i = 0; i < GlobalVariableUtils.getFrequentationAnnuelleMap().size() - 1; i++) {
                int diff = getFrequentationAnnuelle(yearSelected - 1 - i) - getFrequentationAnnuelle(yearSelected - 2 - i);
                System.out.println("Diff = " + diff);

                Double percent = new Integer(diff).doubleValue() / new Integer(getFrequentationAnnuelle(yearSelected - 1 - i)).doubleValue();
                System.out.println("Percent = " + percent);

                calculPourcentageAnnuelle.add(percent);
            }

            double moyPourcentage = 0;
            for (int i = 0; i < calculPourcentageAnnuelle.size(); i++) {
                moyPourcentage += calculPourcentageAnnuelle.get(i);
            }

            // Représente Ppg sur le papier
            moyPourcentage = moyPourcentage / calculPourcentageAnnuelle.size();
            System.out.println("Calcul Moy Percent (Ppg) : " + moyPourcentage);

            // Moyenne pondéré par le profil (MP14 sur le papier)
            double moyPondere = moyenneJournalierAnnuelle + moyenneJournalierAnnuelle * moyPourcentage;
            // Représente le (M14 + MP14) /2 sur papier....
            double moySalut = (moyPondere + moyenneJournalierAnnuelle) / 2;

            // Réinitialisation de la date
            cal.setTime(dateSelected);
            // Initialisation à l'année précédente
            cal.add(Calendar.YEAR, -1);

            System.out.println("Freq Journaliere 2013 : " + cal.getTime());
            System.out.println("Freq Journaliere 2013 : " + getFrequentationJournaliere(cal.getTime()));

            int result = (int) (nbPassagerJournalier + nbPassagerPondere + moyPondere) / 3;

            System.out.println("RESULT : " + result);

            algoResult.setPrevisionPassager(result);
        } else {
            // DISPLAY POP UP
            // Aucunes données en Base blabla
        }

        return algoResult;
    }

    public static AlgoResult process2(Date date, AlgoResult algoResult) {
        ArrayList<JourFerie> jours = (ArrayList<JourFerie>) ParametersUtils.get(ParametersUtils.PARAM_JOURS_FERIES);

        if (jours != null && jours.size() > 0) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.HOUR, 0);

            Boolean isFerie = false;

            switch (getDayOfWeek(date)) {
                case Calendar.MONDAY:
                    isFerie = isFerie(date, jours);

                    cal.add(Calendar.DAY_OF_MONTH, 1);

                    if (isFerie) {
                        // Si le lendemain est ferie
                        if (isFerie(cal.getTime(), jours)) {
                            int prevision = process1(cal.getTime())
                                    .getPrevisionPassager();

                            algoResult.setPrevisionPassager(prevision - ((prevision * 10) / 100));
                        } else {
                            algoResult.setPrevisionPassager(process1(cal.getTime())
                                    .getPrevisionPassager());
                        }

                    } // Si le lendemain est ferie
                    else if (isFerie(cal.getTime(), jours)) {
                        algoResult.setPrevisionPassager(process1(cal.getTime())
                                .getPrevisionPassager());
                    }

                    break;
                case Calendar.TUESDAY:
                    isFerie = isFerie(date, jours);

                    cal.add(Calendar.DAY_OF_MONTH, -1);

                    if (isFerie) {
                        // Si la veille est ferie
                        if (isFerie(cal.getTime(), jours)) {
                            algoResult.setPrevisionPassager(process1(cal.getTime())
                                    .getPrevisionPassager());
                        } else {
                            // do nothing
                        }

                    } // Si la veille est ferie
                    else if (isFerie(cal.getTime(), jours)) {
                        algoResult.setPrevisionPassager(process1(cal.getTime())
                                .getPrevisionPassager());
                    }

                    break;
                case Calendar.WEDNESDAY:

                    isFerie = isFerie(date, jours);

                    if (isFerie) {
                        int prevision = process1(date)
                                .getPrevisionPassager();

                        algoResult.setPrevisionPassager(prevision - ((prevision * 25) / 100));
                        break;
                    }

                    cal.add(Calendar.DAY_OF_MONTH, -1);
                    // Si la veille est ferie
                    if (isFerie(cal.getTime(), jours)) {
                        algoResult.setPrevisionPassager(process1(cal.getTime())
                                .getPrevisionPassager());
                        break;
                    }

                    cal.add(Calendar.DAY_OF_MONTH, 2);
                    // Si le lendemain est ferie
                    if (isFerie(cal.getTime(), jours)) {
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        // Et le jour d'apres est ferie
                        if (isFerie(cal.getTime(), jours)) {
                            algoResult.setPrevisionPassager(process1(cal.getTime())
                                    .getPrevisionPassager());
                        }
                    }

                    break;
                case Calendar.THURSDAY:

                    isFerie = isFerie(date, jours);

                    if (isFerie) {
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        // Si le lendemain est ferie
                        if (isFerie(cal.getTime(), jours)) {
                            int prevision = process1(date)
                                    .getPrevisionPassager();

                            algoResult.setPrevisionPassager(prevision - ((prevision * 25) / 100));
                        } else {
                            int prevision = process1(date)
                                    .getPrevisionPassager();

                            algoResult.setPrevisionPassager(prevision - ((prevision * 10) / 100));

                        }

                        break;
                    }

                    cal.add(Calendar.DAY_OF_MONTH, -1);
                    // Si la veille est ferie
                    if (isFerie(cal.getTime(), jours)) {
                        int prevision = process1(date)
                                .getPrevisionPassager();

                        algoResult.setPrevisionPassager(prevision - ((prevision * 20) / 100));
                        break;
                    }

                    cal.add(Calendar.DAY_OF_MONTH, 2);
                    Date JPlus1 = cal.getTime();
                    // Si le lendemain est ferie
                    if (isFerie(cal.getTime(), jours)) {
                        cal.add(Calendar.DAY_OF_MONTH, 3);
                        // Si le lundi suivant est ferie
                        if (isFerie(cal.getTime(), jours)) {
                            algoResult.setPrevisionPassager(process1(JPlus1)
                                    .getPrevisionPassager());
                        } else {
                            int prevision = process1(date)
                                    .getPrevisionPassager();

                            algoResult.setPrevisionPassager(prevision + ((prevision * 10) / 100));
                        }
                    }

                    break;
                case Calendar.FRIDAY:
                    isFerie = isFerie(date, jours);

                    if (isFerie) {
                        cal.add(Calendar.DAY_OF_MONTH, 3);
                        // Si lundi suivant ferie
                        if (isFerie(cal.getTime(), jours)) {
                            int prevision = process1(date)
                                    .getPrevisionPassager();

                            algoResult.setPrevisionPassager(prevision - ((prevision * 30) / 100));
                        } else {
                            int prevision = process1(date)
                                    .getPrevisionPassager();

                            algoResult.setPrevisionPassager(prevision - ((prevision * 20) / 100));
                        }
                        break;
                    }

                    cal.add(Calendar.DAY_OF_MONTH, -1);
                    // Si la veille est ferie
                    if (isFerie(cal.getTime(), jours)) {
                        int prevision = process1(date)
                                .getPrevisionPassager();

                        algoResult.setPrevisionPassager(prevision - ((prevision * 20) / 100));
                    }

                    break;
                case Calendar.SATURDAY:

                    cal.add(Calendar.DAY_OF_MONTH, 1);

                    // Si le lendemain est ferie
                    if (isFerie(cal.getTime(), jours)) {
                        int prevision = process1(date)
                                .getPrevisionPassager();

                        algoResult.setPrevisionPassager(prevision - ((prevision * 20) / 100));
                    }

                    break;
                case Calendar.SUNDAY:

                    cal.add(Calendar.DAY_OF_MONTH, 1);

                    // Si le lendemain est ferie
                    if (isFerie(cal.getTime(), jours)) {

                        cal.add(Calendar.DAY_OF_MONTH, -2);

                        algoResult.setPrevisionPassager(process1(cal.getTime())
                                .getPrevisionPassager());
                    }
                    break;
                default:
                    break;
            }
        }

        return algoResult;
    }

    public static AlgoResult processBlindageAlgo(Date dateSelected, int gap) {
        AlgoResult algoResult = new AlgoResult();

        int yearSelected = getYear(dateSelected);
        int currentMonth = getMonth(dateSelected);
        int currentDay = getDayOfMonth(dateSelected);
        int dayOfWeek = getDayOfWeek(dateSelected);

        HashMap<Date, FrequentationJournaliere> yearGappedMap
                = new HashMap<Date, FrequentationJournaliere>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateSelected);
        cal.set(Calendar.YEAR, yearSelected - (gap - 1));
        cal.set(Calendar.MONTH, currentMonth);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Integer yearGapped = cal.get(Calendar.YEAR);
        Integer freqAnnuelleGapped = 0;

        System.out.println("#######################################");
        System.out.println("## " + cal.getTime());

        // Changer 365 pour année bis
        // Calcul des années manquantes
        for (int i = 1; i < 365 + 1; i++) {
            cal.set(Calendar.DAY_OF_YEAR, i);

            int freq = process1(cal.getTime()).getPrevisionPassager();

            yearGappedMap.put(cal.getTime(),
                    new FrequentationJournaliere(cal.getTime(), freq));

            freqAnnuelleGapped += freq;

            System.out.println("Date : " + cal.getTime() + " => " + freq);
        }

        cal.setTime(dateSelected);
        cal.set(Calendar.YEAR, yearSelected - (gap - 1));
        cal.set(Calendar.WEEK_OF_YEAR, getWeekOfYear(dateSelected));
        cal.set(Calendar.DAY_OF_WEEK, getDayOfWeek(dateSelected));
        System.out.println("#######################################");
        System.out.println("Freq 2014 : " + yearGappedMap.get(cal.getTime()).getDate());
        System.out.println("Freq 2014 : " + yearGappedMap.get(cal.getTime()).getFrequentation());

        System.out.println("Frequentation Annuelle " + cal.get(Calendar.YEAR)
                + " : " + freqAnnuelleGapped);

        ArrayList<Integer> yearsComplete = DatabaseHelper.getYearsComplete();
        yearsComplete.add(yearGapped);
        Collections.sort(yearsComplete);

        int YEARS_TO_COMPARE = yearsComplete.size();
        System.out.println("NB ANNEE : " + YEARS_TO_COMPARE);

        if (YEARS_TO_COMPARE > 0) {

            if (YEARS_TO_COMPARE == 1 || YEARS_TO_COMPARE == 2) {
                algoResult.setPrevisionPassager(DatabaseHelper
                        .getLastFrequentationOfCompleteYear(dateSelected));
                return algoResult;
            }

            int nbSemaine = getWeekOfYear(dateSelected);
            int jour = getDayOfWeek(dateSelected);

            cal.setTime(dateSelected);

            /**
             * CALCUL JOURNALIER
             */
            ArrayList<Double> variationsJournaliere = new ArrayList<Double>();
            Date firstDate;
            Date secondDate;
            for (int i = YEARS_TO_COMPARE; i > 1; i--) {

                cal.set(Calendar.YEAR, yearSelected - i);
                cal.set(Calendar.WEEK_OF_YEAR, nbSemaine);
                cal.set(Calendar.DAY_OF_WEEK, jour);
                firstDate = cal.getTime();

                cal.set(Calendar.YEAR, yearSelected - (i - 1));
                cal.set(Calendar.WEEK_OF_YEAR, nbSemaine);
                cal.set(Calendar.DAY_OF_WEEK, jour);
                secondDate = cal.getTime();

                System.out.println("First Date : " + firstDate);
                System.out.println("Second Date : " + secondDate);

                Double var;

                if (cal.get(Calendar.YEAR) == yearGapped) {
                    double freq1 = new Integer(getFrequentationJournaliere(firstDate)).doubleValue();
                    double freq2 = yearGappedMap.get(secondDate).getFrequentation().doubleValue();

                    var = (freq1 - freq2) / freq2;
                } else {
                    var = getVariation(firstDate, secondDate, true);
                }

                System.out.println("Variation : " + var);

                variationsJournaliere.add(var);
            }

            // calcul moyenne journaliere
            double moyVariationJournaliere;
            double sommeJournaliere = 0;
            for (int i = 0; i < YEARS_TO_COMPARE - 1; i++) {
                sommeJournaliere += variationsJournaliere.get(i);
            }
            // Représente le Alpha sur le papier
            moyVariationJournaliere = sommeJournaliere / (YEARS_TO_COMPARE - 1);
            System.out.println("Alpha = " + moyVariationJournaliere);

            // Réinitialisation de la date
            cal.setTime(dateSelected);
            // Initialisation à l'année précédente
            cal.set(Calendar.YEAR, yearSelected - 1);
            cal.set(Calendar.WEEK_OF_YEAR, nbSemaine);
            cal.set(Calendar.DAY_OF_WEEK, jour);

            // représente le J1 sur le papier
            double nbPassagerJournalier = yearGappedMap.get(cal.getTime()).getFrequentation()
                    + moyVariationJournaliere * yearGappedMap.get(cal.getTime()).getFrequentation();
            System.out.println("J1 = " + nbPassagerJournalier);

            // Réinitialisation pour l'année
            cal.setTime(dateSelected);

            /**
             * CALCUL ANNUEL
             */
            ArrayList<Double> variationsAnnuelle = new ArrayList<Double>();
            for (int i = YEARS_TO_COMPARE; i > 1; i--) {

                cal.set(yearSelected - i, currentMonth, currentDay);
                firstDate = cal.getTime();
                cal.set(yearSelected - (i - 1), currentMonth, currentDay);
                secondDate = cal.getTime();

                System.out.println("First Date : " + firstDate);
                System.out.println("Second Date : " + secondDate);

                Double var;

                if (cal.get(Calendar.YEAR) == yearGapped) {
                    double freq1 = new Integer(getFrequentationAnnuelle(getYear(firstDate))).doubleValue();
                    double freq2 = freqAnnuelleGapped.doubleValue();

                    var = (freq1 - freq2) / freq2;
                } else {
                    var = getVariation(firstDate, secondDate, false);
                }

                System.out.println("Variation : " + var);

                variationsAnnuelle.add(var);
            }

            // Calcul moyenne annuelle
            double moyVariationAnnuelle;
            double sommeAnnuelle = 0;
            for (int i = 0; i < YEARS_TO_COMPARE - 1; i++) {
                sommeAnnuelle += variationsAnnuelle.get(i);
            }
            // Représente le Beta sur le papier
            moyVariationAnnuelle = sommeAnnuelle / (YEARS_TO_COMPARE - 1);
            System.out.println("Beta = " + moyVariationAnnuelle);

            // représente le A1 sur le papier
            double nbPassagerAnnuelle = freqAnnuelleGapped
                    + moyVariationAnnuelle * freqAnnuelleGapped;
            System.out.println("A1 = " + nbPassagerAnnuelle);

            // Réinitialisation de la date
            cal.setTime(dateSelected);
            // Initialisation à l'année précédente
            cal.set(Calendar.YEAR, yearSelected - 1);
            cal.set(Calendar.WEEK_OF_YEAR, nbSemaine);
            cal.set(Calendar.DAY_OF_WEEK, jour);

            /**
             * CALCUL ASSOCIE JOURNALIER/ANNUEL
             */
            // Pondéré par Beta : représente NBP14 sur le papier
            double nbPassagerPondere = yearGappedMap.get(cal.getTime()).getFrequentation()
                    + moyVariationAnnuelle * yearGappedMap.get(cal.getTime()).getFrequentation();
            System.out.println("NBP14 = " + nbPassagerPondere);

            // Représente le M14 sur le papier
            double moyenneJournalierAnnuelle = (nbPassagerJournalier + nbPassagerPondere) / 2;
            System.out.println("M14 = " + moyenneJournalierAnnuelle);

            HashMap<Integer, Integer> annuelleMap = GlobalVariableUtils.getFrequentationAnnuelleMap();
            annuelleMap.put(yearGapped, freqAnnuelleGapped);

            /**
             * CALCUL PROFIL GLOBAL
             */
            ArrayList<Double> calculPourcentageAnnuelle = new ArrayList<Double>();
            for (int i = 0; i < annuelleMap.size() - 1; i++) {
                int diff = annuelleMap.get(yearSelected - 1 - i) - annuelleMap.get(yearSelected - 2 - i);
                System.out.println("Diff = " + diff);

                Double percent = new Integer(diff).doubleValue() / annuelleMap.get(yearSelected - 1 - i).doubleValue();
                System.out.println("Percent = " + percent);

                calculPourcentageAnnuelle.add(percent);
            }

            annuelleMap.remove(yearGapped);

            double moyPourcentage = 0;
            for (int i = 0; i < calculPourcentageAnnuelle.size(); i++) {
                moyPourcentage += calculPourcentageAnnuelle.get(i);
            }

            // Représente Ppg sur le papier
            moyPourcentage = moyPourcentage / calculPourcentageAnnuelle.size();
            System.out.println("Calcul Moy Percent (Ppg) : " + moyPourcentage);

            // Moyenne pondéré par le profil (MP14 sur le papier)
            double moyPondere = moyenneJournalierAnnuelle + moyenneJournalierAnnuelle * moyPourcentage;
            // Représente le (M14 + MP14) /2 sur papier....
            double moySalut = (moyPondere + moyenneJournalierAnnuelle) / 2;

            // Réinitialisation de la date
            cal.setTime(dateSelected);
            // Initialisation à l'année précédente
            cal.add(Calendar.YEAR, -1);

            int result = (int) (nbPassagerJournalier + nbPassagerPondere + moyPondere) / 3;

            System.out.println("RESULT : " + result);

            algoResult.setPrevisionPassager(result);
        } else {
            // DISPLAY POP UP
            // Aucunes données en Base blabla
        }

        return algoResult;
    }

    public static JourFerie paques(int y) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.YEAR, y);

        JourFerie datePaques = new JourFerie();

        int month;

        int g = (y % 19) + 1;
        int c = y / 100 + 1;
        int x = 3 * c / 4 - 12;
        int z = (8 * c + 5) / 25 - 5;
        int d = 5 * y / 4 - x - 10;
        int e = (11 * g + 20 + z - x) % 30;
        if (e == 25 && g > 11 || e == 24) {
            ++e;
        }
        int n = 44 - e;
        if (n < 21) {
            n = n + 30;
        }
        int day = n + 7 - ((d + n) % 7);
        day = day + 1;

        if (day > 31) {
            month = 3;
            day = day - 31;
            cal.set(Calendar.MONTH, month);
        } else {
            month = 2;
            cal.set(Calendar.MONTH, month);
        }

        cal.set(Calendar.DAY_OF_MONTH, day);

        datePaques.setDate(cal.getTime());
        datePaques.setLibelle("Pâques");

        System.out.println(datePaques.getLibelle() + " " + datePaques.getDate());

        return datePaques;
    }

    public static JourFerie ascension(JourFerie paques) {

        JourFerie dateAscencion = new JourFerie();

        Calendar cal = Calendar.getInstance();
        cal.setTime(paques.getDate());
        cal.add(Calendar.DAY_OF_YEAR, 38);

        dateAscencion.setDate(cal.getTime());
        dateAscencion.setLibelle("Ascension");

        System.out.println(dateAscencion.getLibelle() + " " + dateAscencion.getDate());

        return dateAscencion;
    }

    public static JourFerie pentecote(JourFerie paques) {

        JourFerie date = new JourFerie();

        Calendar cal = Calendar.getInstance();
        cal.setTime(paques.getDate());
        cal.add(Calendar.DAY_OF_MONTH, 49);

        date.setDate(cal.getTime());
        date.setLibelle("Pentecôte");

        System.out.println(date.getLibelle() + " " + date.getDate());

        return date;
    }

    public static JourFerie vendrediSaint(JourFerie paques) {

        JourFerie date = new JourFerie();

        Calendar cal = Calendar.getInstance();
        cal.setTime(paques.getDate());
        cal.add(Calendar.DAY_OF_MONTH, -3);

        date.setDate(cal.getTime());
        date.setLibelle("Vendredi Saint");

        System.out.println(date.getLibelle() + " " + date.getDate());

        return date;
    }

    private static Boolean isFerie(Date date, ArrayList<JourFerie> jours) {
        Boolean isFerie = false;

        for (int i = 0; i < jours.size(); i++) {
            JourFerie jf = jours.get(i);

            if (jf.getDate().equals(date)) {
                isFerie = true;
            }
        }

        if (isFerie) {
            System.out.println(date + " IS FERIE");
        } else {
            System.out.println(date + " IS NOT FERIE");
        }

        return isFerie;
    }

//    public static void displayJour(Date date) {
//
//        int nbSemaine = getWeekOfYear(date);
//        int jour = getDayOfWeek(date);
//
//        System.out.println("Semaine # " + nbSemaine);
//
//        switch (jour) {
//            case Calendar.MONDAY:
//                System.out.println("Jour LUNDI");
//                break;
//            case Calendar.TUESDAY:
//                System.out.println("Jour MARDI");
//                break;
//            case Calendar.WEDNESDAY:
//                System.out.println("Jour MERCREDI");
//                break;
//            case Calendar.THURSDAY:
//                System.out.println("Jour JEUDI");
//                break;
//            case Calendar.FRIDAY:
//                System.out.println("Jour VENDREDI");
//                break;
//            case Calendar.SATURDAY:
//                System.out.println("Jour SAMEDI");
//                break;
//            case Calendar.SUNDAY:
//                System.out.println("Jour DIMANCHE");
//                break;
//        }
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//
//        cal.add(Calendar.YEAR, -1);
//        cal.set(Calendar.WEEK_OF_YEAR, nbSemaine);
//        cal.set(Calendar.DAY_OF_WEEK, jour);
//        System.out.println("Date year - 1 : " + cal.getTime());
//
//        cal.add(Calendar.YEAR, -1);
//        cal.set(Calendar.WEEK_OF_YEAR, nbSemaine);
//        cal.set(Calendar.DAY_OF_WEEK, jour);
//        System.out.println("Date year - 2 : " + cal.getTime());
//
//        cal.add(Calendar.YEAR, -1);
//        cal.set(Calendar.WEEK_OF_YEAR, nbSemaine);
//        cal.set(Calendar.DAY_OF_WEEK, jour);
//        System.out.println("Date year - 3 : " + cal.getTime());
//    }
    private static double getVariation(Date firstDate, Date secondDate, boolean isJournaliere) {
        if (isJournaliere) {
            double freq1 = new Integer(getFrequentationJournaliere(firstDate)).doubleValue();
            double freq2 = new Integer(getFrequentationJournaliere(secondDate)).doubleValue();

            System.out.println("Freq : " + freq1);

            return (freq1 - freq2) / freq2;
        } else {
            double freq1 = new Integer(getFrequentationAnnuelle(getYear(firstDate))).doubleValue();
            double freq2 = new Integer(getFrequentationAnnuelle(getYear(secondDate))).doubleValue();

            System.out.println("Freq : " + freq1);

            return (freq1 - freq2) / freq2;
        }
    }

    public static int getFrequentationJournaliere(Date date) {
        FrequentationJournaliere row = DatabaseHelper.getFrequentationJournaliere(date);//GlobalVariableUtils.getExcelMap().get(date);

        if (row == null) {
            System.err.println("NUULL at Date : " + date);
        }

        return row.getFrequentation();
    }

    public static int getFrequentationAnnuelle(int year) {
        return GlobalVariableUtils.getFrequentationAnnuelleMap().get(year);
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public static int getDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static int getWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
}
