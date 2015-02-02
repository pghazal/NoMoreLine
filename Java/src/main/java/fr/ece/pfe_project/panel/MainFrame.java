package fr.ece.pfe_project.panel;

import fr.ece.pfe_project.algo.Algorithm;
import fr.ece.pfe_project.database.DatabaseHelper;
import fr.ece.pfe_project.model.JourFerie;
import fr.ece.pfe_project.model.Plan;
import fr.ece.pfe_project.parse.ParseUtils;
import fr.ece.pfe_project.utils.GlobalVariableUtils;
import fr.ece.pfe_project.utils.ParametersUtils;
import javax.swing.JOptionPane;
import fr.ece.pfe_project.widget.StartingProgressDialog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jdatepicker.ComponentIconDefaults;

/**
 *
 * @author pierreghazal
 */
public class MainFrame extends javax.swing.JFrame {

    private static MainPanel mainPanel;
    private ParametersDialog parametersDialog;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        setLocationRelativeTo(null);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }

                final MainFrame mf = new MainFrame();
                Runnable runnable = new Runnable() {

                    @Override
                    public void run() {
                        DatabaseHelper.initialize();
                        ParametersUtils.loadDatabase();
                        ParseUtils.initialize();

                        loadJourFerieDefaut();

                        try {
                            InputStream stream = ComponentIconDefaults.class.
                                    getResourceAsStream("/nomoreline/plan/plan.png");
                            BufferedImage image = ImageIO.read(stream);

                            GlobalVariableUtils.getPlans().add(
                                    new Plan(image, "Plan 1"));
                        } catch (IOException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        mainPanel = new MainPanel();
                        mf.setContentPane(mainPanel);
                    }
                };

                StartingProgressDialog startingProgressDialog
                        = new StartingProgressDialog(mf, runnable, "Chargement...");

                startingProgressDialog.setVisible(true);
            }
        });
    }

    private static void loadJourFerieDefaut() {
        ArrayList<JourFerie> jours = new ArrayList<JourFerie>();
        Calendar cal = Calendar.getInstance();

        JourFerie paques = Algorithm.paques(cal.get(Calendar.YEAR));
        jours.add(paques);
        jours.add(Algorithm.ascension(paques));
        jours.add(Algorithm.pentecote(paques));
        jours.add(Algorithm.vendrediSaint(paques));

        cal.set(Calendar.MILLISECOND, 0);

        cal.set(cal.get(Calendar.YEAR), 4, 1, 0, 0, 0);
        jours.add(new JourFerie("Fête du Travail", cal.getTime()));
        cal.set(cal.get(Calendar.YEAR), 0, 1, 0, 0, 0);
        jours.add(new JourFerie("Jour de l'an", cal.getTime()));
        cal.set(cal.get(Calendar.YEAR), 6, 14, 0, 0, 0);
        jours.add(new JourFerie("Fête Nationale", cal.getTime()));
        cal.set(cal.get(Calendar.YEAR), 10, 11, 0, 0, 0);
        jours.add(new JourFerie("Armistice", cal.getTime()));
        cal.set(cal.get(Calendar.YEAR), 4, 8, 0, 0, 0);
        jours.add(new JourFerie("8 Mai", cal.getTime()));
        cal.set(cal.get(Calendar.YEAR), 7, 15, 0, 0, 0);
        jours.add(new JourFerie("Assomption", cal.getTime()));
        cal.set(cal.get(Calendar.YEAR), 10, 1, 0, 0, 0);
        jours.add(new JourFerie("Toussaint", cal.getTime()));
        cal.set(cal.get(Calendar.YEAR), 11, 25, 0, 0, 0);
        jours.add(new JourFerie("Noël", cal.getTime()));
        cal.set(cal.get(Calendar.YEAR), 11, 26, 0, 0, 0);
        jours.add(new JourFerie("26 Décembre", cal.getTime()));

        ParametersUtils.set(ParametersUtils.PARAM_JOURS_FERIES, (ArrayList<JourFerie>) jours);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuParameter = new javax.swing.JMenuItem();
        jMenuQuitter = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("No More Line");
        setMinimumSize(new java.awt.Dimension(810, 600));
        setName("No More Line"); // NOI18N
        setPreferredSize(new java.awt.Dimension(2147483647, 2147483647));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jMenu1.setText("Fichier");

        jMenuParameter.setText("Paramètres");
        jMenuParameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuParameterActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuParameter);

        jMenuQuitter.setText("Quitter");
        jMenuQuitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuQuitterActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuQuitter);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 578, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuParameterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuParameterActionPerformed
        if (parametersDialog == null) {
            parametersDialog = new ParametersDialog(this, true);
            parametersDialog.setLocationRelativeTo(null);
        }

        if (!parametersDialog.isVisible()) {
            parametersDialog.setVisible(true);
        }
    }//GEN-LAST:event_jMenuParameterActionPerformed

    private void jMenuQuitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuQuitterActionPerformed
        formWindowClosing(null);
    }//GEN-LAST:event_jMenuQuitterActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        if (JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr(e) de vouloir quitter l'application ?", "Fermeture de l'application",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            // TODO : sauvegarde de l'état du logiciel

            System.exit(0);
        }
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuParameter;
    private javax.swing.JMenuItem jMenuQuitter;
    // End of variables declaration//GEN-END:variables
}
