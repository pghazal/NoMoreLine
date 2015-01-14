package fr.ece.pfe_project.panel;

import fr.ece.pfe_project.model.JourFerie;
import fr.ece.pfe_project.tablemodel.JourFerieTableModel;
import fr.ece.pfe_project.utils.ExcelUtils;
import fr.ece.pfe_project.utils.ParametersUtils;
import fr.ece.pfe_project.widget.ProgressDialog;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultCellEditor;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author pierreghazal
 */
public class ParametersDialog extends javax.swing.JDialog {

    private final Integer DEFAULT_SEUIL_JOUR = 4000;
    private final Integer DEFAULT_SEUIL_CAMERA = 70;

    private static JDialog dialog;

    SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

    public class DateRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            if (value instanceof Date) {
                String strDate = f.format((Date) value);
                this.setText(strDate);
            }

            return this;
        }
    }

    public class DateEditor extends DefaultCellEditor {

        private final JFormattedTextField textField;

        public DateEditor() {
            super(new JFormattedTextField(f));

            textField = (JFormattedTextField) super.getComponent();
            textField.setFont(new Font(textField.getFont().getName(), Font.PLAIN, 11));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column) {

            if (value instanceof Date) {

                String strDate = f.format((Date) value);
                this.textField.setText(strDate);
            }

            return this.textField;
        }

        @Override
        public Object getCellEditorValue() {

            try {
                if (textField.getText().trim().length() == 0) {
                    throw new ParseException("", 0);
                }

                java.util.Date utilDate = f.parse(textField.getText().trim());

                return utilDate;
            } catch (ParseException e) {
                msgbox("Format date invalide. Format accepté : jj/mm/aaaa");

                return textField.getText();
            }
        }
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

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        this.tableFeries.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
        this.tableFeries.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tableFeries.getColumnModel().getColumn(0).setMinWidth(20);
        this.tableFeries.getColumnModel().getColumn(2).
                setCellRenderer(new DateRenderer());
        this.tableFeries.getColumnModel().getColumn(2).
                setCellEditor(new DateEditor());

        initParameters();

        dialog = this;
    }

    private void initParameters() {
//        String paramPathExcel = (String) ParametersUtils.get(ParametersUtils.PARAM_PATH_EXCEL);
//
//        if (paramPathExcel != null) {
//            this.textFieldPathExcel.setText(paramPathExcel);
//        }

        Integer seuilJour = (Integer) ParametersUtils.get(ParametersUtils.PARAM_SUEIL_JOUR);
        Integer seuilCamera = (Integer) ParametersUtils.get(ParametersUtils.PARAM_SUEIL_CAMERA);

        if (seuilJour != null) {
            this.spinnerSeuilJour.setValue(seuilJour);
        } else {
            this.spinnerSeuilJour.setValue(DEFAULT_SEUIL_JOUR);
        }

        if (seuilCamera != null) {
            this.spinnerSeuilCamera.setValue(seuilCamera);
        } else {
            this.spinnerSeuilCamera.setValue(DEFAULT_SEUIL_CAMERA);
        }

        ArrayList<JourFerie> jours = (ArrayList<JourFerie>) ParametersUtils.get(ParametersUtils.PARAM_JOURS_FERIES);
        if (jours != null && jours.size() > 0) {
            ((JourFerieTableModel) this.tableFeries.getModel()).setData(jours, true);
        } else {
            // Ajouter les jours feries par defaut
            
        }
    }

    //Poppup Message
    public static void msgbox(String s) {
        JOptionPane.showMessageDialog(dialog, s, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonValider = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        spinnerSeuilJour = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        spinnerSeuilCamera = new javax.swing.JSpinner();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableFeries = new javax.swing.JTable();
        addJourFerieButton = new javax.swing.JButton();
        deleteJourFerieButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        textFieldPathExcel = new javax.swing.JTextField();
        buttonBrowserExcel = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        buttonReset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Paramètres");
        setAlwaysOnTop(true);
        setModal(true);

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

        jLabel2.setText("Seuil d'alerte journalier :");

        spinnerSeuilJour.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(4000), Integer.valueOf(0), null, Integer.valueOf(100)));

        jLabel3.setText("Seuil d'alerte par caméras :");

        spinnerSeuilCamera.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(70), Integer.valueOf(0), null, Integer.valueOf(10)));

        jScrollPane1.setBorder(null);

        tableFeries.setAutoCreateRowSorter(true);
        tableFeries.setModel(new JourFerieTableModel());
        tableFeries.setFillsViewportHeight(true);
        tableFeries.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableFeries.setShowGrid(false);
        tableFeries.setShowVerticalLines(true);
        tableFeries.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tableFeries);

        addJourFerieButton.setText("+");
        addJourFerieButton.setFocusable(false);
        addJourFerieButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJourFerieButtonActionPerformed(evt);
            }
        });

        deleteJourFerieButton.setText("-");
        deleteJourFerieButton.setFocusable(false);
        deleteJourFerieButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteJourFerieButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(addJourFerieButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteJourFerieButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(242, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 98, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addJourFerieButton)
                    .addComponent(deleteJourFerieButton)))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(33, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(spinnerSeuilJour, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                            .addComponent(spinnerSeuilCamera))))
                .addContainerGap(340, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(spinnerSeuilJour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(spinnerSeuilCamera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Général", jPanel1);

        jLabel1.setText("Chemin d'accès Excel (xls, xlsx)");

        textFieldPathExcel.setFocusable(false);

        buttonBrowserExcel.setText("Parcourir");
        buttonBrowserExcel.setFocusable(false);
        buttonBrowserExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBrowserExcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(textFieldPathExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonBrowserExcel)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldPathExcel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonBrowserExcel))
                .addContainerGap(149, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Importation", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 674, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 207, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Exportation", jPanel3);

        buttonReset.setText("Reset");
        buttonReset.setMaximumSize(new java.awt.Dimension(93, 29));
        buttonReset.setMinimumSize(new java.awt.Dimension(93, 29));
        buttonReset.setPreferredSize(new java.awt.Dimension(93, 29));
        buttonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(buttonReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonValider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonValider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonCancel)
                    .addComponent(buttonReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                Boolean isSuccess = false;

                // Saving parameters in memory
                if (textFieldPathExcel.getText() != null && textFieldPathExcel.getText().length() > 0) {
                    isSuccess = ExcelUtils.loadExcel(textFieldPathExcel.getText());
                }

                if ((int) spinnerSeuilJour.getValue() >= 0 && (int) spinnerSeuilCamera.getValue() >= 0) {
                    ParametersUtils.set(ParametersUtils.PARAM_SUEIL_JOUR, (int) spinnerSeuilJour.getValue());
                    ParametersUtils.set(ParametersUtils.PARAM_SUEIL_CAMERA, (int) spinnerSeuilCamera.getValue());

                    isSuccess = true;
                }

                JourFerieTableModel model = (JourFerieTableModel) tableFeries.getModel();
                ParametersUtils.set(ParametersUtils.PARAM_JOURS_FERIES, (ArrayList<JourFerie>) model.getDatas());

                if (isSuccess) {
                    // Reinit
                    textFieldPathExcel.setText("");

                    // Saving parameters in file parameter
                    ParametersUtils.saveParameters();
                    // Dispose the parameter window
                    dispose();
                }
            }
        };

        ProgressDialog progressDialog = new ProgressDialog(this, r, "Veuillez patienter...");
        progressDialog.setVisible(true);
    }//GEN-LAST:event_buttonValiderActionPerformed

    private void buttonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonResetActionPerformed
        // REMETTRE PARAMETRE PAR DEFAUT

        JourFerieTableModel model = (JourFerieTableModel) this.tableFeries.getModel();
        model.setData(null, true);
        ParametersUtils.set(ParametersUtils.PARAM_JOURS_FERIES, (ArrayList<JourFerie>) model.getDatas());

        // Afficher pop-up ?
        this.spinnerSeuilJour.setValue(DEFAULT_SEUIL_JOUR);
        this.spinnerSeuilCamera.setValue(DEFAULT_SEUIL_CAMERA);

        ParametersUtils.set(ParametersUtils.PARAM_SUEIL_JOUR, (int) spinnerSeuilJour.getValue());
        ParametersUtils.set(ParametersUtils.PARAM_SUEIL_CAMERA, (int) spinnerSeuilCamera.getValue());

        // Saving parameters in file parameter
        ParametersUtils.saveParameters();
    }//GEN-LAST:event_buttonResetActionPerformed

    private void addJourFerieButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addJourFerieButtonActionPerformed
        JourFerieTableModel model = (JourFerieTableModel) this.tableFeries.getModel();

        if (tableFeries.isEditing()) {
            tableFeries.getCellEditor().stopCellEditing();
        }

        model.add(new JourFerie());
    }//GEN-LAST:event_addJourFerieButtonActionPerformed

    private void deleteJourFerieButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteJourFerieButtonActionPerformed
        JourFerieTableModel model = (JourFerieTableModel) this.tableFeries.getModel();

        int index = this.tableFeries.getSelectedRow();

        // Si une ligne est selectionnee
        if (index != -1) {
            if (tableFeries.isEditing()) {
                tableFeries.getCellEditor().stopCellEditing();
            }
            model.removeDataAtRow(index);
        }
    }//GEN-LAST:event_deleteJourFerieButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addJourFerieButton;
    private javax.swing.JButton buttonBrowserExcel;
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonReset;
    private javax.swing.JButton buttonValider;
    private javax.swing.JButton deleteJourFerieButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JSpinner spinnerSeuilCamera;
    private javax.swing.JSpinner spinnerSeuilJour;
    private javax.swing.JTable tableFeries;
    private javax.swing.JTextField textFieldPathExcel;
    // End of variables declaration//GEN-END:variables
}
