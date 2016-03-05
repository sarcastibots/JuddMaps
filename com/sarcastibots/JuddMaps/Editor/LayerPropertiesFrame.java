package com.sarcastibots.JuddMaps.Editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.sarcastibots.JuddMaps.Editor.Layer.LayerType;

public class LayerPropertiesFrame 
extends JFrame 
implements ActionListener, TableModelListener {

    /** generated serial id */
    private static final long serialVersionUID = 7582572712770482342L;
    
    LayerPropertiesTableModel tableModel;
    JTable propertiesTable;
    JButton closeBtn;
    Map map;
    MapEdit mapEdit;
    
    public LayerPropertiesFrame( MapEdit edit) {
	this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	this.mapEdit = edit;
	initComonents();
    }

    private void initComonents() {
	this.setTitle("Map Layer Properties");
	this.setLayout( new GridBagLayout() );
	
	addLayerPropertiesTable();
	
	addCloseBtn();
	
	pack();
    }

    private void addLayerPropertiesTable() {
	// properties table columns name(string), visible(checkbox), type(combo), move up(button), move down(button
	
	tableModel = new LayerPropertiesTableModel();
	tableModel.addTableModelListener(this);
        propertiesTable = new JTable( tableModel );
        
        // set up column editors
        TableColumn layerTypeColumn = propertiesTable.getColumnModel().getColumn(2);
        JComboBox<LayerType> comboBox = new JComboBox<>(LayerType.values());
        layerTypeColumn.setCellEditor(new DefaultCellEditor(comboBox));
        
        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        layerTypeColumn.setCellRenderer(renderer);
        
        propertiesTable.getColumn("Move Up").setCellRenderer(new ButtonRenderer());
        propertiesTable.getColumn("Move Up").setCellEditor(
                new ButtonEditor(new JCheckBox()));
        
        propertiesTable.getColumn("Move Down").setCellRenderer(new ButtonRenderer());
        propertiesTable.getColumn("Move Down").setCellEditor(
                new ButtonEditor(new JCheckBox()));
        
        JScrollPane propsScrollPane = new JScrollPane();
        propsScrollPane.setViewportView(propertiesTable);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.gridwidth = 2;
	gbc.weightx = 0.5;
	gbc.weighty = 0.5;
	this.add(propsScrollPane, gbc);
    }
    
    private void addCloseBtn() {
	closeBtn = new JButton("Close");
	closeBtn.addActionListener( this );
	
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.gridx = 1;
	gbc.gridy = 1;
	gbc.gridwidth = 1;
	gbc.anchor = GridBagConstraints.EAST;
	this.add(closeBtn, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if ( e.getSource() == closeBtn) {
	    this.setVisible(false);
	} else if ( e.getSource() == propertiesTable ) {
	    
	}
    }

    public void setMap ( Map m ) {
	this.map = m;
	updateLayers();
    }

    public void updateLayers() {
	tableModel.clear();
	// to avoid ordering confusion it's best to start at the end and work our way down
	// this way lower indexed layers are displayed graphically lower on the table.
	for ( int i = map.layers.size() - 1; i >= 0; i-- ) {
	    tableModel.addRow( map.layers.get(i) );
	}
	
	this.propertiesTable.revalidate();
	this.propertiesTable.repaint();
    }

    @Override
    public void tableChanged(TableModelEvent e) {
	int row = e.getFirstRow();
        int column = e.getColumn();
        int mapLayer = map.getLayerCount() - row - 1; //we reversed things earlier so now all the indices are backwards fun
        Object data = tableModel.getValueAt(row, column);
        switch ( column ) {
        case 0: 
            map.layers.get(mapLayer).setName((String)data);
            break;
        case 1:
            map.layers.get(mapLayer).setVisible((boolean)data);
            break;
        case 2:
            map.layers.get(mapLayer).setLayerType((LayerType)data);
            break;
        case 3:
            map.moveLayer(mapLayer, mapLayer+1);
            this.updateLayers();
            mapEdit.updateLayerComboItems();
            break;
        case 4:
            map.moveLayer(mapLayer, mapLayer-1);
            this.updateLayers();
            mapEdit.updateLayerComboItems();
            break;
        }
    }
}
