/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ece.pfe_project.panel;

import fr.ece.pfe_project.interfaces.CameraListener;
import fr.ece.pfe_project.interfaces.ToolbarActionsListener;
import fr.ece.pfe_project.interfaces.ToolbarEntityListener;
import org.jdatepicker.ComponentManager;

/**
 *
 * @author pierreghazal
 */
public class ToolbarActionsPanel extends javax.swing.JPanel implements
        ToolbarEntityListener, CameraListener {

    private final MainPanel.ToolbarsListener toolbarsListener;

    /**
     * Creates new form ToolbarTop
     *
     * @param toolbarsListener
     */
    public ToolbarActionsPanel(MainPanel.ToolbarsListener toolbarsListener) {
        initComponents();

        this.toolbarsListener = toolbarsListener;
    }

    @Override
    public void changeCameraStatus(boolean cameraStatus) {
        if (cameraStatus == true) {
            cameraState.setIcon(ComponentManager.getInstance().getComponentIconDefaults().getonLedIcon());
            System.out.println("LED ON");
        } else {
            System.out.println("LED OFF");
            cameraState.setIcon(ComponentManager.getInstance().getComponentIconDefaults().getoffLedIcon());
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

        jOptionPane1 = new javax.swing.JOptionPane();
        buttonAdd = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        cameraState = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(32767, 50));
        setPreferredSize(new java.awt.Dimension(600, 50));

        buttonAdd.setText("+");
        buttonAdd.setEnabled(false);
        buttonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddActionPerformed(evt);
            }
        });

        buttonDelete.setText("Delete");
        buttonDelete.setEnabled(false);
        buttonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteActionPerformed(evt);
            }
        });

        cameraState.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jdatepicker/icons/off_led_icon.png"))); // NOI18N
        cameraState.setText("Status Camera");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 292, Short.MAX_VALUE)
                .addComponent(cameraState)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonAdd)
                    .addComponent(buttonDelete)
                    .addComponent(cameraState))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddActionPerformed
        this.toolbarsListener.performAction(ToolbarActionsListener.ACTION_ADD);
    }//GEN-LAST:event_buttonAddActionPerformed

    private void buttonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteActionPerformed
        this.toolbarsListener.performAction(ToolbarActionsListener.ACTION_DELETE);
    }//GEN-LAST:event_buttonDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JLabel cameraState;
    private javax.swing.JOptionPane jOptionPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void entityHasChange(ToolbarEntityListener.ENTITY typeEntity) {
        System.out.println("TBAction entityHasChange : " + typeEntity.toString());

        switch (typeEntity) {

            case CAMERA:
            case EXCELROW:
            case CARNETADRESSE:
                buttonAdd.setEnabled(true);
                buttonDelete.setEnabled(true);
                break;
            case LISTINGVOLS:
            case NONE:
            default:
                buttonAdd.setEnabled(false);
                buttonDelete.setEnabled(false);
                break;
        }
    }
}
