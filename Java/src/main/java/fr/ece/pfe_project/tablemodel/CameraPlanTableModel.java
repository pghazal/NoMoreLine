package fr.ece.pfe_project.tablemodel;

import fr.ece.pfe_project.model.Camera;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pierreghazal
 */
public class CameraPlanTableModel extends DefaultTableModel {

    private final ArrayList<Camera> cameras;

    public CameraPlanTableModel() {
        this(new ArrayList<Camera>());
    }

    public CameraPlanTableModel(ArrayList<Camera> l) {
        super();
        this.cameras = new ArrayList<Camera>(l);
    }

    @Override
    public int getRowCount() {
        if (cameras == null) {
            return 0;
        }

        return cameras.size();
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return String.class;
            case 1:
                
            default:
                return String.class;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Camera c = this.cameras.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return "Caméra #" + c.getId();
            case 1:
                break;
        }

        return "null";
    }

    public ArrayList<Camera> getDatas() {
        return cameras;
    }

    public void setData(ArrayList<Camera> l, boolean fireTableDataChanged) {
        this.cameras.clear();
        if (l != null) {
            this.cameras.addAll(l);
        }

        if (fireTableDataChanged) {
            fireTableDataChanged();
        }
    }
}
