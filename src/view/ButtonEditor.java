package view;

import dao.ProdutoDAO;
import model.Produto;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private int produtoId;
    private JTable table;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton("✏️ Editar");
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(90, 71, 171));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(70, 55, 140), 1, true));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    produtoId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
                    Produto produto = ProdutoDAO.buscarPorId(produtoId);
                    if (produto != null) {
                        new AtualizarProdutoFrame(produto);
                    }
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return "✏️ Editar";
    }
}
