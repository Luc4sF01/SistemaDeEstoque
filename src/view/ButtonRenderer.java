package view;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);
        setText("✏️ Editar");
        setFont(new Font("SansSerif", Font.BOLD, 14));
        setBackground(new Color(90, 71, 171));
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(70, 55, 140), 1, true));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}
