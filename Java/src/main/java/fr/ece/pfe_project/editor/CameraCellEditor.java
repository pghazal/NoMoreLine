package fr.ece.pfe_project.editor;

import fr.ece.pfe_project.model.Camera;
import fr.ece.pfe_project.widget.CameraCellComponent;
import java.awt.Component;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author pierreghazal
 */
public class CameraCellEditor extends AbstractCellEditor implements TableCellEditor {

    CameraCellComponent panel;

    public CameraCellEditor() {
        panel = new CameraCellComponent();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Camera camera = (Camera) value;
        panel.updateData(camera, true, table);
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return false;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        return true;
    }

    @Override
    public void cancelCellEditing() {
        
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        
    }
}
