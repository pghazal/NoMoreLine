package fr.ece.pfe_project.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import javax.swing.filechooser.FileFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdatepicker.ComponentManager;
import org.jdatepicker.impl.DateComponentFormatter;

/**
 *
 * @author pierreghazal
 */
public class ExcelUtils {

    public final static String XLSX = "xlsx";
    public final static String XLS = "xls";

    public static FileInputStream getExcelFile(String path) throws FileNotFoundException {
        return new FileInputStream(new File(path));
    }

    public static XSSFWorkbook getWorkbook(FileInputStream fis) throws IOException {
        return new XSSFWorkbook(fis);
    }

    public static XSSFSheet getSheet(XSSFWorkbook workbook, int index) {
        return workbook.getSheetAt(index);
    }

    public static Object getValue(XSSFSheet sheet, int row, int col) {
        try {

            XSSFRow xrow = sheet.getRow(row);
            XSSFCell cell = xrow.getCell(col);

            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    return cell.getNumericCellValue();
                case Cell.CELL_TYPE_STRING:
                    return (new DateComponentFormatter(
                            ComponentManager.getInstance().getComponentFormatDefaults().getSelectedDateFormat()))
                            .stringToValue(cell.getStringCellValue());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void setValueAtEnd(Date date, Object value) {
        try {
            FileInputStream file = getExcelFile((String) ParametersUtils.get(ParametersUtils.PARAM_PATH_EXCEL));
            XSSFWorkbook workbook = getWorkbook(file);
            XSSFSheet sheet = getSheet(workbook, 0);
            XSSFRow row = sheet.getRow(sheet.getLastRowNum() + 1);

            XSSFCell cell = row.getCell(0);
            cell.setCellValue(date);

            cell = row.getCell(1);

            if (value instanceof Integer) {
                cell.setCellValue((Integer) value);
            } else if (value instanceof String) {
                cell.setCellValue((String) value);
            }

            // Close file for reading
            file.close();

            // Open file for writing
            FileOutputStream outFile = new FileOutputStream(
                    new File((String) ParametersUtils.get(ParametersUtils.PARAM_PATH_EXCEL)));
            workbook.write(outFile);
            outFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void replaceValueForDate(Date date, Object value) {
        try {
            FileInputStream file = getExcelFile((String) ParametersUtils.get(ParametersUtils.PARAM_PATH_EXCEL));
            XSSFWorkbook workbook = getWorkbook(file);
            XSSFSheet sheet = getSheet(workbook, 0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (row.getCell(0).getDateCellValue().equals(date)) {
                    if (value instanceof Integer) {
                        row.getCell(1).setCellValue((Integer) value);
                    } else if (value instanceof String) {
                        row.getCell(1).
                                setCellValue((String) value);
                    }

                    break;
                }
            }

            // Close file for reading
            file.close();

            // Open file for writing
            FileOutputStream outFile = new FileOutputStream(
                    new File((String) ParametersUtils.get(ParametersUtils.PARAM_PATH_EXCEL)));
            workbook.write(outFile);
            outFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readExcel() {
        try {

            FileInputStream file = getExcelFile((String) ParametersUtils.get(ParametersUtils.PARAM_PATH_EXCEL));
            XSSFWorkbook workbook = getWorkbook(file);
            XSSFSheet sheet = getSheet(workbook, 0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();
                    //Check the cell type and format accordingly
                    switch (cell.getCellType()) {

                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t");

                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;

                        default:
                            break;
                    }
                }

                System.out.println("\n");
            }

            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    public void updateExcel() {
//
//        try {
//
//            FileInputStream file = getExcelFile();
//            XSSFWorkbook workbook = getWorkbook(file);
//            XSSFSheet sheet = getSheet(workbook, 0);
//            Cell cell = null;
//
//            //Update the value of cell
//            for (int i = 0; i <= 9; i++) {
//                cell = sheet.getRow(i).getCell(1);
//                cell.setCellValue(cell.getNumericCellValue() * 2);
//            }
//
//            file.close();
//
//            FileOutputStream outFile = new FileOutputStream(
//                    new File((String) ParametersUtils.get(ParametersUtils.PARAM_PATH_EXCEL)));
//            workbook.write(outFile);
//            outFile.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public class ExcelFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = getExtension(f);
            if (extension != null) {
                return (extension.equals(ExcelUtils.XLSX)
                        || extension.equals(ExcelUtils.XLS));
            }

            return false;
        }

        @Override
        public String getDescription() {
            return "Fichier Excel";
        }
    }
}
