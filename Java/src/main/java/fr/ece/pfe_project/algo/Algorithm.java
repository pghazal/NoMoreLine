package fr.ece.pfe_project.algo;

import fr.ece.pfe_project.model.ExcelRow;
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

    public static void process(Date dateSelected) {
        int yearSelected = getYear(dateSelected);
        int currentMonth = getMonth(dateSelected);
        int currentDay = getDay(dateSelected);

//        // Initialisation des YEARS_TO_COMPARE années précédentes
//        ArrayList<Integer> previousYears = new ArrayList<Integer>();
//        for (int i = YEARS_TO_COMPARE; i > 0; i--) {
//            previousYears.add(currentYear - i);
//        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateSelected);

        /**
         * CALCUL JOURNALIER
         */
        ArrayList<Double> variationsJournaliere = new ArrayList<Double>();
        for (int i = YEARS_TO_COMPARE; i > 1; i--) {
            Date firstDate;
            Date secondDate;

            cal.set(yearSelected - i, currentMonth, currentDay);
            firstDate = cal.getTime();
            cal.set(yearSelected - (i - 1), currentMonth, currentDay);
            secondDate = cal.getTime();

            System.out.println("First Date : " + firstDate);
            System.out.println("Second Date : " + secondDate);

            variationsJournaliere.add(getVariation(firstDate, secondDate, true));
        }

        // calcul moyenne journaliere
        double moyVariationJournaliere;
        int sommeJournaliere = 0;
        for (int i = 0; i < YEARS_TO_COMPARE - 1; i++) {
            sommeJournaliere += variationsJournaliere.get(i);
        }
        // Représente le Alpha sur le papier
        moyVariationJournaliere = sommeJournaliere / (YEARS_TO_COMPARE - 1);

        // Réinitialisation de la date
        cal.setTime(dateSelected);
        // Initialisation à l'année précédente
        cal.add(Calendar.YEAR, -1);

        // représente le J1 sur le papier
        double nbPassagerJournalier = getFrequentationJournaliere(cal.getTime())
                + moyVariationJournaliere * getFrequentationJournaliere(cal.getTime());

        // Réinitialisation pour l'année
        cal.setTime(dateSelected);

        /**
         * CALCUL ANNUEL
         */
        ArrayList<Double> variationsAnnuelle = new ArrayList<Double>();
        for (int i = YEARS_TO_COMPARE; i > 1; i--) {
            Date firstDate;
            Date secondDate;

            cal.set(yearSelected - i, currentMonth, currentDay);
            firstDate = cal.getTime();
            cal.set(yearSelected - (i - 1), currentMonth, currentDay);
            secondDate = cal.getTime();

            System.out.println("First Date : " + firstDate);
            System.out.println("Second Date : " + secondDate);

            variationsAnnuelle.add(getVariation(firstDate, secondDate, false));
        }

        // Calcul moyenne annuelle
        double moyVariationAnnuelle;
        int sommeAnnuelle = 0;
        for (int i = 0; i < YEARS_TO_COMPARE - 1; i++) {
            sommeAnnuelle += variationsAnnuelle.get(i);
        }
        // Représente le Beta sur le papier
        moyVariationAnnuelle = sommeAnnuelle / (YEARS_TO_COMPARE - 1);

        // représente le A1 sur le papier
        double nbPassagerAnnuelle = getFrequentationAnnuelle(yearSelected - 1)
                + moyVariationAnnuelle * getFrequentationAnnuelle(yearSelected - 1);

        /**
         * CALCUL ASSOCIE JOURNALIER/ANNUEL
         */
        // Pondéré par Beta : représente NBP14 sur le papier
        double nbPassagerPondere = getFrequentationJournaliere(cal.getTime())
                + moyVariationAnnuelle * getFrequentationJournaliere(cal.getTime());

        // Représente le M14 sur le papier
        double moyenneJournalierAnnuelle = (nbPassagerJournalier + nbPassagerPondere) / 2;

        /**
         * CALCUL PROFIL GLOBAL
         */
        
    }

    private static double getVariation(Date firstDate, Date secondDate, boolean isJournaliere) {
        if (isJournaliere) {
            int freq1 = getFrequentationJournaliere(firstDate);
            int freq2 = getFrequentationJournaliere(secondDate);

            return (freq2 - freq1) / freq1;
        } else {
            int freq1 = getFrequentationAnnuelle(getYear(firstDate));
            int freq2 = getFrequentationAnnuelle(getYear(secondDate));

            return (freq2 - freq1) / freq1;
        }
    }

    public static int getFrequentationJournaliere(Date date) {
        ExcelRow row = GlobalVariableUtils.getExcelMap().get(date);
        
        if(row == null) System.err.println("NUULLL");
        
        return row.getValue();
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

    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
}
