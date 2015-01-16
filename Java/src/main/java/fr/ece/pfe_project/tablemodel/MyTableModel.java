package fr.ece.pfe_project.tablemodel;

import javax.swing.table.AbstractTableModel;
import fr.ece.pfe_project.panel.ToolbarEntityPanel;
import fr.ece.pfe_project.model.Camera;
import fr.ece.pfe_project.model.CarnetAdresses;
import fr.ece.pfe_project.model.FrequentationJournaliere;
import fr.ece.pfe_project.model.ModelInterface;
import fr.ece.pfe_project.model.ListingVols;
import java.util.Date;

/**
 *
 * @author Pierre Ghazal
 */
public class MyTableModel extends AbstractTableModel {

    private final String[] columnNone = {};
    private final String[] columnCamera = {"Cameras"};
    private final String[] columnExcel = {"Date", "Flux de la journée"};
    private final String[] columnListingVols = {"Date", "Heure de départ", "Destination", "N° de vol", "Compagnie", "Observation"};
    private final String[] columnCarnetAdresses = {"Compagnie", "Nombre de guichets", "Societe d'assistance", "Téléphone"};

    private final String[][] columnNames = {
        columnNone, columnCamera, columnExcel, columnListingVols, columnCarnetAdresses
    };

    private ModelInterface[] data;
    private ToolbarEntityPanel.ENTITY typeEntity;

    public MyTableModel() {
        this(new ModelInterface[0]);
    }

    public MyTableModel(ModelInterface[] data) {
        typeEntity = ToolbarEntityPanel.ENTITY.NONE;
        this.data = data;
    }

    @Override
    public int getColumnCount() {
        switch (typeEntity) {
            case NONE:
                break;

            case CAMERA:
                return columnNames[ToolbarEntityPanel.ENTITY.CAMERA.ordinal()].length;

            case EXCELROW:
                return columnNames[ToolbarEntityPanel.ENTITY.EXCELROW.ordinal()].length;
                
            case LISTINGVOLS:
            return columnNames[ToolbarEntityPanel.ENTITY.LISTINGVOLS.ordinal()].length;
                
            case CARNETADRESSE:
            return columnNames[ToolbarEntityPanel.ENTITY.CARNETADRESSE.ordinal()].length;

            default:
                break;
        }

        return 0;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public String getColumnName(int column) {
        switch (typeEntity) {
            case NONE:
                break;

            case CAMERA:
                return columnNames[ToolbarEntityPanel.ENTITY.CAMERA.ordinal()][column];

            case EXCELROW:
                return columnNames[ToolbarEntityPanel.ENTITY.EXCELROW.ordinal()][column];
                
            case LISTINGVOLS:
                return columnNames[ToolbarEntityPanel.ENTITY.LISTINGVOLS.ordinal()][column];
                
            case CARNETADRESSE:
                return columnNames[ToolbarEntityPanel.ENTITY.CARNETADRESSE.ordinal()][column];

            default:
                break;
        }

        return "null";
    }

    @Override
    public Object getValueAt(int row, int column) {
        ModelInterface myData;

        switch (typeEntity) {
            case NONE:
                break;

            case CAMERA:

                myData = (Camera) data[row];

                switch (column) {
                    case 0:
                        return (Camera) myData;
                    default:
                        System.err.println("Logic Error");
                        break;
                }
                break;
                
            case LISTINGVOLS:

                myData = (ListingVols) data[row];

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

            case EXCELROW:

                myData = (FrequentationJournaliere) data[row];

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
                
            case CARNETADRESSE:

                myData = (CarnetAdresses) data[row];

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

    public ModelInterface getDataAtRow(int row) {
        return data[row];
    }

    public void setData(ModelInterface[] data, boolean fireTableDataChanged) {
        this.data = data;

        if (fireTableDataChanged) {
            fireTableDataChanged();
        }
    }

    public void setEntity(ToolbarEntityPanel.ENTITY entity) {
        this.typeEntity = entity;
    }
}
