package fr.ece.pfe_project.panel;

import fr.ece.pfe_project.editor.CameraCellEditor;
import fr.ece.pfe_project.model.Camera;
import fr.ece.pfe_project.model.Comptoir;
import fr.ece.pfe_project.model.Employee;
import fr.ece.pfe_project.model.FrequentationJournaliere;
import fr.ece.pfe_project.model.ListingVols;
import fr.ece.pfe_project.model.ModelInterface;
import fr.ece.pfe_project.panel.MainPanel.FaceDetectorListener;
import fr.ece.pfe_project.renderer.CameraCellRenderer;
import fr.ece.pfe_project.tablemodel.MyTableModel;
import fr.ece.pfe_project.utils.GlobalVariableUtils;
import fr.ece.pfe_project.widget.CameraCellComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import org.jdatepicker.ComponentManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import real_time_image_processing.FaceDetectorThread;

/**
 *
 * @author pierreghazal
 */
public class ListPanel extends javax.swing.JPanel implements FaceDetectorThread.FaceDetectorInterface,
        ToolbarEntityPanel.ToolbarEntityListener, MouseMotionListener, MouseListener, ActionListener {

    private final Comptoir comptoirs[];
    private final Camera cameras[];
    private final Employee employees[];
    private final ListingVols listingVols[];
    private boolean isCameraActive;
    //private boolean isRefreshBoutonActive;

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

        //Initialisation de la liste des vols
        listingVols = new ListingVols[]{};

        // Data initialization
        comptoirs = new Comptoir[]{
            new Comptoir(1), new Comptoir(2),
            new Comptoir(3), new Comptoir(4),
            new Comptoir(5), new Comptoir(6)
        };

        //setVisibility false pour rendre invisible les 2 combobox au démarrage
        setVisibility(false);

        //Rendre invisible au démarrage le bouton refresh
        setVisibilityRefresh(false);

        //SetCameraButtonVisibility false pour rendre invisible le bouton caméra au démmarage
        setCameraButtonVisibility(false);

        cameras = new Camera[]{
            new Camera(1)
        };

        //cameras[1].setState(Camera.CAMERA_STATE.ALERT); // SET THE 2ND CAMERA AS DECTECTING CROWD
        employees = new Employee[]{
            new Employee(), new Employee(),
            new Employee()
        };

        isCameraActive = false;
       // isRefreshBoutonActive = false;
        refreshButton.addActionListener(this);
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
                setVisibilityRefresh(false);
                setVisibility(false);
                setCameraButtonVisibility(false);
                itemsTable.setRowHeight(16);
                model.setData(comptoirs, false);
                break;
            case CAMERA:
                setVisibilityRefresh(false);
                setVisibility(false);
                setCameraButtonVisibility(true);
                itemsTable.setRowHeight(new CameraCellComponent().getPreferredSize().height);
                model.setData(cameras, false);
                CameraButton.addActionListener(this);
                break;
            case EXCELROW:
                setVisibilityRefresh(false);
                setVisibility(true);
                setCameraButtonVisibility(false);
                JComboboxItems(jComboBox1);
                itemsTable.setRowHeight(16);
                model.setData(GlobalVariableUtils.getExcelMap().values().toArray(new FrequentationJournaliere[0]), false);
                break;
            case LISTINGVOLS:
                setVisibilityRefresh(true);
                setVisibility(false);
                setCameraButtonVisibility(false);
                itemsTable.setRowHeight(16);
                //listingVols.addActionListener(this);
                //Fonction à lancer lors du clique bouton: listingVolsrecup
                if (!testConnexion()) {
                    model.setData((ListingVols[]) listingVolsrecup().toArray(new ListingVols[0]), false);
                } else {
                    JOptionPane.showMessageDialog(this, "Pas de connexion internet", "Warning", JOptionPane.WARNING_MESSAGE);
                }

                break;
            case NONE:
                setVisibility(false);
                setVisibilityRefresh(false);
                setCameraButtonVisibility(false);
                cameraInterface(false);
                break;

            default:
                break;
        }

        model.fireTableStructureChanged();
    }

    private void setVisibility(boolean bool) {

        if (bool == false) {
            jLabel1.setVisible(false);
            jComboBox1.setVisible(false);
            jComboBox2.setVisible(false);

        } else {
            jLabel1.setVisible(true);
            jComboBox1.setVisible(true);
            jComboBox2.setVisible(true);

        }

    }

    private void setCameraButtonVisibility(boolean bool) {

        if (bool == false) {
            CameraButton.setVisible(false);
        } else {
            CameraButton.setVisible(true);
            CameraButton.setOpaque(false);
            CameraButton.setContentAreaFilled(false);
            CameraButton.setBorderPainted(false);
            CameraButton.setIcon(ComponentManager.getInstance().getComponentIconDefaults().getgreenCameraIcon());

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (isCameraActive == true) {
            //On désactive les caméras 
            CameraButton.setIcon(ComponentManager.getInstance().getComponentIconDefaults().getgreenCameraIcon());
            cameraInterface(!isCameraActive);

            // CameraButton.setText("Activer caméra");
        } else //On change le label du bouton (de "activer caméra" à "désactiver caméra) et sa couleur
        {
            CameraButton.setIcon(ComponentManager.getInstance().getComponentIconDefaults().getredCameraIcon());
            //On lance l'activation des caméras une fois qu'on appuie sur le bouton
            cameraInterface(!isCameraActive);
        }

        if (e.getSource() == refreshButton) {
            
            System.out.println("Button Refresh clicked");
            //if (isRefreshBoutonActive) {
                listingVolsrecup();
            //}
        }

    }

    private void JComboboxItems(JComboBox comboBox) {
        String[] months = {"January", "February", "March", "April", "Mei", "June",
            "July", "August", "September", "October", "November", "December"};

        comboBox.setModel(new DefaultComboBoxModel(months));

    }

    public boolean testConnexion() {
        boolean internet = false;
        URL url;
        try {
            url = new URL("http://www.google.fr");
            HttpURLConnection urlConn;
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();
            internet = false;
        } catch (MalformedURLException ex) {
            Logger.getLogger(ListPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            internet = true;
            Logger.getLogger(ListPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return internet;
    }

    private void cameraInterface(boolean on) {

        isCameraActive = !isCameraActive;
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

    //Fonction pour rendre le bouton refresh visible
    private void setVisibilityRefresh(boolean bool) {

        if (bool == false) {
            refreshButton.setVisible(false);

        } else {
            refreshButton.setVisible(true);

        }

    }

    //Fonction pour récupérer la liste des vols
    private ArrayList listingVolsrecup() {
        //isRefreshBoutonActive = !isRefreshBoutonActive;
        try {
            //On se connecte au site et on charge le document html
            Document doc = Jsoup.connect("http://www.strasbourg.aeroport.fr/destinations/vols").get();
            //On récupère dans ce document la premiere balise ayant comme nom td et pour attribut class="center"
            int el = doc.select("td").size();
            int nb = 0;
            String[] tab = new String[6];
            ArrayList<ListingVols> ensembleDesVols = new ArrayList<ListingVols>();
            ListingVols listingVols = null;
            for (int i = 3; i < el; i++) {
                listingVols = new ListingVols();
                Element element = doc.select("td").get(i);
                String element1 = element.text();
                tab[nb] = element1;
                nb++;
                if (nb == 6) {
                    for (int j = 0; j < 6; j++) {
                        switch (j) {
                            case 0:
                                listingVols.setDate1(tab[j]);
                                break;
                            case 1:
                                listingVols.setHeure(tab[j]);
                                break;
                            case 2:
                                listingVols.setDestination(tab[j]);
                                break;
                            case 3:
                                listingVols.setNumeroVol(tab[j]);
                                break;
                            case 4:
                                listingVols.setCompagnie(tab[j]);
                                break;
                            case 5:
                                listingVols.setObservation(tab[j]);
                            default:
                                break;
                        }

                    }
                    nb = 0;
                    ensembleDesVols.add(listingVols);
                }

            }

            return ensembleDesVols;
        } catch (MalformedURLException | NumberFormatException | java.lang.ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
        } catch (IOException ex) {
            System.out.println(ex);
        }

        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSpinnerPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        CameraButton = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        refreshButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemsTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jSpinnerPanel.setPreferredSize(new java.awt.Dimension(40, 40));

        jLabel1.setText("Sélectionner date :");
        jSpinnerPanel.add(jLabel1);

        CameraButton.setPreferredSize(new java.awt.Dimension(35, 35));
        jSpinnerPanel.add(CameraButton);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jSpinnerPanel.add(jComboBox1);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jSpinnerPanel.add(jComboBox2);

        refreshButton.setText("Refresh");
        refreshButton.setMaximumSize(new java.awt.Dimension(90, 23));
        refreshButton.setMinimumSize(new java.awt.Dimension(35, 23));
        refreshButton.setPreferredSize(new java.awt.Dimension(70, 23));
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        jSpinnerPanel.add(refreshButton);

        add(jSpinnerPanel, java.awt.BorderLayout.PAGE_START);

        itemsTable.setAutoCreateRowSorter(true);
        itemsTable.setModel(new MyTableModel());
        itemsTable.setFillsViewportHeight(true);
        itemsTable.setShowVerticalLines(false);
        itemsTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(itemsTable);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_refreshButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CameraButton;
    private javax.swing.JTable itemsTable;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jSpinnerPanel;
    private javax.swing.JButton refreshButton;
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
