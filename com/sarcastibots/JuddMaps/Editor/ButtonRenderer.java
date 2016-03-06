package com.sarcastibots.JuddMaps.Editor;

//Courtesy of http://www.crionics.com/products/opensource/faq/swing_ex/SwingExamples.html

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
	setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
	    boolean isSelected, boolean hasFocus, int row, int column) {
	if (isSelected) {
	    setForeground(table.getSelectionForeground());
	    setBackground(table.getSelectionBackground());
	} else {
	    setForeground(table.getForeground());
	    setBackground(UIManager.getColor("Button.background"));
	}
	setText((value == null) ? "" : value.toString());
	return this;
    }
}

@SuppressWarnings("serial")
class ButtonEditor extends DefaultCellEditor {
    protected JButton button;

    private String label;

    private boolean isPushed;

    public ButtonEditor(JCheckBox checkBox) {
	super(checkBox);
	button = new JButton();
	button.setOpaque(true);
	button.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		fireEditingStopped();
	    }
	});
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
	    boolean isSelected, int row, int column) {
	if (isSelected) {
	    button.setForeground(table.getSelectionForeground());
	    button.setBackground(table.getSelectionBackground());
	} else {
	    button.setForeground(table.getForeground());
	    button.setBackground(table.getBackground());
	}
	label = (value == null) ? "" : value.toString();
	button.setText(label);
	isPushed = true;
	return button;
    }

    public Object getCellEditorValue() {
	if (isPushed) {
	    // 
	    // 
	    //JOptionPane.showMessageDialog(button, label + ": Ouch!");
	    // System.out.println(label + ": Ouch!");
	}
	isPushed = false;
	return new String(label);
    }

    public boolean stopCellEditing() {
	isPushed = false;
	return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
	super.fireEditingStopped();
    }
}


