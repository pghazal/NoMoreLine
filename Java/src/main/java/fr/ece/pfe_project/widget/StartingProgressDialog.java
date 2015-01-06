/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ece.pfe_project.widget;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author pierreghazal
 */
public class StartingProgressDialog extends ProgressDialog {

    public StartingProgressDialog(JFrame parent, Runnable runnable, String message) {
        super(parent, runnable, message);
    }

    public StartingProgressDialog(JDialog parent, Runnable runnable, String message) {
        super(parent, runnable, message);
    }
}
