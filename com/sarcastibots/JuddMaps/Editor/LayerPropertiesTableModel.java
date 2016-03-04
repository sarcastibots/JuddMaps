package com.sarcastibots.JuddMaps.Editor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class LayerPropertiesTableModel extends AbstractTableModel {
    
    private String[] columnNames = {
		"Layer Name", "Visible?", "Type", "Move Up", "Move Down"
    };
    
    private List<Object[]> data;

    public LayerPropertiesTableModel() {
	data = new ArrayList<>();
    }

    @Override
    public Class<?> getColumnClass(int c) {
	return getValueAt(0, c).getClass();
    }

    @Override
    public int getColumnCount() {
	return this.columnNames.length;
    }

    @Override
    public String getColumnName(int arg0) {
	return this.columnNames[arg0];
    }

    @Override
    public int getRowCount() {
	return data.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
	return data.get(row)[col];
    }

    @Override
    public boolean isCellEditable(int arg0, int arg1) {
	return true;
    }

    @Override
    public void setValueAt(Object newValue, int row, int col) {
	data.get(row)[col] = newValue;
    }

    public void addRow( Layer layer ) {
	Object[] layerData = new Object[this.columnNames.length];
	layerData[0] = layer.getName();
	layerData[1] = layer.isVisible();
	layerData[2] = layer.getLayerType();
	layerData[3] = "Up";
	layerData[4] = "Down";
	this.data.add(layerData);
    }
}
