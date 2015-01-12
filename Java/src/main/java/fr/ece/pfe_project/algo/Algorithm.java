package fr.ece.pfe_project.algo;

import fr.ece.pfe_project.database.DatabaseHelper;
import fr.ece.pfe_project.model.AlgoResult;
import fr.ece.pfe_project.model.FrequentationJournaliere;
import fr.ece.pfe_project.utils.GlobalVariableUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author pierreghazal
 */
public class Algorithm {

    // Nombre d'année que l'on compare dans le passé
    private final static int YEARS_TO_COMPARE = 3;

    public static AlgoResult process(Date dateSelected) {
        int yearSelected = getYear(dateSelected);
        int currentMonth = getMonth(dateSelected);
        int currentDay = getDayOfMonth(dateSelected);
        
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

        AlgoResult algoResult = new AlgoResult();
        algoResult.setPrevisionPassager(result);

        return algoResult;
    }

    public static void displayJour(Date date) {

        int nbSemaine = getWeekOfYear(date);
        int jour = getDayOfWeek(date);

        System.out.println("Semaine # " + nbSemaine);

        switch (jour) {
            case Calendar.MONDAY:
                System.out.println("Jour LUNDI");
                break;
            case Calendar.TUESDAY:
                System.out.println("Jour MARDI");
                break;
            case Calendar.WEDNESDAY:
                System.out.println("Jour MERCREDI");
                break;
            case Calendar.THURSDAY:
                System.out.println("Jour JEUDI");
                break;
            case Calendar.FRIDAY:
                System.out.println("Jour VENDREDI");
                break;
            case Calendar.SATURDAY:
                System.out.println("Jour SAMEDI");
                break;
            case Calendar.SUNDAY:
                System.out.println("Jour DIMANCHE");
                break;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.add(Calendar.YEAR, -1);
        cal.set(Calendar.WEEK_OF_YEAR, nbSemaine);
        cal.set(Calendar.DAY_OF_WEEK, jour);
        System.out.println("Date year - 1 : " + cal.getTime());

        cal.add(Calendar.YEAR, -1);
        cal.set(Calendar.WEEK_OF_YEAR, nbSemaine);
        cal.set(Calendar.DAY_OF_WEEK, jour);
        System.out.println("Date year - 2 : " + cal.getTime());

        cal.add(Calendar.YEAR, -1);
        cal.set(Calendar.WEEK_OF_YEAR, nbSemaine);
        cal.set(Calendar.DAY_OF_WEEK, jour);
        System.out.println("Date year - 3 : " + cal.getTime());
    }

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
