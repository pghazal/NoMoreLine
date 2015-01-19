package fr.ece.pfe_project.tablemodel;

import fr.ece.pfe_project.interfaces.ToolbarEntityListener.ENTITY;
import static fr.ece.pfe_project.interfaces.ToolbarEntityListener.ENTITY.CAMERA;
import static fr.ece.pfe_project.interfaces.ToolbarEntityListener.ENTITY.CARNETADRESSE;
import static fr.ece.pfe_project.interfaces.ToolbarEntityListener.ENTITY.EXCELROW;
import static fr.ece.pfe_project.interfaces.ToolbarEntityListener.ENTITY.LISTINGVOLS;
import static fr.ece.pfe_project.interfaces.ToolbarEntityListener.ENTITY.NONE;
import javax.swing.table.AbstractTableModel;
import fr.ece.pfe_project.panel.ToolbarEntityPanel;
import fr.ece.pfe_project.model.Camera;
import fr.ece.pfe_project.model.CarnetAdresses;
import fr.ece.pfe_project.model.FrequentationJournaliere;
import fr.ece.pfe_project.model.ListingVols;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Pierre Ghazal
 */
public class MyTableModel<T> extends AbstractTableModel {

    private final String[] columnNone = {};
    private final String[] columnCamera = {"Cameras"};
    private final String[] columnExcel = {"Date", "Fréquentation"};
    private final String[] columnListingVols = {"Date", "Heure de départ", "Destination", "N° de vol", "Compagnie", "Observation"};
    private final String[] columnCarnetAdresses = {"Compagnie", "Nombre de guichets", "Societe d'assistance", "Téléphone"};

    private final String[][] columnNames = {
        columnNone, columnCamera, columnExcel, columnListingVols, columnCarnetAdresses
    };

    private ArrayList<T> data;
    private ENTITY typeEntity;

    public MyTableModel() {
        this(new ArrayList<T>());
    }

    public MyTableModel(ArrayList<T> data) {
        typeEntity = ENTITY.NONE;
        this.data = data;
    }

    @Override
    public int getColumnCount() {
        switch (typeEntity) {
            case NONE:
                break;

            case CAMERA:
                return columnNames[ENTITY.CAMERA.ordinal()].length;

            case EXCELROW:
                return columnNames[ENTITY.EXCELROW.ordinal()].length;

            case LISTINGVOLS:
                return columnNames[ENTITY.LISTINGVOLS.ordinal()].length;

            case CARNETADRESSE:
                return columnNames[ENTITY.CARNETADRESSE.ordinal()].length;

            default:
                break;
        }

        return 0;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public String getColumnName(int column) {
        switch (typeEntity) {
            case NONE:
                break;

            case CAMERA:
                return columnNames[ENTITY.CAMERA.ordinal()][column];

            case EXCELROW:
                return columnNames[ENTITY.EXCELROW.ordinal()][column];

            case LISTINGVOLS:
                return columnNames[ENTITY.LISTINGVOLS.ordinal()][column];

            case CARNETADRESSE:
                return columnNames[ENTITY.CARNETADRESSE.ordinal()][column];

            default:
                break;
        }

        return "null";
    }

    @Override
    public Object getValueAt(int row, int column) {
        T myData;

        switch (typeEntity) {
            case NONE:
                break;

            case CAMERA:

                myData = data.get(row);

                switch (column) {
                    case 0:
                        return myData;
                    default:
                        System.err.println("Logic Error");
                        break;
                }
                break;

            case EXCELROW:

                myData = data.get(row);

                switch (column) {
                    case 0:
                        return ((FrequentationJournaliere) myData).getDate();
                    case 1:
                        return ((FrequentationJournaliere) myData).getFrequentation();
                    default:
                        System.err.println("Logic Error");
                        break;
                }
                break;

            case LISTINGVOLS:

                myData = data.get(row);

                switch (column) {
                    case 0:
                        return ((ListingVols) myData).getDate1();
                    case 1:
                        return ((ListingVols) myData).getHeure();
                    case 2:
                        return ((ListingVols) myData).getDestination();
                    case 3:
                        return ((ListingVols) myData).getNumeroVol();
                    case 4:
                        return ((ListingVols) myData).getCompagnie();
                    case 5:
                        return ((ListingVols) myData).getObservation();
                    default:
                        System.err.println("Logic Error");
                        break;
                }
                break;

            case CARNETADRESSE:

                myData = data.get(row);

                switch (column) {
                    case 0:
                        return ((CarnetAdresses) myData).getCompagnieca();
                    case 1:
                        return ((CarnetAdresses) myData).getNombreGuichet();
                    case 2:
                        return ((CarnetAdresses) myData).getSocieteAssistance();
                    case 3:
                        return ((CarnetAdresses) myData).getTelephone();
                    default:
                        System.err.println("Logic Error");
                        break;
                }
                break;

            default:
                break;
        }

        return "null";
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (typeEntity) {
            case NONE:
                break;

            case CAMERA:
                switch (column) {
                    case 0:
                        return Camera.class;
                }
                break;

            case EXCELROW:

                switch (column) {
                    case 0:
                        return Date.class;
                    case 1:
                        return Integer.class;
                }
                break;

            case LISTINGVOLS:

                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                    case 2:
                        return String.class;
                    case 3:
                        return String.class;
                    case 4:
                        return String.class;
                    case 5:
                        return String.class;
                }
                break;

            case CARNETADRESSE:

                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return Integer.class;
                    case 2:
                        return String.class;
                    case 3:
                        return String.class;
                }
                break;

            default:
                break;
        }

        return String.class;
    }

    public T getDataAtRow(int row) {
        return data.get(row);
    }

    public void setData(ArrayList<T> data, boolean fireTableDataChanged) {
        this.data.clear();
        this.data.addAll(data);

        if (fireTableDataChanged) {
            fireTableDataChanged();
        }
    }

    public void setEntity(ENTITY entity) {
        this.typeEntity = entity;
    }
}
