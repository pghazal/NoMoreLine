/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ece.pfe_project.widget;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jdatepicker.ComponentManager;

/**
 *
 * @author pierreghazal
 */
public class StartingProgressDialog extends ProgressDialog {

    private Component parent;
    private JLabel icon;

    public StartingProgressDialog(JFrame parent, Runnable runnable, String message) {
        super(parent, runnable, message);
        this.parent = (JFrame) parent;
    }

    public StartingProgressDialog(JDialog parent, Runnable runnable, String message) {
        super(parent, runnable, message);
        this.parent = (JDialog) parent;
    }

    @Override
    protected void init(Runnable runnable, String message) {
        setupControls();
        setupComponent();
        setupEventHandlers();
        setMessage(message);
        setRunnable(runnable);
    }

    @Override
    protected void setupControls() {
        icon = new JLabel(ComponentManager.getInstance().getComponentIconDefaults().getLogoIcon());

        super.setupControls();
    }

    @Override
    protected void setupComponent() {
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = GridBagConstraints.RELATIVE;
        gc.anchor = GridBagConstraints.NORTHWEST;
        contentPane.add(icon, gc);
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(getLblMessage(), gc);
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(getProgressBar(), gc);

        setTitle("");
        setModal(true);
        setResizable(false);
        setUndecorated(true);
        pack();
    }

    @Override
    protected void setupEventHandlers() {

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent event) {
                final Thread task = new Thread(getRunnable());
                task.start();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            task.join();
                        } catch (InterruptedException e) {
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                setVisible(false);
                                parent.setVisible(true);
                            }
                        });
                    }
                }.start();
            }
        });
    }

}
