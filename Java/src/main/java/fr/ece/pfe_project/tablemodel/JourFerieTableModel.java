package fr.ece.pfe_project.tablemodel;

import fr.ece.pfe_project.model.JourFerie;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pierreghazal
 */
public class JourFerieTableModel extends DefaultTableModel {

    private final ArrayList<JourFerie> jours;

    public JourFerieTableModel() {
        this(new ArrayList<JourFerie>());
    }

    public JourFerieTableModel(ArrayList<JourFerie> l) {
        super(new String[]{"N°", "Jours fériés", "Date"}, 0);
        this.jours = new ArrayList<JourFerie>(l);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 0) {
            return false;
        }

        return true;
    }

    @Override
    public int getRowCount() {
        if (jours == null) {
            return 0;
        }

        return jours.size();
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return Date.class;
            default:
                return String.class;
        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        System.out.println("EDITING");

        switch (column) {

            case 0:
                break;
            case 1:
                jours.get(row).setLibelle((String) aValue);
                break;
            case 2:
                jours.get(row).setDate((Date) aValue);
                break;
            default:
                break;
        }

        fireTableCellUpdated(row, column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        JourFerie jf = this.jours.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return rowIndex + 1;
            case 1:
                return jf.getLibelle();
            case 2:
                return jf.getDate();
        }

        return "null";
    }

    public void removeDataAtRow(int row) {
        jours.remove(row);
        this.fireTableDataChanged();
    }

    public void add(JourFerie jf) {
        if (jf != null) {
            jours.add(jf);
            this.fireTableDataChanged();
        }
    }

    public ArrayList<JourFerie> getDatas() {
        return jours;
    }

    public void setData(ArrayList<JourFerie> l, boolean fireTableDataChanged) {
        this.jours.clear();
        if (l != null) {
            this.jours.addAll(l);
        }

        if (fireTableDataChanged) {
            fireTableDataChanged();
        }
    }
}
