package com.sarcastibots.JuddMaps.Editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class LayerPropertiesFrame 
extends JFrame 
implements ActionListener {

    /** generated serial id */
    private static final long serialVersionUID = 7582572712770482342L;
    
    LayerPropertiesTableModel tableModel;
    JTable propertiesTabel;
    JButton closeBtn;
    Map map;
    
    public LayerPropertiesFrame() {
	this.setDefaultCloseOperation(HIDE_ON_CLOSE);
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
        propertiesTabel = new JTable();
        propertiesTabel.setModel(tableModel);
        propertiesTabel.getColumn("Move Up").setCellRenderer(new ButtonRenderer());
        propertiesTabel.getColumn("Move Down").setCellRenderer(new ButtonRenderer());
        
        JScrollPane propsScrollPane = new JScrollPane();
        propsScrollPane.setViewportView(propertiesTabel);
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
	}
    }

    public void setMap ( Map m ) {
	this.map = m;
	updateTableModel();
    }

    private void updateTableModel() {
	for ( Layer l: map.layers ) {
	    tableModel.addRow(l);
	}
    }
}
