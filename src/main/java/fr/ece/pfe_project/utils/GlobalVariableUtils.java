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

    public static HashMap<Date, ExcelRow> getExcelMap() {
        if (excelMap == null) {
            excelMap = new HashMap<Date, ExcelRow>();
        }

        return excelMap;
    }

    public static void showExcelMap() {
        for (HashMap.Entry<Date, ExcelRow> entry : getExcelMap().entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue().getValue());
        }
    }
}
