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

    final static Color ORANGE_CUSTOM = new Color(235, 206, 157);
    CameraCellComponent panel;

    public CameraCellRenderer() {
        panel = new CameraCellComponent();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Camera camera = (Camera) value;
        panel.setBackground(row % 2 == 0 ? ORANGE_CUSTOM : Color.WHITE);
        panel.updateData(camera, isSelected, table);
        return panel;
    }
}
