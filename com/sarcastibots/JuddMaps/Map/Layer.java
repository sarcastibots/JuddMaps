package com.sarcastibots.JuddMaps.Map;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Layer {

    public enum LayerType {
	GROUND,
	SPRITE
    }
//    public static final int GROUND = 0;
//    public static final int SPRITE = 1;

    String name;
    /** TODO instead of creating a pile of Tile objects we'll use a flyweight class so 
     * Layer only needs an integer array and a reference to the flyweight
     * since we need to resize dynamically 'List' is the most appropriate data structure.*/
    //List<Integer> grid;
    // each inner list represents a column
    List<List<Tile>> grid;
    private int gridWidth, gridHeight;

    private LayerType layerType;
    private boolean visible;

    public Layer(int width, int height, LayerType type, String name) {
	this.gridWidth = width;
	this.gridHeight = height;
	this.name = name;
	this.setLayerType(type);

	// provide starting size argument to reduce resizes
	grid = new ArrayList<>(gridWidth);
	for ( int i = 0; i < gridWidth; i++ ) {
	    grid.add( newColumn() );
	}
	
	visible = true;
    }

    private List<Tile> newColumn() {
	List<Tile> column = new ArrayList<>(gridHeight);
	// this is necessary to avoid an index out of bounds exception 
	for ( int j = 0; j < gridHeight; j++) {
	    column.add(new Tile() );
	}
	return column;
    }
    
    /**
     * 
     * @param g
     * @param origin
     * @param size
     * @param layer
     */
    public void render(Graphics g, Point origin, Dimension size, int zoomWidth, int zoomHeight) {
	if (visible) {
	    double minX = Math.max(origin.getX()/zoomWidth, 0);
	    double maxX = Math.min((origin.getX()+size.getWidth())/zoomWidth, gridWidth);

	    double minY = Math.max(origin.getY()/zoomHeight, 0);
	    double maxY = Math.min((origin.getY()+size.getHeight())/zoomHeight, gridHeight);
	    for(int y=(int)minY; y<maxY; y++) {
		for(int x=(int)minX; x<maxX;  x++) {
		    Tile tile = getTile(x, y);
		    if(tile != null) {
			tile.render(g, x*zoomWidth+zoomWidth, y*zoomHeight+zoomHeight);
		    }
		}
	    }
	}
    }

    /**
     * This method is called {@link}Map.resize use that method instead
     **/
    protected void resize(int newWidth, int newHeight)
    {	
	if ( newHeight > gridHeight ) {
	    addRows( newHeight );
	} else if (newHeight < gridHeight ) {
	    removeRows( newHeight );
	}
	if ( newWidth > gridWidth ) {
	    addColumns( newWidth );
	} else if ( newWidth < gridWidth ) {
	    removeColumns( newWidth);
	}
    }

    private void removeColumns(int newWidth) {
	while ( gridWidth != newWidth ) {
	    // decrement first to avoid out of bounds exception
	    gridWidth--;
	    grid.remove(gridWidth);
	}
    }

    private void addColumns(int newWidth) {
	for ( int i = gridWidth; i <= newWidth; i++) {
	    grid.add( newColumn() );
	}
	gridWidth = newWidth;
    }

    private void removeRows(int newHeight) {
	while ( gridHeight != newHeight ) {
	    // decrement first to avoid out of bounds exception
	    gridHeight--;
	    for ( List<Tile> column: grid ) {
		column.remove(gridHeight);
	    }
	}
    }

    private void addRows(int newHeight) {
	for ( int i = gridHeight; i <= newHeight; i++ ) {
	    for ( List<Tile> column: grid ) {
		column.add( new Tile() );
	    }
	}
	gridHeight = newHeight;
    }

    public void setTile(int x, int y, Tile t) {
	grid.get(x).set(y, t);
    }

    public Tile getTile(int x, int y) {
	return grid.get(x).get(y);
    }

    /**
     * executes a non wrapping shift
     * @param offX
     * @param offY
     */
    public void shift(int offX, int offY) {

	for ( List<Tile> column: grid) {
	    shiftColumn(column, offY);
	}

	shiftRows(offX);
    }

    private void shiftRows(int offX) {
	// if offset is positive we want to add that many new fields to the beginning of the list and remove them from the end
	int insertLocation;
	int removeLocation;
	if ( offX > 0) {
	    insertLocation = 0;
	    removeLocation = gridWidth;
	} else {
	    insertLocation = gridWidth;
	    removeLocation = 0;
	}
	int shiftMag = Math.abs(offX);
	for ( int i = 0; i < shiftMag; i++ ) {
	    grid.add( insertLocation, newColumn() );
	    grid.remove(removeLocation);
	}
    }

    /**
     * non-wrapping column shift
     * @param offY
     */
    private void shiftColumn(List<Tile> column, int offY) {
	// if offset is positive we want to add that many new fields to the beginning of the list and remove them from the end
	int insertLocation;
	int removeLocation;
	if ( offY > 0) {
	    insertLocation = 0;
	    removeLocation = gridHeight;
	} else {
	    insertLocation = gridHeight;
	    removeLocation = 0;
	}
	int shiftMag = Math.abs(offY);
	for ( int i = 0; i < shiftMag; i++ ) {
	    column.add(insertLocation, new Tile());
	    column.remove(removeLocation);
	}
    }

    public void clear() {
	for(int x = 0; x < gridWidth; x++) {
	    for(int y = 0; y < gridHeight; y++) {
		this.setTile(x, y, null );
	    }
	}
    }

    public LayerType getLayerType() {
	return layerType;
    }

    public void setLayerType(LayerType layerType) {
	this.layerType = layerType;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String usName) {
	String sName = usName.replace(" ", "_");
        this.name = sName;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
