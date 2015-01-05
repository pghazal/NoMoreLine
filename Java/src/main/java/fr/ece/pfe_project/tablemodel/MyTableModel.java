package fr.ece.pfe_project.tablemodel;

import javax.swing.table.AbstractTableModel;
import fr.ece.pfe_project.panel.ToolbarEntityPanel;
import fr.ece.pfe_project.model.Camera;
import fr.ece.pfe_project.model.Comptoir;
import fr.ece.pfe_project.model.Employee;
import fr.ece.pfe_project.model.ExcelRow;
import fr.ece.pfe_project.model.ModelInterface;
import java.util.Date;

/**
 *
 * @author Pierre Ghazal
 */
public class MyTableModel extends AbstractTableModel {

    private final String[] columnNone = {};
    private final String[] columnComptoir = {"ID", "Numéro"};
    private final String[] columnCamera = {"ID", "Numéro"};
    private final String[] columnExcel = {"Date", "Flux de la journée"};

    private final String[][] columnNames = {
        columnNone, columnComptoir, columnCamera, columnExcel
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

            case COMPTOIR:
                return columnNames[ToolbarEntityPanel.ENTITY.COMPTOIR.ordinal()].length;

            case EXCELROW:
                return columnNames[ToolbarEntityPanel.ENTITY.EXCELROW.ordinal()].length;

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

            case COMPTOIR:
                return columnNames[ToolbarEntityPanel.ENTITY.COMPTOIR.ordinal()][column];

            case EXCELROW:
                return columnNames[ToolbarEntityPanel.ENTITY.EXCELROW.ordinal()][column];

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
                        return ((Camera) myData).getId();
                    case 1:
                        return ((Camera) myData).getNumber();
                    default:
                        System.err.println("Logic Error");
                        break;
                }
                break;

            case COMPTOIR:

                myData = (Comptoir) data[row];

                switch (column) {
                    case 0:
                        return ((Comptoir) myData).getId();
                    case 1:
                        return ((Comptoir) myData).getNumber();
                    default:
                        System.err.println("Logic Error");
                        break;
                }
                break;


            case EXCELROW:

                myData = (ExcelRow) data[row];

                switch (column) {
                    case 0:
                        return ((ExcelRow) myData).getDate();
                    case 1:
                        return ((ExcelRow) myData).getValue();
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
                        return Long.class;
                    case 1:
                        return Integer.class;
                }
                break;

            case COMPTOIR:

                switch (column) {
                    case 0:
                        return Long.class;
                    case 1:
                        return Integer.class;
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
