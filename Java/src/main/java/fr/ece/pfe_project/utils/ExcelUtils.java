package fr.ece.pfe_project.utils;

import fr.ece.pfe_project.database.DatabaseHelper;
import fr.ece.pfe_project.model.FrequentationJournaliere;
import fr.ece.pfe_project.panel.ParametersDialog;
import static java.awt.image.ImageObserver.ERROR;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.filechooser.FileFilter;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
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

    enum TYPE_ERROR {

        NO_ERROR, ERROR_EMPTY, ERROR_NUMERIC, ERROR_DATEFORMAT
    }

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

    public static void setValueAtEnd(String path, Date date, Object value) {
        try {
            FileInputStream file = getExcelFile(path);
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
                    new File(path));
            workbook.write(outFile);
            outFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void replaceValueForDate(String path, Date date, Object value) {
        try {
            FileInputStream file = getExcelFile(path);
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
                    new File(path));
            workbook.write(outFile);
            outFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void readExcel() {
//        try {
//
//            FileInputStream file = getExcelFile((String) ParametersUtils.get(ParametersUtils.PARAM_PATH_EXCEL));
//            XSSFWorkbook workbook = getWorkbook(file);
//            XSSFSheet sheet = getSheet(workbook, 0);
//
//            //Iterate through each rows one by one
//            Iterator<Row> rowIterator = sheet.iterator();
//
//            while (rowIterator.hasNext()) {
//                Row row = rowIterator.next();
//                //For each row, iterate through all the columns
//                Iterator<Cell> cellIterator = row.cellIterator();
//
//                while (cellIterator.hasNext()) {
//
//                    Cell cell = cellIterator.next();
//                    //Check the cell type and format accordingly
//                    switch (cell.getCellType()) {
//
//                        case Cell.CELL_TYPE_NUMERIC:
//                            System.out.print(cell.getNumericCellValue() + "\t");
//
//                            break;
//                        case Cell.CELL_TYPE_STRING:
//                            System.out.print(cell.getStringCellValue() + "\t");
//                            break;
//
//                        default:
//                            break;
//                    }
//                }
//
//                System.out.println("\n");
//            }
//
//            file.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public static boolean loadExcel(String filePath) {
        FileInputStream file = null;

        HashMap<Date, FrequentationJournaliere> tempMap = new HashMap<Date, FrequentationJournaliere>();
        Boolean excelOK = true;

        try {
            file = getExcelFile(filePath);
            XSSFWorkbook workbook = getWorkbook(file);
            XSSFSheet sheet = getSheet(workbook, 0);

            Iterator<Row> rows = sheet.iterator();

            while (rows.hasNext()) {
                Row row = rows.next();

                switch (checkExcel(row)) {

                    case ERROR_EMPTY:
                        System.out.println("CASE ERROR EMPTY");
                        excelOK = false;
                        throw new ParseException("Problème case vide dans votre excel", ERROR);

                    case ERROR_NUMERIC:
                        System.out.println("CASE ERROR NUMERIC");
                        excelOK = false;
                        throw new ParseException("Problème cellule non numérique sur 2ème colonne", ERROR);

                    case ERROR_DATEFORMAT:
                        excelOK = false;
                        throw new ParseException("Format de date incorrect dans une des cellules de la 1ère colonne", ERROR);

                    default:
                        break;
                }

                tempMap.put(row.getCell(0).getDateCellValue(),
                        new FrequentationJournaliere(row.getCell(0).getDateCellValue(), (int) row.getCell(1).getNumericCellValue()));
            }

            if (excelOK && !tempMap.isEmpty()) {
                for (Map.Entry<Date, FrequentationJournaliere> entry : tempMap.entrySet()) {
                    System.out.println("Adding to DB...");
                    DatabaseHelper.addFrequentationJournaliere(entry.getKey(), entry.getValue().getFrequentation());
                }

                GlobalVariableUtils.getExcelMap().clear();
                GlobalVariableUtils.getExcelMap().
                        putAll(DatabaseHelper.getAllFrequentationJournaliere());

                ArrayList<Integer> l = DatabaseHelper.getYearsComplete();
                for (int i = 0; i < l.size(); i++) {
                    int sum = DatabaseHelper.aggregateFrequentationOfYear(l.get(i));
                    DatabaseHelper.addFrequentationAnnuelle(l.get(i), sum);
                }
               
                GlobalVariableUtils.getFrequentationAnnuelleMap().clear();
                GlobalVariableUtils.getFrequentationAnnuelleMap().
                        putAll(DatabaseHelper.getAllFrequentationAnnuelle());

            } else {
                tempMap.clear();
                System.out.println("Erreur in Excel - Not adding to DB...");
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } catch (ParseException ex) {
            String str = ex.getMessage();
            ParametersDialog.msgbox(str);
            return false;
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return true;
    }

    public static List<FrequentationJournaliere> sortedListFromMap(HashMap<Date, FrequentationJournaliere> datas) {
        List<FrequentationJournaliere> listFreq = new ArrayList<FrequentationJournaliere>(datas.values());

        Collections.sort(listFreq, new Comparator<FrequentationJournaliere>() {

            @Override
            public int compare(FrequentationJournaliere o1, FrequentationJournaliere o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        return listFreq;
    }

    public static boolean exportExcel(String path, HashMap<Date, FrequentationJournaliere> datas) {
        try {

            List<FrequentationJournaliere> listFreq = sortedListFromMap(datas);

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet();

            CellStyle style = sheet.getWorkbook().createCellStyle();
            style.setDataFormat((short) BuiltinFormats.getBuiltinFormat("m/d/yy"));

            int rownum = 0;

            for (FrequentationJournaliere fj : listFreq) {
                Row row = sheet.createRow(rownum++);

                for (int i = 0; i < 2; i++) {
                    Cell cell = row.createCell(i);

                    if (i == 0) {
                        cell.setCellStyle(style);
                        cell.setCellValue((Date) fj.getDate());
                    } else {
                        cell.setCellValue(fj.getFrequentation());
                    }
                }
            }

            String ext = getExtension(new File(path));

            if (ext == null || !ext.equalsIgnoreCase(ExcelUtils.XLSX)) {
                path += "." + XLSX;
            }

            System.err.println("PATH : " + path);

            // Open file for writing
            FileOutputStream out
                    = new FileOutputStream(new File(path));
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully...");

            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    //Différents tests de l'excel pour détecter erreur
    private static TYPE_ERROR checkExcel(Row row) {

        if ((row.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK) || (row.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK)) {
            return TYPE_ERROR.ERROR_EMPTY;
        }

        if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
            if (row.getCell(0).getDateCellValue() == null) {
                return TYPE_ERROR.ERROR_DATEFORMAT;
            }
        }

        if (row.getCell(0).getCellType() != Cell.CELL_TYPE_NUMERIC) {
            return TYPE_ERROR.ERROR_DATEFORMAT;
        }

        if (row.getCell(1).getCellType() != Cell.CELL_TYPE_NUMERIC) {
            return TYPE_ERROR.ERROR_NUMERIC;
        }

        /* if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING){
         
         System.out.println("C'EST UNE STRING !!");
             
         if (isValidDate(row.getCell(0).getStringCellValue()) == false)
         {
         System.out.println("IS VALIDATE !!");
                
         return TYPE_ERROR.ERROR_DATEFORMAT;
         }
            
         }*/
        return TYPE_ERROR.NO_ERROR;
    }

    //Format de la date valide, à appeler pour vérifier
//    public boolean isValidDate(String dateString) {
//        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//        try {
//            df.parse(dateString);
//            return true;
//        } catch (ParseException e) {
//            return false;
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
