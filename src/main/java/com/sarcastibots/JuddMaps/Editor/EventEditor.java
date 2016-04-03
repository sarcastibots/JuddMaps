package com.sarcastibots.JuddMaps.Editor;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

import com.sarcastibots.JuddMaps.Map.Event;

@SuppressWarnings("serial")
public class EventEditor 
extends JFrame {
    private static final Insets INSETS = new Insets(5, 5, 5, 5);
    private static int NUM_COLS = 2;
    
    Event event;
    
    JLabel id;
    JLabel location;
    JLabel width;
    JLabel height;
    
    JTextArea conditions;
    JTextArea results;
    
    public EventEditor( ) {
	this.setTitle("Event Editor");
	this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	this.setPreferredSize( new Dimension(250, 400) );
	
	initComponents( this.getContentPane() );
    }

    private void initComponents( Container pane) {
	pane.setLayout( new GridBagLayout() );
	
	int row = 0;
	addHeaderPanel(row, pane);
	
//	addIDRow(row, pane);
//	
//	row++;
//	addLocationRow(row, pane);
//	
//	row++;
//	addWidthRow(row, pane);
//	
//	row++;
//	addHeightRow(row, pane);
	
	row++;
	addConditionsTextArea(row, pane);
	
	row++;
	addResultsTextArea(row, pane);
	
	row++;
	addCloseBtn(row, pane);
	
	pack();
	
    }
    
    private void addHeaderPanel(int row, Container pane) {
	JPanel headerPanel = new JPanel();
	headerPanel.setLayout( new GridLayout(0, 2) );
	
	addIDRow(headerPanel);
	
	addLocationRow(headerPanel);
	
	addWidthRow(headerPanel);
	
	addHeightRow(headerPanel);
	
	pane.add(headerPanel, this.makeConstraints(row, 0, 2));
    }

    private void addIDRow(Container panel) {
	JLabel idLabel = new JLabel("ID");

	panel.add(idLabel);
	
	id = new JLabel( );
	id.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED) );
	panel.add(id);
    }

    private void addLocationRow(Container panel) {
	JLabel locationLabel = new JLabel("Base Point");
	panel.add(locationLabel);
	
	location = new JLabel(  );
	location.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED) );
	panel.add(location);
    }
    
    private void addWidthRow(Container panel) {
	
	JLabel widthLabel = new JLabel("Width");
	panel.add(widthLabel);
	
	width = new JLabel(  );
	width.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED) );
	panel.add(width);
	
    }
    
    private void addHeightRow(Container panel) {
	
	JLabel heightLabel = new JLabel("Height");
	panel.add(heightLabel);
	
	height = new JLabel( );
	height.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED) );
	panel.add(height);
    }

    private void addConditionsTextArea(int row, Container pane) {
	conditions = new JTextArea();
	JScrollPane conditionsScroll = new JScrollPane( conditions );
	conditionsScroll.setPreferredSize(new Dimension(200, 100));
	conditionsScroll.setBorder( BorderFactory.createTitledBorder("Conditions"));
	
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.BOTH;
	gbc.insets = INSETS;
	gbc.gridx = 0;
	gbc.gridy = row;
	gbc.gridwidth = NUM_COLS;
	gbc.anchor = GridBagConstraints.EAST;
	pane.add(conditionsScroll, gbc);
    }

    private void addResultsTextArea(int row, Container pane) {
	results = new JTextArea();
	JScrollPane resultsScroll = new JScrollPane( results );
	resultsScroll.setPreferredSize(new Dimension(200, 100));
	resultsScroll.setBorder( BorderFactory.createTitledBorder("Results"));
	
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.insets = INSETS;
	gbc.fill = GridBagConstraints.BOTH;
	gbc.gridx = 0;
	gbc.gridy = row;
	gbc.gridwidth = NUM_COLS;
	gbc.anchor = GridBagConstraints.EAST;
	pane.add(resultsScroll, gbc);
    }
    
    private GridBagConstraints makeConstraints(int row, int col, int width) {
	GridBagConstraints gbc = new GridBagConstraints();
	
	gbc.insets = INSETS;
	gbc.gridx = col;
	gbc.gridy = row;
	gbc.gridwidth = width;
	
	if ( width > 1) {
	    gbc.fill = GridBagConstraints.BOTH;
	} else {
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	}
	
	
	if ( col % 2 == 0 ){
	    gbc.anchor = GridBagConstraints.WEST;
	} else {
	    gbc.anchor = GridBagConstraints.EAST;
	}
	
	return gbc;
    }
    
    private void addCloseBtn(int row, Container pane) {
	JButton closeBtn = new JButton("Close");
	closeBtn.addActionListener( new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		setVisible(false);
	    }
	    
	});
	
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.insets = INSETS;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.gridx = 0;
	gbc.gridy = row;
	gbc.gridwidth = NUM_COLS;
	gbc.anchor = GridBagConstraints.EAST;
	pane.add(closeBtn, gbc);
    }

    public void setEvent(Event e) {
	if ( this.event != null ) {
	    saveEvent();
	}
	this.event = e;
	
	loadEvent();
    }

    private void loadEvent() {
	id.setText(String.valueOf(event.getID()));
	
	Rectangle locationRect = event.getLocation();
	location.setText("(" + String.valueOf(locationRect.x) + ", " + String.valueOf(locationRect.y) + ")");
	width.setText(String.valueOf(locationRect.width + 1));
	height.setText( String.valueOf(locationRect.height + 1) );
	
	String condText = "";
	for ( String con: event.getConditions() ) {
	    condText += con + "\n";
	}
	conditions.setText(condText);
	
	String resultsText = "";
	for ( String result: event.getResults() ) {
	    resultsText += result + "\n";
	}
	results.setText(resultsText);
    }

    private void saveEvent() {
	StringTokenizer condToken = new StringTokenizer( conditions.getText(), "\n");
	List<String> conditionsList = event.getConditions();
	conditionsList.clear();
	while ( condToken.hasMoreTokens() ) {
	    conditionsList.add(condToken.nextToken().trim());
	}
	StringTokenizer resultsToken = new StringTokenizer( results.getText(), "\n");
	List<String> resultsList = event.getResults();
	resultsList.clear();
	while ( resultsToken.hasMoreTokens() ) {
	    resultsList.add(resultsToken.nextToken().trim());
	}
    }
}
