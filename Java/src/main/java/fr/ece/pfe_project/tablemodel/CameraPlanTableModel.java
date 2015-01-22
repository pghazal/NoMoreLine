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
        super(new String[]{"Caméra", "Position"}, 0);
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
        return String.class;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {

        switch (column) {

            case 0:
                break;
            case 1:
                cameras.get(row).setPosition((String) aValue);
                break;
            default:
                break;
        }

        fireTableCellUpdated(row, column);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Camera c = this.cameras.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return "Caméra #" + c.getId();
            case 1:
                if (c.getPosition() == null) {
                    return " - ";
                } else {
                    return c.getPosition();
                }
        }

        return null;
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
