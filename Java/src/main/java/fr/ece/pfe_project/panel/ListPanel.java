package fr.ece.pfe_project.panel;

import fr.ece.pfe_project.database.DatabaseHelper;
import static fr.ece.pfe_project.database.DatabaseHelper.carnetAdresseExists;
import fr.ece.pfe_project.editor.CameraCellEditor;
import fr.ece.pfe_project.interfaces.ToolbarActionsListener;
import fr.ece.pfe_project.interfaces.ToolbarEntityListener;
import fr.ece.pfe_project.interfaces.ToolbarEntityListener.ENTITY;
import static fr.ece.pfe_project.interfaces.ToolbarEntityListener.ENTITY.CAMERA;
import static fr.ece.pfe_project.interfaces.ToolbarEntityListener.ENTITY.CARNETADRESSE;
import static fr.ece.pfe_project.interfaces.ToolbarEntityListener.ENTITY.EXCELROW;
import static fr.ece.pfe_project.interfaces.ToolbarEntityListener.ENTITY.LISTINGVOLS;
import static fr.ece.pfe_project.interfaces.ToolbarEntityListener.ENTITY.NONE;
import fr.ece.pfe_project.model.Camera;
import fr.ece.pfe_project.model.CarnetAdresses;
import fr.ece.pfe_project.model.FrequentationJournaliere;
import fr.ece.pfe_project.model.ListingVols;
import fr.ece.pfe_project.panel.MainPanel.FaceDetectorListener;
import fr.ece.pfe_project.panel.MainPanel.ToolbarsListener;
import static fr.ece.pfe_project.panel.ParametersDialog.msgbox;
import fr.ece.pfe_project.parse.ParseUtils;
import fr.ece.pfe_project.renderer.CameraCellRenderer;
import fr.ece.pfe_project.tablemodel.MyTableModel;
import fr.ece.pfe_project.utils.ExcelUtils;
import fr.ece.pfe_project.utils.ParametersUtils;
import fr.ece.pfe_project.widget.CameraCellComponent;
import fr.ece.pfe_project.widget.CameraSaisieDialog;
import fr.ece.pfe_project.widget.CarnetAdressesDialog;
import fr.ece.pfe_project.widget.ExcelSaisieDialog;
import fr.ece.pfe_project.widget.PlanCameraDialog;
import fr.ece.pfe_project.widget.ProgressDialog;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
public class ListPanel extends JPanel implements FaceDetectorThread.FaceDetectorInterface,
        ToolbarEntityListener, ToolbarActionsListener, ActionListener {

    final static SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
    final static Color ORANGE_CUSTOM = new Color(235, 206, 157);

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

            this.setBackground(row % 2 == 0 ? ORANGE_CUSTOM : Color.WHITE);

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

            c.setBackground(row % 2 == 0 ? ORANGE_CUSTOM : Color.WHITE);

            if (isSelected) {
                c.setBackground(Color.BLUE);
                c.setForeground(Color.WHITE);
            } else {
                c.setForeground(Color.BLACK);
            }

            return c;
        }
    }

    private class ImageLabel extends JLabel {

        private Image _myimage;

        public ImageLabel(String text) {
            super(text);
        }

        @Override
        public void setIcon(Icon icon) {
            super.setIcon(icon);
            if (icon instanceof ImageIcon) {
                _myimage = ((ImageIcon) icon).getImage();
            }
        }

        @Override
        public void paint(Graphics g) {
            g.drawImage(_myimage, 0, 0, getParent().getWidth(), getParent().getHeight(), null);
        }
    }

    private ArrayList<Camera> cameras;
    private boolean isCameraActive;

    private MyTableModel model;

    private FaceDetectorListener faceDetectorListener;
    private ToolbarsListener toolbarsListener;

    private PlanPanel planPanel;
    private ImageLabel tutoImg;

    /**
     * Creates new form DrawingPanel
     *
     * @param faceListener
     * @param toolbarsListener
     */
    public ListPanel(FaceDetectorListener faceListener, ToolbarsListener toolbarsListener) {
        initComponents();

        tutoImg = new ImageLabel("tuto");
        tutoImg.setIcon(ComponentManager.getInstance().getComponentIconDefaults().getTutoIcon());
        scrollPaneTuto.setViewportView(tutoImg);
        tutoImg.repaint();
        ((CardLayout) (cardPanel.getLayout())).show(cardPanel, "tuto");

        parameterButton.setText("");
        parameterButton.setIcon(ComponentManager.getInstance().getComponentIconDefaults().getParameterIcon());
        parameterButton.setToolTipText("Paramétrer");
        parameterButton.setBorderPainted(false);
        parameterButton.setContentAreaFilled(false);

        planPanel = new PlanPanel();
        planPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                planPanelComponentResized(evt);
            }
        });
        this.scrollPanePlans.setViewportView(planPanel);
        planPanel.repaint();

        this.faceDetectorListener = faceListener;
        this.toolbarsListener = toolbarsListener;

        //Initialisation du carnet d'adresses
        //carnetAdressesA.add(new CarnetAdresses("Air France", 3, "AIR FRANCE", "0 970 808 816", 1));
        //carnetAdressesA.add(new CarnetAdresses("Brussels Airlines", 2, "AVIAPARTNER", "03 88 64 73 11", 2));
        //setVisibility false pour rendre invisible les 2 combobox au démarrage
