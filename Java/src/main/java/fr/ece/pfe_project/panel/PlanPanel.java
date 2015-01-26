package fr.ece.pfe_project.panel;

import fr.ece.pfe_project.database.DatabaseHelper;
import fr.ece.pfe_project.model.Camera;
import fr.ece.pfe_project.utils.GlobalVariableUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pierreghazal
 */
public class PlanPanel extends javax.swing.JPanel {

    private static final Map<Integer, String> alphabetMap;
    private static final Map<String, Integer> alphabetReverseMap;
    private static final Map<Integer, String> numbersMap;
    private static final Map<String, Integer> numbersReverseMap;

    static {
        Map<Integer, String> aMap1 = new HashMap<Integer, String>();
        aMap1.put(1, "A");
        aMap1.put(2, "B");
        aMap1.put(3, "C");
        aMap1.put(4, "D");
        aMap1.put(5, "E");
        aMap1.put(6, "F");
        aMap1.put(7, "G");
        aMap1.put(8, "H");
        aMap1.put(9, "I");
        aMap1.put(10, "J");
        aMap1.put(11, "K");
        aMap1.put(12, "L");
        aMap1.put(13, "M");
        aMap1.put(14, "N");
        aMap1.put(15, "O");
        aMap1.put(16, "P");
        aMap1.put(17, "Q");
        aMap1.put(18, "R");
        aMap1.put(19, "S");
        aMap1.put(20, "T");
        aMap1.put(21, "U");
        aMap1.put(22, "V");
        aMap1.put(23, "W");
        aMap1.put(24, "X");
        aMap1.put(25, "Y");
        aMap1.put(26, "Z");
        alphabetMap = Collections.unmodifiableMap(aMap1);

        alphabetReverseMap = new HashMap<String, Integer>();
        for (Map.Entry<Integer, String> entry : alphabetMap.entrySet()) {
            alphabetReverseMap.put(entry.getValue(), entry.getKey());
        }

        Map<Integer, String> aMap2 = new HashMap<Integer, String>();

        aMap2.put(1, "1");
        aMap2.put(2, "2");
        aMap2.put(3, "3");
        aMap2.put(4, "4");
        aMap2.put(5, "5");
        aMap2.put(6, "6");
        aMap2.put(7, "7");
        aMap2.put(8, "8");
        aMap2.put(9, "9");
        aMap2.put(10, "10");
        numbersMap = Collections.unmodifiableMap(aMap2);

        numbersReverseMap = new HashMap<String, Integer>();
        for (Map.Entry<Integer, String> entry : numbersMap.entrySet()) {
            numbersReverseMap.put(entry.getValue(), entry.getKey());
        }
    }

    private static final int COLUMN = 8;
    private static final int ROW = 2;
    private static final Color COLOR_ALERT = new Color(128, 0, 0, 128);

    private Integer indexPanel = 0;

    private ArrayList<Camera> cameras;

    public void setCameras(ArrayList<Camera> cameras) {
        this.cameras = cameras;
    }

    public ArrayList<Camera> getCameras() {
        return cameras;
    }

    /**
     * Creates new form PlanPanel
     */
    public PlanPanel() {
        initComponents();
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
            g2d.drawString(alphabetMap.get(i), x - COLUMN_WIDTH / 2, 15);
        }

        // Lignes
        for (int i = 1; i < ROW + 1; i++) {
            int y = i * ROW_HEIGHT;
            g2d.drawLine(0, y, getSize().width, y);
            g2d.drawString(numbersMap.get(i), 10, y - ROW_HEIGHT / 2);
        }

        g2d.setPaint(COLOR_ALERT);

        // Affichage alert
        for (Camera c : cameras) {
            switch (c.getState()) {
                case ALERT:
                    String colStr = c.getPosition().substring(0, 1);
                    String rowStr = c.getPosition().substring(1);

                    int col = alphabetReverseMap.get(colStr);
                    int row = numbersReverseMap.get(rowStr);

                    g2d.fillRect((col - 1) * COLUMN_WIDTH, (row - 1) * ROW_HEIGHT, COLUMN_WIDTH, ROW_HEIGHT);

                    break;
                default:
                    break;
            }
        }

        ArrayList<String> positions = new ArrayList<String>();

        for (int i = 0; i < COLUMN; i++) {
            for (int j = 0; j < ROW; j++) {
                positions.add(alphabetMap.get(i + 1) + numbersMap.get(j + 1));
            }
        }

        DatabaseHelper.setAllPositionsPlan(positions);
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
