/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ece.pfe_project.panel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JToggleButton;
import fr.ece.pfe_project.panel.MainPanel.ToolbarsListener;

/**
 *
 * @author pierreghazal
 */
public class ToolbarEntityPanel extends javax.swing.JPanel implements ActionListener {

    public enum ENTITY {

        NONE, COMPTOIR, CAMERA, EXCELROW, LISTINGVOLS, CARNETADRESSE
    }

    public interface ToolbarEntityListener {

        public void entityHasChange(ENTITY typeEntity);
    }

    private final ToolbarsListener toolbarsListener;

    /**
     * Creates new form ToolbarEntity
     *
     * @param listener
     */
    public ToolbarEntityPanel(ToolbarsListener listener) {
        initComponents();

        this.toolbarsListener = listener;

        this.comptoirButton.addActionListener(this);
        this.comptoirButton.setActionCommand(ENTITY.COMPTOIR.toString());
        this.cameraButton.addActionListener(this);
        this.cameraButton.setActionCommand(ENTITY.CAMERA.toString());
        this.excelButton.addActionListener(this);
        this.excelButton.setActionCommand(ENTITY.EXCELROW.toString());
        this.listingVols.addActionListener(this);
        this.listingVols.setActionCommand(ENTITY.LISTINGVOLS.toString());
        this.carnetAdressesButton.addActionListener(this);
        this.carnetAdressesButton.setActionCommand(ENTITY.CARNETADRESSE.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (ENTITY.COMPTOIR.toString().equals(e.getActionCommand())) {

            if (!comptoirButton.isSelected()) {
                this.comptoirButton.setSelected(false);
                this.toolbarsListener.entityHasChange(ENTITY.NONE);
            } else {
                this.cameraButton.setSelected(false);
                this.comptoirButton.setSelected(true);
                this.excelButton.setSelected(false);
                this.listingVols.setSelected(false);
                this.toolbarsListener.entityHasChange(ENTITY.COMPTOIR);
            }

        } else if (ENTITY.CAMERA.toString().equals(e.getActionCommand())) {

            if (!cameraButton.isSelected()) {
                this.cameraButton.setSelected(false);
                this.toolbarsListener.entityHasChange(ENTITY.NONE);
            } else {
                this.cameraButton.setSelected(true);
                this.comptoirButton.setSelected(false);              
                this.excelButton.setSelected(false);
                this.listingVols.setSelected(false);
                this.toolbarsListener.entityHasChange(ENTITY.CAMERA);
            }

               
        } else if (ENTITY.EXCELROW.toString().equals(e.getActionCommand())) {

            if (!excelButton.isSelected()) {
                this.excelButton.setSelected(false);
                this.toolbarsListener.entityHasChange(ENTITY.NONE);
            } else {
                this.cameraButton.setSelected(false);
                this.comptoirButton.setSelected(false);
                this.excelButton.setSelected(true);
                this.listingVols.setSelected(false);
                this.toolbarsListener.entityHasChange(ENTITY.EXCELROW);
            }
            
            
        } else if (ENTITY.LISTINGVOLS.toString().equals(e.getActionCommand())) {

            if (!listingVols.isSelected()) {
                this.listingVols.setSelected(false);
                this.toolbarsListener.entityHasChange(ENTITY.NONE);
            } else {
                this.cameraButton.setSelected(false);
                this.comptoirButton.setSelected(false);              
                this.excelButton.setSelected(false);
                this.listingVols.setSelected(true);
                this.toolbarsListener.entityHasChange(ENTITY.LISTINGVOLS);
            }

        } else if (ENTITY.CARNETADRESSE.toString().equals(e.getActionCommand())) {

            if (!carnetAdressesButton.isSelected()) {
                this.carnetAdressesButton.setSelected(false);
                this.toolbarsListener.entityHasChange(ENTITY.NONE);
            } else {
                this.cameraButton.setSelected(false);
                this.comptoirButton.setSelected(false);              
                this.excelButton.setSelected(false);
                this.listingVols.setSelected(false);
                this.carnetAdressesButton.setSelected(true);
                this.toolbarsListener.entityHasChange(ENTITY.CARNETADRESSE);
            }

        }
    }

    public void resetToggleButtons() {

        this.toolbarsListener.entityHasChange(ENTITY.NONE);

        // Unselect every JToggleButton in the Container
        for (Component button : this.getComponents()) {
            if (button instanceof JToggleButton) {
                ((JToggleButton) button).setSelected(false);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        comptoirButton = new javax.swing.JToggleButton();
        cameraButton = new javax.swing.JToggleButton();
        excelButton = new javax.swing.JToggleButton();
        listingVols = new javax.swing.JToggleButton();
        carnetAdressesButton = new javax.swing.JToggleButton();

        setMaximumSize(new java.awt.Dimension(90, 32767));
        setPreferredSize(new java.awt.Dimension(90, 600));

        comptoirButton.setText("Comptoir");

        cameraButton.setText("Camera");
        cameraButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cameraButtonActionPerformed(evt);
            }
        });

        excelButton.setText("Excel");
        excelButton.setMaximumSize(new java.awt.Dimension(104, 29));
        excelButton.setMinimumSize(new java.awt.Dimension(104, 29));
        excelButton.setPreferredSize(new java.awt.Dimension(104, 29));
        excelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excelButtonActionPerformed(evt);
            }
        });

        listingVols.setText("Listing des vols");

        carnetAdressesButton.setText("Carnet d'adresses");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(carnetAdressesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(comptoirButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cameraButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(excelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(listingVols, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(comptoirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cameraButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(excelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listingVols, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(carnetAdressesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(207, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void excelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excelButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_excelButtonActionPerformed

    private void cameraButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cameraButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cameraButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton cameraButton;
    private javax.swing.JToggleButton carnetAdressesButton;
    private javax.swing.JToggleButton comptoirButton;
    private javax.swing.JToggleButton excelButton;
    private javax.swing.JToggleButton listingVols;
    // End of variables declaration//GEN-END:variables
}
