/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ece.pfe_project.panel;

import fr.ece.pfe_project.model.ExcelRow;
import fr.ece.pfe_project.utils.ExcelUtils;
import static fr.ece.pfe_project.utils.ExcelUtils.getExcelFile;
import static fr.ece.pfe_project.utils.ExcelUtils.getSheet;
import static fr.ece.pfe_project.utils.ExcelUtils.getWorkbook;
import fr.ece.pfe_project.utils.GlobalVariableUtils;
import fr.ece.pfe_project.utils.ParametersUtils;
import fr.ece.pfe_project.widget.ProgressDialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author pierreghazal
 */
public class ParametersDialog extends javax.swing.JDialog {

        enum TYPE_ERROR{
           NO_ERROR, ERROR_EMPTY, ERROR_NUMERIC, ERROR_DATEFORMAT
        }
    /**
     * Creates new form ParametersDialog
     *
     * @param parent
     * @param modal
     */
    public ParametersDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        initParameters();
    }

    private void initParameters() {
        String paramPathExcel = (String) ParametersUtils.get(ParametersUtils.PARAM_PATH_EXCEL);

        if (paramPathExcel != null) {
            this.textFieldPathExcel.setText(paramPathExcel);
        }
    }

    public void loadExcel() {
        FileInputStream file = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Cell cell = null;
        boolean bool = true;

        try {
            file = getExcelFile((String) ParametersUtils.get(ParametersUtils.PARAM_PATH_EXCEL));
            XSSFWorkbook workbook = getWorkbook(file);
            XSSFSheet sheet = getSheet(workbook, 0);

            Iterator<Row> rows = sheet.iterator();

            while (rows.hasNext()) {
                Row row = rows.next();
                cell = row.getCell(0);
            
                System.out.println("BEFORE SWITCH");
                
            switch(checkExcel(row)){
                
                case ERROR_EMPTY:
                       System.out.println("CASE ERROR EMPTY");
                       throw new ParseException("Problème case vide dans votre excel", ERROR);
                    
                case ERROR_NUMERIC:
                       System.out.println("CASE ERROR NUMERIC");
                       throw new ParseException("Problème cellule non numérique sur 2ème colonne", ERROR);
                          
                case ERROR_DATEFORMAT:
                       throw new ParseException("Format de date incorrect dans une des cellules de la 1ère colonne", ERROR);
                
                default:
                    break;    
                
            }

              // ICI METTRE DONNEES EXCEL DANS BASE DE DONNEES
                GlobalVariableUtils.getExcelMap().put(row.getCell(0).getDateCellValue(),
                        new ExcelRow(row.getCell(0).getDateCellValue(), (int) row.getCell(1).getNumericCellValue()));
            }

            // Sert pour les tests
            GlobalVariableUtils.showExcelMap();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            String str=ex.getMessage();
            msgbox(str);
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //Poppup Message
    private void msgbox(String s) {

        JOptionPane.showMessageDialog(this, s, "Warning",JOptionPane.WARNING_MESSAGE);
    }

    //Format de la date valide, à appeler pour vérifier
    public boolean isValidDate(String dateString) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            df.parse(dateString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    //Différents tests de l'excel pour détecter erreur
    private TYPE_ERROR checkExcel(Row row) {

        System.out.println("ENTREE DANS CHECK EXCEL");
        
        if ((row.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK) || (row.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK)) {
           
            System.out.println("\nVIIDDEEEE\n");
            return TYPE_ERROR.ERROR_EMPTY;
        }
   
       /* if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING){
         
             System.out.println("C'EST UNE STRING !!");
             
            if (isValidDate(row.getCell(0).getStringCellValue()) == false)
            {
                System.out.println("IS VALIDATE !!");
                
                return TYPE_ERROR.ERROR_DATEFORMAT;
            }
            
        }*/
        
         if(row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC)
        {
            System.out.println("NUMERIC VRAIII");
            if(row.getCell(0).getDateCellValue() == null)
            {
                return TYPE_ERROR.ERROR_DATEFORMAT;
            }  
        } 
         
         if(row.getCell(0).getCellType() != Cell.CELL_TYPE_NUMERIC)
        {
            return TYPE_ERROR.ERROR_DATEFORMAT;
        }
        
        if (row.getCell(1).getCellType() != Cell.CELL_TYPE_NUMERIC) {
            
            return TYPE_ERROR.ERROR_NUMERIC;
        }
        
        return TYPE_ERROR.NO_ERROR;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        textFieldPathExcel = new javax.swing.JTextField();
        buttonBrowserExcel = new javax.swing.JButton();
        buttonValider = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Paramètres");
        setAlwaysOnTop(true);
        setModal(true);

        jLabel1.setText("Chemin d'accès Excel (xls, xlsx)");

        textFieldPathExcel.setFocusable(false);

        buttonBrowserExcel.setText("Parcourir");
        buttonBrowserExcel.setFocusable(false);
        buttonBrowserExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBrowserExcelActionPerformed(evt);
            }
        });

        buttonValider.setText("OK");
        buttonValider.setMaximumSize(new java.awt.Dimension(93, 29));
        buttonValider.setMinimumSize(new java.awt.Dimension(93, 29));
        buttonValider.setPreferredSize(new java.awt.Dimension(93, 29));
        buttonValider.setSize(new java.awt.Dimension(93, 29));
        buttonValider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonValiderActionPerformed(evt);
            }
        });

        buttonCancel.setText("Annuler");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(textFieldPathExcel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonBrowserExcel))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 317, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(buttonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonValider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buttonBrowserExcel)
                    .addComponent(textFieldPathExcel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 209, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonValider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonBrowserExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBrowserExcelActionPerformed
        final JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.addChoosableFileFilter(new ExcelUtils().new ExcelFilter());
        fc.setAcceptAllFileFilterUsed(false);

        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            if (file.exists()) {
                String path = file.getPath();

                this.textFieldPathExcel.setText(path);
            }
        } else {

        }
    }//GEN-LAST:event_buttonBrowserExcelActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void buttonValiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonValiderActionPerformed
        Runnable r = new Runnable() {

            @Override
            public void run() {

                //Test du format de l'excel
                // Saving parameters in memory
                if (textFieldPathExcel.getText() != null && textFieldPathExcel.getText().length() > 0) {
                    ParametersUtils.set(ParametersUtils.PARAM_PATH_EXCEL, textFieldPathExcel.getText());
                }

                // Saving parameters in file parameter
                ParametersUtils.saveParameters();

                loadExcel();

                // Dispose the parameter window
                dispose();
            }
        };

        ProgressDialog progressDialog = new ProgressDialog(this, r, "Veuillez patienter...");
        progressDialog.setVisible(true);
    }//GEN-LAST:event_buttonValiderActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonBrowserExcel;
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonValider;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField textFieldPathExcel;
    // End of variables declaration//GEN-END:variables
}
