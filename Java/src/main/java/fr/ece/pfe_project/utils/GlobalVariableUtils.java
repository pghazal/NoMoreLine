package fr.ece.pfe_project.utils;

import fr.ece.pfe_project.model.FrequentationJournaliere;
import fr.ece.pfe_project.model.JourFerie;
import fr.ece.pfe_project.model.Plan;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author pierreghazal
 */
public class GlobalVariableUtils {

    private static HashMap<Date, FrequentationJournaliere> excelMap;
    private static HashMap<Integer, Integer> freqAnnuelleMap;
    private static ArrayList<JourFerie> joursFeries;
    private static ArrayList<Plan> plans;

    public static HashMap<Date, FrequentationJournaliere> getExcelMap() {
        if (excelMap == null) {
            excelMap = new HashMap<Date, FrequentationJournaliere>();
        }

        return excelMap;
    }

    public static HashMap<Integer, Integer> getFrequentationAnnuelleMap() {
        if (freqAnnuelleMap == null) {
            freqAnnuelleMap = new HashMap<Integer, Integer>();
        }

        return freqAnnuelleMap;
    }

    public static ArrayList<JourFerie> getJoursFeries() {
        if (joursFeries == null) {
            joursFeries = new ArrayList<JourFerie>();
        }

        return joursFeries;
    }

    public static ArrayList<Plan> getPlans() {
        if (plans == null) {
            plans = new ArrayList<Plan>();
        }

        return plans;
    }
}
