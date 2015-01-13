package fr.ece.pfe_project.panel;

import fr.ece.pfe_project.editor.CameraCellEditor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import fr.ece.pfe_project.model.Camera;
import fr.ece.pfe_project.model.Comptoir;
import fr.ece.pfe_project.model.Employee;
import fr.ece.pfe_project.model.FrequentationJournaliere;
import fr.ece.pfe_project.panel.MainPanel.FaceDetectorListener;
import fr.ece.pfe_project.renderer.CameraCellRenderer;
import fr.ece.pfe_project.tablemodel.MyTableModel;
import fr.ece.pfe_project.utils.GlobalVariableUtils;
import fr.ece.pfe_project.widget.CameraCellComponent;
import real_time_image_processing.FaceDetectorThread;

/**
 *
 * @author pierreghazal
 */
public class ListPanel extends javax.swing.JPanel implements FaceDetectorThread.FaceDetectorInterface,
        ToolbarEntityPanel.ToolbarEntityListener, MouseMotionListener, MouseListener {

    private final Comptoir comptoirs[];
    private final Camera cameras[];
    private final Employee employees[];

    FaceDetectorListener faceDetectorListener;

    /**
     * Creates new form DrawingPanel
     *
     * @param faceListener
     */
    public ListPanel(FaceDetectorListener faceListener) {
        initComponents();

        faceDetectorListener = faceListener;

        // Listener
        addMouseListener(this);
        addMouseMotionListener(this);

        // Data initialization
        comptoirs = new Comptoir[]{
            new Comptoir(1), new Comptoir(2),
            new Comptoir(3), new Comptoir(4),
            new Comptoir(5), new Comptoir(6)
        };

        cameras = new Camera[]{
            new Camera(1), new Camera(2),
            new Camera(3)
        };

        cameras[1].setState(Camera.CAMERA_STATE.ALERT);

        employees = new Employee[]{
            new Employee(), new Employee(),
            new Employee()
        };

        itemsTable.setDefaultRenderer(Camera.class, new CameraCellRenderer());
        itemsTable.setDefaultEditor(Camera.class, new CameraCellEditor());

        this.itemsTable.setModel(new MyTableModel());
    }

    @Override
    public void entityHasChange(ToolbarEntityPanel.ENTITY typeEntity) {
        System.out.println("List entityHasChange : " + typeEntity.toString());

        MyTableModel model = (MyTableModel) this.itemsTable.getModel();
        model.setEntity(typeEntity);

        // TODO : model.setData() avec les nouvelles données
        switch (typeEntity) {

            case COMPTOIR:
                itemsTable.setRowHeight(16);
                model.setData(comptoirs, false);
                break;
            case CAMERA:
                itemsTable.setRowHeight(new CameraCellComponent().getPreferredSize().height);
                model.setData(cameras, false);
                cameraInterface(true);
                break;
            case EXCELROW:
                itemsTable.setRowHeight(16);
                model.setData(GlobalVariableUtils.getExcelMap().values().toArray(new FrequentationJournaliere[0]), false);
                break;
            case NONE:
                cameraInterface(false);
                break;

            default:
                break;
        }

        model.fireTableStructureChanged();
    }

    private void cameraInterface(boolean on) {
        // On souhaite lancer l'activation des cameras
        if (on) {
            for (int i = 0; i < cameras.length; i++) {
                Camera cam = cameras[i];
                if (cam != null && cam.getFaceDetectorThread() != null
                        && !cam.getFaceDetectorThread().isActive()) {
                    cam.getFaceDetectorThread().launch(0);
                } else if (cam == null) {
                    cam = new Camera(i);
                    cam.setFaceDetectorThread(new FaceDetectorThread(faceDetectorListener));
                    cam.getFaceDetectorThread().launch(0);
                } else if (cam.getFaceDetectorThread() == null) {
                    cam.setFaceDetectorThread(new FaceDetectorThread(faceDetectorListener));
                    cam.getFaceDetectorThread().launch(0);
                } else if (cam.getFaceDetectorThread() != null
                        && cam.getFaceDetectorThread().isActive()) {
                    // do nothing
                }
            }
        } // Extinction du systeme de detection
        else {
            for (int i = 0; i < cameras.length; i++) {
                Camera cam = cameras[i];

                if (cam != null && cam.getFaceDetectorThread() != null
                        && !cam.getFaceDetectorThread().isActive()) {
                    // do nothing
                } else if (cam == null) {
                    // do nothing
                } else if (cam.getFaceDetectorThread() == null) {
                    // do nothing
                } else if (cam.getFaceDetectorThread() != null
                        && cam.getFaceDetectorThread().isActive()) {
                    cam.getFaceDetectorThread().stopFaceDetection();
                    cam.setFaceDetectorThread(null);
                }
            }
        }
    }

    @Override
    public void getCountFaceDetected(int number_of_faces) {
        System.out.println("List Panel NB FACES : " + number_of_faces);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        itemsTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        itemsTable.setAutoCreateRowSorter(true);
        itemsTable.setModel(new MyTableModel());
        itemsTable.setFillsViewportHeight(true);
        itemsTable.setShowVerticalLines(false);
        itemsTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(itemsTable);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable itemsTable;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

//    @Override
//    public void tableChanged(TableModelEvent e) {
//        
//        System.out.println("# Table Changed");
//        int row = e.getFirstRow();
//        int column = e.getColumn();
//        MyTableModel model = (MyTableModel) e.getSource();
//        String columnName = model.getColumnName(column);
//
//        Object data = model.getValueAt(row, column);
//
//    }
}
