package fr.ece.pfe_project.panel;

import fr.ece.pfe_project.database.DatabaseHelper;
import fr.ece.pfe_project.editor.CameraCellEditor;
import fr.ece.pfe_project.model.Camera;
import fr.ece.pfe_project.model.CarnetAdresses;
import fr.ece.pfe_project.model.FrequentationJournaliere;
import fr.ece.pfe_project.model.ListingVols;
import fr.ece.pfe_project.panel.MainPanel.FaceDetectorListener;
import fr.ece.pfe_project.panel.MainPanel.ToolbarsListener;
import static fr.ece.pfe_project.panel.ParametersDialog.msgbox;
import fr.ece.pfe_project.renderer.CameraCellRenderer;
import fr.ece.pfe_project.tablemodel.MyTableModel;
import fr.ece.pfe_project.utils.ExcelUtils;
import fr.ece.pfe_project.utils.GlobalVariableUtils;
import fr.ece.pfe_project.widget.CameraCellComponent;
import fr.ece.pfe_project.widget.ProgressDialog;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
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

    public interface CameraStatusListener {

        public void changeCameraStatus(boolean cameraStatus);
    }

    SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

    public class DateEditor extends DefaultCellEditor {

        private final JFormattedTextField textField;

        public DateEditor() {
            super(new JFormattedTextField(f));

            textField = (JFormattedTextField) super.getComponent();
            textField.setFont(new Font(textField.getFont().getName(), Font.PLAIN, 11));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column) {

            if (value instanceof Date) {

                String strDate = f.format((Date) value);
                this.textField.setText(strDate);
            }

            return this.textField;
        }

        @Override
        public Object getCellEditorValue() {

            try {
                if (textField.getText().trim().length() == 0) {
                    throw new ParseException("", 0);
                }

                java.util.Date utilDate = f.parse(textField.getText().trim());

                return utilDate;
            } catch (ParseException e) {
                msgbox("Format date invalide. Format accepté : jj/mm/aaaa");

                return textField.getText();
            }
        }
    }

    public class DateRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            if (value instanceof Date) {
                String strDate = f.format((Date) value);
                this.setText(strDate);
            }

            this.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);

            if (isSelected) {
                this.setBackground(Color.BLUE);
                this.setForeground(Color.WHITE);
            } else {
                this.setForeground(Color.BLACK);
            }

            return this;
        }
    }

    public class MyDefaultRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);

            if (isSelected) {
                c.setBackground(Color.BLUE);
                c.setForeground(Color.WHITE);
            } else {
                c.setForeground(Color.BLACK);
            }

            return c;
        }
    }

    private final ArrayList<Camera> cameras;
    private final ArrayList<CarnetAdresses> carnetAdresses;
    private boolean isCameraActive;

    MyTableModel model;

    FaceDetectorListener faceDetectorListener;
    ToolbarsListener toolbarsListener;

    /**
     * Creates new form DrawingPanel
     *
     * @param faceListener
     * @param toolbarsListener
     */
    public ListPanel(FaceDetectorListener faceListener, ToolbarsListener toolbarsListener) {
        initComponents();

        this.faceDetectorListener = faceListener;
        this.toolbarsListener = toolbarsListener;

        // Listener
        addMouseListener(this);
        addMouseMotionListener(this);

        //Initialisation du carnet d'adresses
        carnetAdresses = new ArrayList<CarnetAdresses>();
        carnetAdresses.add(new CarnetAdresses("Air France", 3, "AIR FRANCE", "0 970 808 816"));
        carnetAdresses.add(new CarnetAdresses("Brussels Airlines", 2, "AVIAPARTNER", "03 88 64 73 11"));

        //setVisibility false pour rendre invisible les 2 combobox au démarrage
        setExcelButtonVisibility(false);
        setMonthComboboxItems();
        setYearComboboxItems();

        //Rendre invisible au démarrage le bouton refresh
        setVisibilityRefresh(false);

        //Rendre invisible au démarrage les boutons pour le carnet d'adresses
        setVisibilityCarnetAdresses(false);

        //SetcameraButtonVisibility false pour rendre invisible le bouton caméra au démmarage
        setCameraButtonVisibility(false);

        cameras = new ArrayList<Camera>();
        cameras.add(new Camera(1));

        //cameras[1].setState(Camera.CAMERA_STATE.ALERT); // SET THE 2ND CAMERA AS DECTECTING CROWD
        isCameraActive = false;
        refreshButton.addActionListener(this);

        //Ajout des listerner sur les boutons de la page carnet d'adresses
        ajouterCA.addActionListener(this);
        modifierCA.addActionListener(this);
        supprimerCA.addActionListener(this);
        cameraButton.addActionListener(this);

        this.itemsTable.setModel(new MyTableModel());

        itemsTable.setDefaultRenderer(Integer.class, new MyDefaultRenderer());
        itemsTable.setDefaultRenderer(String.class, new MyDefaultRenderer());
        itemsTable.setDefaultRenderer(Long.class, new MyDefaultRenderer());
        itemsTable.setDefaultRenderer(Date.class, new DateRenderer());
        itemsTable.setDefaultRenderer(Camera.class, new CameraCellRenderer());
        itemsTable.setDefaultEditor(Camera.class, new CameraCellEditor());

        itemsTable.setDefaultEditor(Date.class, new DateEditor());
    }

    @Override
    public void entityHasChange(ToolbarEntityPanel.ENTITY typeEntity) {
        System.out.println("List entityHasChange : " + typeEntity.toString());

        model = (MyTableModel) this.itemsTable.getModel();
        model.setEntity(typeEntity);

        switch (typeEntity) {

            case CAMERA:
                setVisibilityRefresh(false);
                setExcelButtonVisibility(false);
                setVisibilityCarnetAdresses(false);
                setCameraButtonVisibility(true);
                itemsTable.setRowHeight(new CameraCellComponent().getPreferredSize().height);
                model.setData(cameras, false);
                System.out.println(isCameraActive);
                break;
            case EXCELROW:
                setVisibilityRefresh(false);
                setExcelButtonVisibility(true);
                setCameraButtonVisibility(false);
                setVisibilityCarnetAdresses(false);
                itemsTable.setRowHeight(16);

                model.setData((ArrayList) ExcelUtils.sortedListFromMap(GlobalVariableUtils.getExcelMap()), false);
                System.out.println(isCameraActive);
                break;
            case LISTINGVOLS:

                Runnable r = new Runnable() {

                    @Override
                    public void run() {

                        //Fonction à lancer lors du clique bouton: listingVolsrecup
                        if (!testConnexion()) {

                            model.setData(listingVolsRecup(), false);

                            setExcelButtonVisibility(false);
                            setCameraButtonVisibility(false);
                            setVisibilityCarnetAdresses(false);
                            setVisibilityRefresh(true);
                            itemsTable.setRowHeight(16);
                        } else {

                            JOptionPane.showMessageDialog(ListPanel.this, "Pas de connexion internet :\n"
                                    + "Veuillez vérifier votre connexion.", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                };

                ProgressDialog progressDialog = new ProgressDialog(new JFrame(), r, "Récupération des données en cours...");
                progressDialog.setVisible(true);

                break;
            case CARNETADRESSE:
                setVisibilityRefresh(false);
                setExcelButtonVisibility(false);
                setCameraButtonVisibility(false);
                setVisibilityCarnetAdresses(true);
                itemsTable.setRowHeight(16);
                model.setData(carnetAdresses, false);
                System.out.println(isCameraActive);

                break;
            case NONE:
                setExcelButtonVisibility(false);
                setVisibilityRefresh(false);
                setCameraButtonVisibility(false);
                setVisibilityCarnetAdresses(false);
                System.out.println(isCameraActive);
                break;

            default:
                break;
        }

        model.fireTableStructureChanged();
    }

    private void setExcelButtonVisibility(boolean bool) {

        if (bool == false) {
            jLabel1.setVisible(false);
            monthComboBox.setVisible(false);
            yearComboBox.setVisible(false);

        } else {
            jLabel1.setVisible(true);
            monthComboBox.setVisible(true);
            yearComboBox.setVisible(true);
        }
    }

    private void setCameraButtonVisibility(boolean bool) {

        if (bool == false) {
            cameraButton.setVisible(false);
        } else {
            cameraButton.setVisible(true);
            cameraButton.setOpaque(false);
            cameraButton.setContentAreaFilled(false);
            cameraButton.setBorderPainted(false);
            //cameraButton.setIcon(ComponentManager.getInstance().getComponentIconDefaults().getgreenCameraIcon());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == cameraButton) {
            if (isCameraActive == true) {
                //On désactive les caméras 
                cameraInterface(!isCameraActive);
                cameraButton.setIcon(ComponentManager.getInstance().getComponentIconDefaults().getgreenCameraIcon());
                toolbarsListener.changeCameraStatus(isCameraActive);
                // cameraButton.setText("Activer caméra");
            } else //On change le label du bouton (de "activer caméra" à "désactiver caméra) et sa couleur
            {
                cameraInterface(!isCameraActive);
                toolbarsListener.changeCameraStatus(isCameraActive);
                cameraButton.setIcon(ComponentManager.getInstance().getComponentIconDefaults().getredCameraIcon());
                //On lance l'activation des caméras une fois qu'on appuie sur le bouton
            }
        }

        if (e.getSource() == refreshButton) {

            System.out.println("Button Refresh clicked");
            Runnable r = new Runnable() {

                @Override
                public void run() {

                    //Fonction à lancer lors du clique bouton: listingVolsrecup
                    if (!testConnexion()) {
                        model.setData(listingVolsRecup(), false);
                    } else {

                        JOptionPane.showMessageDialog(ListPanel.this, "Pas de connexion internet :\n"
                                + "Veuillez vérifier votre connexion.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            };

            ProgressDialog progressDialog = new ProgressDialog(new JFrame(), r, "Récupération des données en cours...");
            progressDialog.setVisible(true);
        }
    }

    private void setMonthComboboxItems() {
        DateFormatSymbols symbols = new DateFormatSymbols();
        String[] monthNames = symbols.getMonths();

        this.monthComboBox.setModel(new DefaultComboBoxModel(monthNames));
    }

    private void setYearComboboxItems() {
        ArrayList<Integer> listYears = DatabaseHelper.getYearsCompletePlusActual();
        Collections.sort(listYears);
        Integer[] years = listYears.toArray(new Integer[0]);

        yearComboBox.setModel(new DefaultComboBoxModel(years));
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

    private void cameraInterface(final boolean on) {

        isCameraActive = !isCameraActive;

        Runnable r = new Runnable() {

            @Override
            public void run() {
                // On souhaite lancer l'activation des cameras
                if (on) {
                    for (int i = 0; i < cameras.size(); i++) {
                        Camera cam = cameras.get(i);
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
                    for (int i = 0; i < cameras.size(); i++) {
                        Camera cam = cameras.get(i);

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
        };

        ProgressDialog progressDialog = new ProgressDialog(new JFrame(), r, "Traitement en cours...");
        if (on) {
            progressDialog.setMessage("Activation des caméras en cours...");
        } else {
            progressDialog.setMessage("Désactivation des caméras en cours...");
        }

        progressDialog.setVisible(true);
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

    //Fonction pour rendre le bouton refresh visible
    private void setVisibilityCarnetAdresses(boolean bool) {

        if (bool == false) {
            ajouterCA.setVisible(false);
            modifierCA.setVisible(false);
            supprimerCA.setVisible(false);

        } else {
            ajouterCA.setVisible(true);
            modifierCA.setVisible(true);
            supprimerCA.setVisible(true);
        }
    }

    //Fonction pour récupérer la liste des vols
    private ArrayList<ListingVols> listingVolsRecup() {
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
        cameraButton = new javax.swing.JButton();
        monthComboBox = new javax.swing.JComboBox();
        yearComboBox = new javax.swing.JComboBox();
        refreshButton = new javax.swing.JButton();
        supprimerCA = new javax.swing.JButton();
        modifierCA = new javax.swing.JButton();
        ajouterCA = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemsTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jSpinnerPanel.setPreferredSize(new java.awt.Dimension(40, 40));

        jLabel1.setText("Sélectionner date :");
        jSpinnerPanel.add(jLabel1);

        cameraButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jdatepicker/icons/greencamera.png"))); // NOI18N
        cameraButton.setPreferredSize(new java.awt.Dimension(35, 35));
        jSpinnerPanel.add(cameraButton);

        monthComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jSpinnerPanel.add(monthComboBox);

        yearComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jSpinnerPanel.add(yearComboBox);

        refreshButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jdatepicker/icons/refresh.png"))); // NOI18N
        refreshButton.setBorderPainted(false);
        refreshButton.setContentAreaFilled(false);
        refreshButton.setMaximumSize(new java.awt.Dimension(35, 35));
        refreshButton.setMinimumSize(new java.awt.Dimension(35, 35));
        refreshButton.setPreferredSize(new java.awt.Dimension(35, 35));
        jSpinnerPanel.add(refreshButton);

        supprimerCA.setText("Supprimer");
        jSpinnerPanel.add(supprimerCA);

        modifierCA.setText("Modifier");
        jSpinnerPanel.add(modifierCA);

        ajouterCA.setText("Ajouter");
        jSpinnerPanel.add(ajouterCA);

        add(jSpinnerPanel, java.awt.BorderLayout.PAGE_START);

        itemsTable.setAutoCreateRowSorter(true);
        itemsTable.setModel(new MyTableModel());
        itemsTable.setFillsViewportHeight(true);
        itemsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        itemsTable.setShowHorizontalLines(false);
        itemsTable.setShowVerticalLines(false);
        itemsTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(itemsTable);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ajouterCA;
    private javax.swing.JButton cameraButton;
    private javax.swing.JTable itemsTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jSpinnerPanel;
    private javax.swing.JButton modifierCA;
    private javax.swing.JComboBox monthComboBox;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton supprimerCA;
    private javax.swing.JComboBox yearComboBox;
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
}
