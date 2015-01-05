package fr.ece.pfe_project.utils;

import fr.ece.pfe_project.model.ExcelRow;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author pierreghazal
 */
public class GlobalVariableUtils {

    private static HashMap<Date, ExcelRow> excelMap;
    private static HashMap<Integer, Integer> freqAnnuelleMap;

    public static HashMap<Date, ExcelRow> getExcelMap() {
        if (excelMap == null) {
            excelMap = new HashMap<Date, ExcelRow>();
        }

        return excelMap;
    }

    public static HashMap<Integer, Integer> getFrequentationAnnuelleMap() {
        if (freqAnnuelleMap == null) {
            if (ParametersUtils.containsKey(ParametersUtils.PARAM_DATABASE_BT_YEAR)) {
                freqAnnuelleMap = (HashMap<Integer, Integer>) ParametersUtils.get(ParametersUtils.PARAM_DATABASE_BT_YEAR);
            } else {
                freqAnnuelleMap = new HashMap<Integer, Integer>();
            }

            if (freqAnnuelleMap.isEmpty()) {
                freqAnnuelleMap.put(2009, 1109397);
                freqAnnuelleMap.put(2010, 1060705);
                freqAnnuelleMap.put(2011, 1080046);
                freqAnnuelleMap.put(2012, 1209064);
                freqAnnuelleMap.put(2013, 1367736);

                System.out.println("Saving PARAM_DATABASE_BT_YEAR");
                // Saving Frequentation Annuelle
                ParametersUtils.set(ParametersUtils.PARAM_DATABASE_BT_YEAR, freqAnnuelleMap);
                // Saving parameters in file parameter
                ParametersUtils.saveParameters();
            }
        }

        return freqAnnuelleMap;
    }

    public static void showExcelMap() {
        for (HashMap.Entry<Date, ExcelRow> entry : getExcelMap().entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue().getValue());
        }
    }
}