//        setExcelButtonVisibility(false);
//        setMonthComboboxItems();
//        setYearComboboxItems();
        //Rendre invisible au démarrage le bouton refresh
        setVisibilityRefresh(false);

        //SetcameraButtonVisibility false pour rendre invisible le bouton caméra au démmarage
        setCameraButtonVisibility(false);
        cameras = DatabaseHelper.getAllCamera();

        setPlanButtonVisibility(false);

        //cameras[1].setState(Camera.CAMERA_STATE.ALERT); // SET THE 2ND CAMERA AS DECTECTING CROWD
        isCameraActive = false;
        refreshButton.addActionListener(this);

        //Ajout des listerner sur les boutons de la page carnet d'adresses
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

    private void planPanelComponentResized(ComponentEvent evt) {
        planPanel.setCameras(cameras);
        planPanel.repaint();
    }

    @Override
    public void performAction(int action) {

        final ENTITY entity = model.getEntity();

        switch (action) {
            case ACTION_ADD:
                switch (entity) {
                    case CAMERA:
                        if (!isCameraActive) {
                            CameraSaisieDialog csd = new CameraSaisieDialog(null, true);
                            csd.setVisible(true);

                            Camera cameraToAdd = csd.getCamera();
                            if (cameraToAdd == null) {
                                System.err.println("Camera NULL");
                                // Ne rien ajouter
                            } else {
                                System.out.println("Camera : " + cameraToAdd.getId());

                                if (DatabaseHelper.cameraExists(cameraToAdd) == false) {
                                    DatabaseHelper.addCamera(cameraToAdd);
                                    ((ArrayList<Camera>) model.getData()).add(cameraToAdd);
                                    cameras.add(cameraToAdd);
                                    model.fireTableDataChanged();
                                } else {
                                    cameraToAdd = null;
                                    JOptionPane.showMessageDialog(this, "L'élément que vous voulez enregistrer existe déjà en base de donnée", "Erreur", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Veuillez avant tout désactiver la détection par caméra.",
                                    "WARNING", JOptionPane.WARNING_MESSAGE);
                        }

                        break;
                    case CARNETADRESSE:
                        CarnetAdressesDialog cad = new CarnetAdressesDialog(null, true);
                        cad.setVisible(true);

                        CarnetAdresses carnetToAdd = cad.getCa();
                        if (carnetToAdd == null) {
                            System.err.println("Excel NULL");
                        } else {
                            //Enregistrement dans la bdd
                            if (carnetAdresseExists(carnetToAdd) == false) {
                                DatabaseHelper.addCarnetAdresses(carnetToAdd);

                                ((ArrayList<CarnetAdresses>) model.getData()).add(carnetToAdd);
                                model.fireTableDataChanged();
                            } else {
                                carnetToAdd = null;
                                JOptionPane.showMessageDialog(this, "L'élément que vous voulez enregistrer existe déjà en base de donnée", "Erreur", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                        break;
                    case EXCELROW:
                        ExcelSaisieDialog esd = new ExcelSaisieDialog(null, true);
                        esd.setVisible(true);

                        FrequentationJournaliere excelToAdd = esd.getFj();
                        if (excelToAdd == null) {
                            System.err.println("Excel NULL");
                        } else {

                            if (DatabaseHelper.frequentationJournaliereExists(excelToAdd.getDate()) == false) {
                                DatabaseHelper.addFrequentationJournaliere(excelToAdd.getDate(), excelToAdd.getFrequentation());
                                System.out.println("Frequentation journaliere de " + excelToAdd.getFrequentation() + " Pour le " + excelToAdd.getDate());
                                ((ArrayList<FrequentationJournaliere>) model.getData()).add(excelToAdd);
                                model.fireTableDataChanged();
                            } else {
                                carnetToAdd = null;
                                JOptionPane.showMessageDialog(this, "L'élément que vous voulez enregistrer existe déjà en base de donnée", "Erreur", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                        break;
                    default:
                        break;
                }

                break;
            case ACTION_DELETE:
                switch (entity) {
                    case CAMERA:
                        if (!isCameraActive) {
                            // Si il y a une ligne selectionnée dans la Table
                            if (itemsTable.getSelectedRowCount() > 0) {
                                Camera selectedCamera = (Camera) model.getDataAtRow(itemsTable.getSelectedRow());
                                if (selectedCamera != null) {
                                    DatabaseHelper.deleteCamera(selectedCamera);
                                    ((ArrayList<Camera>) model.getData()).remove(selectedCamera);
                                    cameras.remove(selectedCamera);
                                    model.fireTableDataChanged();
                                }
                            } else {
                                JOptionPane.showMessageDialog(this,
                                        "Aucun élément sélectionné dans la liste",
                                        "Erreur",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Veuillez avant tout désactiver la détection par caméra.",
                                    "WARNING", JOptionPane.WARNING_MESSAGE);
                        }

                        break;
                    case CARNETADRESSE:
                        if (itemsTable.getSelectedRowCount() > 0) {
                            CarnetAdresses selectedCarnet = (CarnetAdresses) model.getDataAtRow(itemsTable.getSelectedRow());
                            if (selectedCarnet != null) {
                                DatabaseHelper.deleteCarnetAdresse(selectedCarnet);
                                ((ArrayList<CarnetAdresses>) model.getData()).remove(selectedCarnet);
                                model.fireTableDataChanged();
                            }
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Aucun élément sélectionné dans la liste",
                                    "Erreur",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    case EXCELROW:
                        if (itemsTable.getSelectedRowCount() > 0) {
                            FrequentationJournaliere selectedFj = (FrequentationJournaliere) model.getDataAtRow(itemsTable.getSelectedRow());
                            if (selectedFj != null) {
                                DatabaseHelper.deleteFrequentationJournaliere(selectedFj.getDate());
                                ((ArrayList<FrequentationJournaliere>) model.getData()).remove(selectedFj);
                                model.fireTableDataChanged();
                            }
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Aucun élément sélectionné dans la liste",
                                    "Erreur",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case ACTION_EDIT:
                switch (entity) {
                    case CAMERA:
                        if (!isCameraActive) {
                            // Si il y a une ligne selectionnée dans la Table
                            if (itemsTable.getSelectedRowCount() > 0) {
                                Camera selectedCamera = (Camera) model.getDataAtRow(itemsTable.getSelectedRow());
                                if (selectedCamera != null) {
                                    CameraSaisieDialog csd = new CameraSaisieDialog(null, true);
                                    csd.setCamera(selectedCamera);
                                    csd.setVisible(true);

                                    Camera newCamera = csd.getCamera();
                                    if (DatabaseHelper.cameraExists(selectedCamera)) {
                                        Integer oldId = selectedCamera.getId();

                                        DatabaseHelper.updateCamera(oldId, newCamera);
                                        ((ArrayList<Camera>) model.getData()).set(itemsTable.getSelectedRow(), newCamera);
                                        cameras.set(itemsTable.getSelectedRow(), newCamera);
                                        model.fireTableDataChanged();
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(this,
                                        "Aucun élément sélectionné dans la liste",
                                        "Erreur",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Veuillez avant tout désactiver la détection par caméra.",
                                    "WARNING", JOptionPane.WARNING_MESSAGE);
                        }

                        break;
                    case CARNETADRESSE:
                        if (itemsTable.getSelectedRowCount() > 0) {
                            CarnetAdresses selectedCarnet = (CarnetAdresses) model.getDataAtRow(itemsTable.getSelectedRow());
                            if (selectedCarnet != null) {
                                CarnetAdressesDialog cad = new CarnetAdressesDialog(null, true);
                                cad.setCa(selectedCarnet);
                                cad.setVisible(true);

                                CarnetAdresses newCarnet = cad.getCa();

                                //Enregistrement dans la bdd
                                if (carnetAdresseExists(selectedCarnet)) {
                                    Integer oldId = selectedCarnet.getId();
                                    DatabaseHelper.updateCarnetAdresses(oldId, newCarnet);

                                    ((ArrayList<CarnetAdresses>) model.getData()).set(itemsTable.getSelectedRow(), newCarnet);
                                    model.fireTableDataChanged();
                                } else {
                                    selectedCarnet = null;
                                    JOptionPane.showMessageDialog(this, "L'élément que vous voulez enregistrer existe déjà en base de donnée", "Erreur", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Aucun élément sélectionné dans la liste",
                                    "Erreur",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    case EXCELROW:
                        if (itemsTable.getSelectedRowCount() > 0) {
                            FrequentationJournaliere selectedFreq = (FrequentationJournaliere) model.getDataAtRow(itemsTable.getSelectedRow());
                            if (selectedFreq != null) {
                                ExcelSaisieDialog esd = new ExcelSaisieDialog(null, true);
                                esd.setFj(selectedFreq);
                                esd.setVisible(true);
                                FrequentationJournaliere newFreq = esd.getFj();

                                if (DatabaseHelper.frequentationJournaliereExists(selectedFreq.getDate())) {
                                    DatabaseHelper.updateFrequentationJournaliere(selectedFreq.getDate(), newFreq.getDate(), newFreq.getFrequentation());

                                    ((ArrayList<FrequentationJournaliere>) model.getData()).set(itemsTable.getSelectedRow(), newFreq);
                                    model.fireTableDataChanged();
                                } else {
                                    selectedFreq = null;
                                    JOptionPane.showMessageDialog(this, "L'élément que vous voulez enregistrer existe déjà en base de donnée", "Erreur", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Aucun élément sélectionné dans la liste",
                                    "Erreur",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void entityHasChange(ENTITY typeEntity) {
        System.out.println("List entityHasChange : " + typeEntity.toString());

        model = (MyTableModel) this.itemsTable.getModel();
        model.setEntity(typeEntity);
        final CardLayout cl = (CardLayout) (cardPanel.getLayout());

        switch (typeEntity) {

            case CAMERA:
                setVisibilityRefresh(false);
                //setExcelButtonVisibility(false);
                setCameraButtonVisibility(true);
                setPlanButtonVisibility(false);
                itemsTable.setRowHeight(new CameraCellComponent().getPreferredSize().height);
                model.setData(DatabaseHelper.getAllCamera(), false);
                cl.show(cardPanel, "table");
                break;

            case EXCELROW:
                setVisibilityRefresh(false);
                //setExcelButtonVisibility(true);
                setCameraButtonVisibility(false);
                setPlanButtonVisibility(false);
                itemsTable.setRowHeight(16);
                model.setData((ArrayList) ExcelUtils.sortedListFromMap(DatabaseHelper.getAllFrequentationJournaliere()), false);
                cl.show(cardPanel, "table");
                break;

            case LISTINGVOLS:

                Runnable r = new Runnable() {

                    @Override
                    public void run() {

                        //Fonction à lancer lors du clique bouton: listingVolsrecup
                        if (!testConnexion()) {

                            model.setData(listingVolsRecup(), false);

                            //setExcelButtonVisibility(false);
                            setCameraButtonVisibility(false);
                            setPlanButtonVisibility(false);
                            setVisibilityRefresh(true);
                            itemsTable.setRowHeight(16);
                            cl.show(cardPanel, "table");
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
                //setExcelButtonVisibility(false);
                setCameraButtonVisibility(false);
                setPlanButtonVisibility(false);
                itemsTable.setRowHeight(16);
                model.setData((ArrayList) DatabaseHelper.getAllCarnetAdresses(), true);
                cl.show(cardPanel, "table");
                break;

            case PLAN:
                setVisibilityRefresh(false);
                //setExcelButtonVisibility(false);
                setCameraButtonVisibility(false);
                setPlanButtonVisibility(true);
                cl.show(cardPanel, "plans");
                break;

            case NONE:
                //setExcelButtonVisibility(false);
                setVisibilityRefresh(false);
                setPlanButtonVisibility(false);
                setCameraButtonVisibility(false);
                cl.show(cardPanel, "tuto");
                break;

            default:
                cl.show(cardPanel, "tuto");
                break;
        }

        model.fireTableStructureChanged();
    }

//    private void setExcelButtonVisibility(boolean bool) {
//
//        if (bool == false) {
//            jLabel1.setVisible(false);
//            monthComboBox.setVisible(false);
//            yearComboBox.setVisible(false);
//
//        } else {
//            jLabel1.setVisible(true);
//            monthComboBox.setVisible(true);
//            yearComboBox.setVisible(true);
//        }
//    }
    private void setCameraButtonVisibility(boolean bool) {

        if (bool == false) {
            cameraButton.setVisible(false);
        } else {
            cameraButton.setVisible(true);
            cameraButton.setOpaque(false);
            cameraButton.setContentAreaFilled(false);
            cameraButton.setBorderPainted(false);
        }
    }

    private void setPlanButtonVisibility(boolean show) {
        parameterButton.setVisible(show);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == cameraButton) {
            if (isCameraActive) {
                //On désactive les caméras 
                cameraInterface(!isCameraActive);
                cameraButton.setIcon(ComponentManager.getInstance().getComponentIconDefaults().getgreenCameraIcon());
                toolbarsListener.changeCameraStatus(isCameraActive);
            } else //On change le label du bouton (de "activer caméra" à "désactiver caméra) et sa couleur
            {
                cameraInterface(!isCameraActive);
                toolbarsListener.changeCameraStatus(isCameraActive);
                cameraButton.setIcon(ComponentManager.getInstance().getComponentIconDefaults().getredCameraIcon());
                //On lance l'activation des caméras une fois qu'on appuie sur le bouton
            }
        } else if (e.getSource() == refreshButton) {

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

//    private void setMonthComboboxItems() {
//        DateFormatSymbols symbols = new DateFormatSymbols();
//        String[] monthNames = symbols.getMonths();
//
//        this.monthComboBox.setModel(new DefaultComboBoxModel(monthNames));
//    }
//
//    private void setYearComboboxItems() {
//        ArrayList<Integer> listYears = DatabaseHelper.getYearsCompletePlusActual();
//        Collections.sort(listYears);
//        Integer[] years = listYears.toArray(new Integer[0]);
//
//        yearComboBox.setModel(new DefaultComboBoxModel(years));
//    }
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
                            cam.setState(Camera.CAMERA_STATE.NORMAL);
                            cam.getFaceDetectorThread().launch(1, cam.getId());

                            System.out.println("##### ID. : " + i);

                        } else if (cam == null) {
                            cam = new Camera(i);
                            cam.setState(Camera.CAMERA_STATE.NORMAL);
                            cam.setFaceDetectorThread(new FaceDetectorThread(faceDetectorListener));
                            cam.getFaceDetectorThread().launch(1, cam.getId());

                            System.out.println("##### ID.. : " + i);
                        } else if (cam.getFaceDetectorThread() == null) {
                            cam.setState(Camera.CAMERA_STATE.NORMAL);
                            cam.setFaceDetectorThread(new FaceDetectorThread(faceDetectorListener));
                            System.out.println("##### ID... : " + i);
                            cam.getFaceDetectorThread().launch(1, cam.getId());
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
                            cam.setState(Camera.CAMERA_STATE.NONE);
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
    public void getCountFaceDetected(int number_of_faces, int percentage_of_differences, int id_camera) {
        System.out.println("List Panel NB FACES : " + number_of_faces);
        System.out.println("List Panel PERCENT : " + percentage_of_differences);
        System.out.println("List Panel ID CAM : " + id_camera);

        Integer seuilCamera = (Integer) ParametersUtils.get(ParametersUtils.PARAM_SUEIL_CAMERA);

        // NOT Alert !
        if (number_of_faces < seuilCamera) {
            if (percentage_of_differences < 10) {
                if (cameras != null && cameras.size() > 0) {
                    Camera camAlert;
                    for (int i = 0; i < cameras.size(); i++) {
                        camAlert = cameras.get(i);

                        if (camAlert.getFaceDetectorThread() != null
                                && camAlert.getFaceDetectorThread().isActive()) {
                            if (camAlert.getId().equals(id_camera)
                                    && camAlert.getState() == Camera.CAMERA_STATE.ALERT
                                    && camAlert.getPosition() != null
                                    && !camAlert.getPosition().equals(" - ")) {
                                camAlert.setState(Camera.CAMERA_STATE.NORMAL);
                                planPanelComponentResized(null);
                                break;
                            }
                        }
                    }
                }
            }

        } // Alert !
        else {
            if (cameras != null && cameras.size() > 0) {
                Camera camAlert;
                for (int i = 0; i < cameras.size(); i++) {
                    camAlert = cameras.get(i);

                    if (camAlert.getFaceDetectorThread() != null
                            && camAlert.getFaceDetectorThread().isActive()) {
                        if (camAlert.getId().equals(id_camera)
                                && camAlert.getState() != Camera.CAMERA_STATE.ALERT
                                && camAlert.getPosition() != null
                                && !camAlert.getPosition().equals(" - ")) {
                            camAlert.setState(Camera.CAMERA_STATE.ALERT);
                            planPanelComponentResized(null);

                            //ParseUtils.sendPush(camAlert.getPosition());
                            JOptionPane.showMessageDialog(null, "Caméra " + id_camera
                                    + " : Détection de formation de file d'attente en position "
                                    + camAlert.getPosition(), "WARNING", JOptionPane.WARNING_MESSAGE);

                            break;
                        }
                    }
                }
            }
        }
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
        cameraButton = new javax.swing.JButton();
        parameterButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        cardPanel = new javax.swing.JPanel();
        scrollPaneTable = new javax.swing.JScrollPane();
        itemsTable = new javax.swing.JTable();
        scrollPanePlans = new javax.swing.JScrollPane();
        scrollPaneTuto = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());

        jSpinnerPanel.setPreferredSize(new java.awt.Dimension(40, 40));

        cameraButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nomoreline/img/greencamera.png"))); // NOI18N
        cameraButton.setPreferredSize(new java.awt.Dimension(35, 35));
        jSpinnerPanel.add(cameraButton);

        parameterButton.setText("jButton1");
        parameterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parameterButtonActionPerformed(evt);
            }
        });
        jSpinnerPanel.add(parameterButton);

        refreshButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nomoreline/img/refresh.png"))); // NOI18N
        refreshButton.setBorderPainted(false);
        refreshButton.setContentAreaFilled(false);
        refreshButton.setMaximumSize(new java.awt.Dimension(35, 35));
        refreshButton.setMinimumSize(new java.awt.Dimension(35, 35));
        refreshButton.setPreferredSize(new java.awt.Dimension(35, 35));
        jSpinnerPanel.add(refreshButton);

        add(jSpinnerPanel, java.awt.BorderLayout.PAGE_START);

        cardPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                cardPanelComponentResized(evt);
            }
        });
        cardPanel.setLayout(new java.awt.CardLayout());

        itemsTable.setModel(new MyTableModel());
        itemsTable.setFillsViewportHeight(true);
        itemsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        itemsTable.setShowHorizontalLines(false);
        itemsTable.setShowVerticalLines(false);
        itemsTable.getTableHeader().setReorderingAllowed(false);
        scrollPaneTable.setViewportView(itemsTable);

        cardPanel.add(scrollPaneTable, "table");
        cardPanel.add(scrollPanePlans, "plans");

        scrollPaneTuto.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneTuto.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        cardPanel.add(scrollPaneTuto, "tuto");

        add(cardPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void parameterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parameterButtonActionPerformed
        if (!isCameraActive) {
            PlanCameraDialog pcd = new PlanCameraDialog(null, true);
            pcd.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez désactiver la détection "
                    + "par caméra afin de pouvoir les paramétrer.", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_parameterButtonActionPerformed

    private void cardPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_cardPanelComponentResized
        tutoImg.repaint();
    }//GEN-LAST:event_cardPanelComponentResized


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cameraButton;
    private javax.swing.JPanel cardPanel;
    private javax.swing.JTable itemsTable;
    private javax.swing.JPanel jSpinnerPanel;
    private javax.swing.JButton parameterButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JScrollPane scrollPanePlans;
    private javax.swing.JScrollPane scrollPaneTable;
    private javax.swing.JScrollPane scrollPaneTuto;
    // End of variables declaration//GEN-END:variables
}
