package fr.ece.pfe_project.panel;

import fr.ece.pfe_project.interfaces.ToolbarEntityListener;
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

    private final ToolbarsListener toolbarsListener;

    /**
     * Creates new form ToolbarEntity
     *
     * @param listener
     */
    public ToolbarEntityPanel(ToolbarsListener listener) {
        initComponents();

        this.toolbarsListener = listener;

        this.cameraButton.addActionListener(this);
        this.excelButton.addActionListener(this);
        this.listingVolsButton.addActionListener(this);
        this.carnetAdressesButton.addActionListener(this);
        this.planButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.cameraButton) {

            if (!cameraButton.isSelected()) {
                this.cameraButton.setSelected(false);
                this.toolbarsListener.entityHasChange(ToolbarEntityListener.ENTITY.NONE);
            } else {
                this.cameraButton.setSelected(true);
                this.excelButton.setSelected(false);
                this.planButton.setSelected(false);
                this.listingVolsButton.setSelected(false);
                this.carnetAdressesButton.setSelected(false);
                this.toolbarsListener.entityHasChange(ToolbarEntityListener.ENTITY.CAMERA);
            }

        } else if (e.getSource() == this.excelButton) {

            if (!excelButton.isSelected()) {
                this.excelButton.setSelected(false);
                this.toolbarsListener.entityHasChange(ToolbarEntityListener.ENTITY.NONE);
            } else {
                this.cameraButton.setSelected(false);
                this.excelButton.setSelected(true);
                this.planButton.setSelected(false);
                this.carnetAdressesButton.setSelected(false);
                this.listingVolsButton.setSelected(false);
                this.toolbarsListener.entityHasChange(ToolbarEntityListener.ENTITY.EXCELROW);
            }

        } else if (e.getSource() == this.listingVolsButton) {

            if (!listingVolsButton.isSelected()) {
                this.listingVolsButton.setSelected(false);
                this.toolbarsListener.entityHasChange(ToolbarEntityListener.ENTITY.NONE);
            } else {
                this.cameraButton.setSelected(false);
                this.excelButton.setSelected(false);
                this.planButton.setSelected(false);
                this.carnetAdressesButton.setSelected(false);
                this.listingVolsButton.setSelected(true);
                this.toolbarsListener.entityHasChange(ToolbarEntityListener.ENTITY.LISTINGVOLS);
            }

        } else if (e.getSource() == this.carnetAdressesButton) {

            if (!carnetAdressesButton.isSelected()) {
                this.carnetAdressesButton.setSelected(false);
                this.toolbarsListener.entityHasChange(ToolbarEntityListener.ENTITY.NONE);
            } else {
                this.cameraButton.setSelected(false);
                this.excelButton.setSelected(false);
                this.planButton.setSelected(false);
                this.listingVolsButton.setSelected(false);
                this.carnetAdressesButton.setSelected(true);
                this.toolbarsListener.entityHasChange(ToolbarEntityListener.ENTITY.CARNETADRESSE);
            }
        } else if (e.getSource() == this.planButton) {

            if (!planButton.isSelected()) {
                this.planButton.setSelected(false);
                this.toolbarsListener.entityHasChange(ToolbarEntityListener.ENTITY.NONE);
            } else {
                this.cameraButton.setSelected(false);
                this.excelButton.setSelected(false);
                this.carnetAdressesButton.setSelected(false);
                this.listingVolsButton.setSelected(false);
                this.planButton.setSelected(true);
                this.toolbarsListener.entityHasChange(ToolbarEntityListener.ENTITY.PLAN);
            }
        }
    }

    public void resetToggleButtons() {

        this.toolbarsListener.entityHasChange(ToolbarEntityListener.ENTITY.NONE);

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

        cameraButton = new javax.swing.JToggleButton();
        excelButton = new javax.swing.JToggleButton();
        listingVolsButton = new javax.swing.JToggleButton();
        carnetAdressesButton = new javax.swing.JToggleButton();
        planButton = new javax.swing.JToggleButton();

        setMaximumSize(new java.awt.Dimension(90, 32767));
        setPreferredSize(new java.awt.Dimension(90, 600));

        cameraButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nomoreline/img/video.png"))); // NOI18N
        cameraButton.setToolTipText("Système de détection par caméra");

        excelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nomoreline/img/excel.png"))); // NOI18N
        excelButton.setToolTipText("Fréquentations journalières");
        excelButton.setMaximumSize(new java.awt.Dimension(104, 29));
        excelButton.setMinimumSize(new java.awt.Dimension(104, 29));
        excelButton.setPreferredSize(new java.awt.Dimension(104, 29));

        listingVolsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nomoreline/img/plane.png"))); // NOI18N
        listingVolsButton.setToolTipText("Liste des vols");

        carnetAdressesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nomoreline/img/directory.png"))); // NOI18N
        carnetAdressesButton.setToolTipText("Carnet d'adresses");

        planButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nomoreline/img/map.png"))); // NOI18N
        planButton.setToolTipText("Plan de l'aéroport");
        planButton.setMaximumSize(new java.awt.Dimension(64, 72));
        planButton.setMinimumSize(new java.awt.Dimension(64, 72));
        planButton.setPreferredSize(new java.awt.Dimension(64, 72));
        planButton.setSize(new java.awt.Dimension(64, 72));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cameraButton, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                    .addComponent(listingVolsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(carnetAdressesButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(planButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(excelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cameraButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(planButton, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(excelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(carnetAdressesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listingVolsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(217, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton cameraButton;
    private javax.swing.JToggleButton carnetAdressesButton;
    private javax.swing.JToggleButton excelButton;
    private javax.swing.JToggleButton listingVolsButton;
    private javax.swing.JToggleButton planButton;
    // End of variables declaration//GEN-END:variables
}
