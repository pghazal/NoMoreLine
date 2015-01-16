package fr.ece.pfe_project.renderer;

import fr.ece.pfe_project.model.Camera;
import fr.ece.pfe_project.widget.CameraCellComponent;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author pierreghazal
 */
public class CameraCellRenderer implements TableCellRenderer {

    CameraCellComponent panel;

    public CameraCellRenderer() {
        panel = new CameraCellComponent();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Camera camera = (Camera) value;
        panel.updateData(camera, isSelected, table);
        panel.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
        return panel;
    }
}
