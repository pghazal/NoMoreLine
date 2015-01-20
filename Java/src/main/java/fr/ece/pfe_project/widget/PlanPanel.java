/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ece.pfe_project.widget;

import fr.ece.pfe_project.utils.GlobalVariableUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;
import javax.swing.JLabel;

/**
 *
 * @author pierreghazal
 */
public class PlanPanel extends javax.swing.JPanel {

    private final List<String> alphabet
            = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N");

    private final List<String> numbers
            = Arrays.asList("1", "2", "3s");

    private static final int COLUMN = 8;
    private static final int ROW = 2;

    private JLabel imageLabel;
    private Integer indexPanel = 0;

    /**
     * Creates new form PlanPanel
     */
    public PlanPanel() {
        initComponents();
        imageLabel = new JLabel();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(GlobalVariableUtils.getPlans().get(indexPanel).getImage(),
                0, 0, this.getWidth(), this.getHeight(), null);

        g2d.setPaint(Color.DARK_GRAY);
        g2d.setFont(new Font(g2d.getFont().getName(), Font.BOLD, 20));
        g2d.setStroke(new BasicStroke(2));

        int COLUMN_WIDTH = getSize().width / COLUMN;
        int ROW_HEIGHT = getSize().height / ROW;

        // Colonnes
        for (int i = 1; i < COLUMN + 1; i++) {
            int x = i * COLUMN_WIDTH;
            g2d.drawLine(x, 0, x, getSize().height);
            g2d.drawString(alphabet.get(i - 1), x - COLUMN_WIDTH / 2, 15);
        }

        // Lignes
        for (int i = 1; i < ROW + 1; i++) {
            int y = i * ROW_HEIGHT;
            g2d.drawLine(0, y, getSize().width, y);
            g2d.drawString(numbers.get(i - 1), 10, y - ROW_HEIGHT / 2);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
