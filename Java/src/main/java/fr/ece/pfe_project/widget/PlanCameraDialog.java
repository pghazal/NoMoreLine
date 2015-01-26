package fr.ece.pfe_project.widget;

import fr.ece.pfe_project.database.DatabaseHelper;
import fr.ece.pfe_project.model.Camera;
import fr.ece.pfe_project.tablemodel.CameraPlanTableModel;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author pierreghazal
 */
public class PlanCameraDialog extends javax.swing.JDialog {

    public class PositionEditor extends DefaultCellEditor {

        private JComboBox comboBox;
        private int selectedRow;

        public PositionEditor() {
            super(new JComboBox());

            this.comboBox = (JComboBox) super.getComponent();
            for (String pos : DatabaseHelper.getListePositionsCamera()) {
                comboBox.addItem(pos);
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            selectedRow = row;

            Camera c = model.getDatas().get(row);

            if (c.getPosition() == null || c.getPosition().equals(" - ")) {
                comboBox.setSelectedItem(" - ");
            } else {
                comboBox.setSelectedItem(c.getPosition());
            }

            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }

        @Override
        public Object getCellEditorValue() {
            String pos = this.comboBox.getSelectedItem().toString();
            System.out.println("getCellEditorValue : " + pos);

            Boolean success = true;

            Camera selectedCamera = model.getDatas().get(selectedRow);
            ArrayList<Camera> cameras = DatabaseHelper.getAllCamera();

            if (cameras != null) {
                for (Camera c : cameras) {
                    if (c.getPosition() != null && c.getPosition().equals(pos) && !c.getId().equals(selectedCamera.getId())) {
                        success = false;
                    }
                }
            }

            if (success) {
                selectedCamera.setPosition(pos);
                DatabaseHelper.updateCamera(selectedCamera.getId(), selectedCamera);

                return pos;
            } else {
                JOptionPane.showMessageDialog(PlanCameraDialog.this, "Une caméra occupe actuellement la position choisie.", "Erreur", JOptionPane.INFORMATION_MESSAGE);
                return selectedCamera.getPosition();
            }
        }
    }

    public class PositionRenderer extends DefaultTableCellRenderer {

        private JComboBox comboBox;

        public PositionRenderer() {
            super();
            comboBox = new JComboBox();
            for (String pos : DatabaseHelper.getListePositionsCamera()) {
                comboBox.addItem(pos);
            }
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String pos = DatabaseHelper.getAllCamera().get(row).getPosition();
            comboBox.setSelectedItem(pos);

            return comboBox;
        }
    }

    public class StringRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);

            if (isSelected) {
                c.setBackground(Color.BLUE);
                c.setForeground(Color.WHITE);
            } else {
                c.setForeground(Color.BLACK);
            }

            return c;
        }
    }

    private CameraPlanTableModel model;

    public PlanCameraDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);

        ArrayList<Camera> list = DatabaseHelper.getAllCamera();

        model = new CameraPlanTableModel(list);

        this.tableCameraPlan.setModel(model);

        this.tableCameraPlan.getColumnModel().getColumn(0).setCellRenderer(new StringRenderer());
        this.tableCameraPlan.getColumnModel().getColumn(1).setCellEditor(new PositionEditor());
        this.tableCameraPlan.getColumnModel().getColumn(1).setCellRenderer(new PositionRenderer());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        annulerButton = new javax.swing.JButton();
        validateButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCameraPlan = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(300, 200));

        annulerButton.setText("Annuler");
        annulerButton.setSize(new java.awt.Dimension(93, 29));
        annulerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annulerButtonActionPerformed(evt);
            }
        });

        validateButton.setText("OK");
        validateButton.setMaximumSize(new java.awt.Dimension(93, 29));
        validateButton.setMinimumSize(new java.awt.Dimension(93, 29));
        validateButton.setPreferredSize(new java.awt.Dimension(93, 29));
        validateButton.setSize(new java.awt.Dimension(93, 29));
        validateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validateButtonActionPerformed(evt);
            }
        });

        tableCameraPlan.setModel(new CameraPlanTableModel());
        tableCameraPlan.setFillsViewportHeight(true);
        tableCameraPlan.setFocusable(false);
        tableCameraPlan.setRequestFocusEnabled(false);
        tableCameraPlan.setRowHeight(25);
        tableCameraPlan.setRowSelectionAllowed(false);
        tableCameraPlan.setShowHorizontalLines(false);
        tableCameraPlan.setShowVerticalLines(false);
        tableCameraPlan.getTableHeader().setResizingAllowed(false);
        tableCameraPlan.getTableHeader().setReorderingAllowed(false);
        tableCameraPlan.setUpdateSelectionOnSort(false);
        jScrollPane1.setViewportView(tableCameraPlan);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 208, Short.MAX_VALUE)
                .addComponent(validateButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(annulerButton))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(annulerButton)
                    .addComponent(validateButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void annulerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annulerButtonActionPerformed
        dispose();
    }//GEN-LAST:event_annulerButtonActionPerformed

    private void validateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validateButtonActionPerformed
        // TODO : Ajouter/Update en base 

        dispose();
    }//GEN-LAST:event_validateButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton annulerButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableCameraPlan;
    private javax.swing.JButton validateButton;
    // End of variables declaration//GEN-END:variables
}
