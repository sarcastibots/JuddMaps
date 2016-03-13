package com.sarcastibots.JuddMaps.Map;

import java.awt.Graphics;
import java.awt.*;

import java.util.List;

import com.sarcastibots.JuddMaps.Editor.Camera;
import com.sarcastibots.JuddMaps.Map.Layer.LayerType;

import java.util.*;


/**
 * A Map is a three layered tile map.
 * Layer 1 is the base layer. If a tile in this layer is null, it is
 * replaced by the base tile (the first tile in the tile set).<p>
 * Layer 2 is a special layer in which tiles are rendered in front of or behind
 * any sprites, depending on its location.<p>
 * Layer 3 is rendered last, over the top of everything else.<p>
 *
 * Tiles do not have to be the same size. A standard tile is 32 by 32
 * pixels. If a tile is larger, it will be rendered in place, with the origin
 * at the bottom-right corner of the image. This means a large tile will
 * extend above and to the left over the top of other tiles behind it.<p>
 *
 * the map is only responsible for drawing unchanging tilesets. All interactive
 * stuff usually happens in the "Scene", using sprites, although the map does
 * support changing tiles on the fly if for example a switch were to move a wall.
 *
 */

public class Map
{
    /* change the below two numbers if you want the grid size to be different.
     * individual tile images may be any size. */
    int tileWidth = 32;
    int tileHeight = 32;

    int zoomWidth = 32;
    int zoomHeight = 32;

    int viewWidth = 400;
    int viewHeight = 400;

    GraphicsBank gfx;

    List<MapChangeListener> changeListeners;

//    final static int LAYERS = 3;


    List<Layer> layers;
    int layerWidth;
    int layerHeight;
    List<Event> events;
    
    /**
     * Maps are constructed with a width, height and layerCnt, originally
     * having all null tiles.
     */
    public Map(int width, int height, int layerCnt, GraphicsBank gb) {
	this.layerHeight = height;
	this.layerWidth = width;
	this.gfx = gb;
	layers = new ArrayList<>();
	layers.add( new Layer(width, height, LayerType.GROUND, "Ground", gfx));
	this.addLayers(layerCnt);
	changeListeners = new ArrayList<>();
	
	events = new ArrayList<>();
    }


    /**
     * Maps are constructed with a width and height, originally
     * having all null tiles.
     *
     * You can also specify the base tile width and height.
     */
    public Map(int width, int height, int layerCnt, int tileWidth, int tileHeight, GraphicsBank gb) {
	this(width, height, layerCnt, gb);
	this.tileWidth = tileWidth;
	this.tileHeight = tileHeight;
	zoomWidth = tileWidth;
	zoomHeight = tileHeight;
    }

    /**
     * set the tile at location x, y in layer z.
     * Layers are:
     * 0: ground level.
     * 1: on or just above ground.
     * 2: above ground (and everything else).
     * @param x the X-location of the tile
     * @param y the Y-location of the tile.
     * @param z the layer the tile is on.
     * @param t the new Tile to set.
     */
    public void setTile(int x, int y, int z, Tile t) {
	layers.get(z).setTile(x, y, t);
	//tiles[x][y][z] = t;
    }

    public void setZoom(float z) {
	zoomWidth  = (int)(tileWidth * z);
	zoomHeight = (int)(tileHeight * z);

    }

    /**
     * used only by the game. Sets the width and height in pixels
     * to draw when rendering this map.
     */
    public void setViewSize(int width, int height) {
	this.viewWidth = width;
	this.viewHeight = height;
    }

    /**
     * used by the game. Renders the map to the given graphics context
     * at the given offsets.
     */
    public void render(Graphics g, int offsetX, int offsetY) {
	//System.out.println("Render Map");

	int minX = Math.max(offsetX/zoomWidth, 0);
	int maxX = Math.min((offsetX+viewWidth)/zoomWidth, layerWidth);

	int minY = Math.max(offsetY/zoomHeight, 0);
	int maxY = Math.min((offsetY+viewHeight)/zoomHeight, layerHeight);

	for(int z = 0; z < layers.size(); z++) {
	    for(int x=minX; x<maxX;  x++) {
		for(int y=minY; y<maxY; y++) {
		    Tile tile = layers.get(z).getTile(x, y);
		    if(tile != null)
			tile.render(g, x*zoomWidth -offsetX, y*zoomHeight -offsetY);
		}
	    }
	}
    }

    public void render(Graphics g, Camera c) {
	setViewSize(c.viewWidth, c.viewHeight);
	render(g, (int)(c.viewx - viewWidth/2), (int)(c.viewy - viewHeight/2));
    }

    /**
     * used by the level editor, renders a portion of the map with the
     * given origin and visible dimension.<p>
     * The origin and size are needed to increase efficiency by
     * only rendering what is on screen.
     *
     * @param origin the top-left visible corner of the map.
     * @param size the size to render.
     */
    public void render(Graphics g, Point origin, Dimension size) {
	
	for(int z=0; z < layers.size(); z++) {
	    render(g, origin, size, z);
	}
    }

    public void render(Graphics g, Point origin, Dimension size, int layer) {
	layers.get(layer).render(g, origin, size, zoomWidth, zoomHeight);
    }

    /**
     * gets the width of the map in tiles
     */
    public int getWidth() {
	return layerWidth;
    }
    /**
     *gets the height of the map in tiles.
     */
    public int getHeight() {
	return layerHeight;
    }
    /**
     * gets the standard width of a tile in the map.
     */
    public int getTileWidth() {
	return tileWidth;
    }
    /**
     * gets the standard height of a tile in the map.
     */
    public int getTileHeight() {
	return tileHeight;
    }

    public int getZoomWidth() {
	return zoomWidth;
    }
    public int getZoomHeight() {
	return zoomHeight;
    }

    /**
     *gets the tile at the given location.
     */
    public Tile getTile(int x, int y, int z) {
	return layers.get(z).getTile(x, y);
    }


    /**
     * Resize the map to newWidth, newHeight.
     * may clip the edges.
     **/
    public void resize(int requestedWidth, int requestedHeight) {
	System.out.println("Call resize");
	// execute idiot check
	int newWidth = Math.max(1, requestedWidth);
	int newHeight = Math.max(1, requestedHeight);
	
	for ( Layer l: layers) {
	    l.resize(newWidth, newHeight);
	}
	this.layerHeight = newHeight;
	this.layerWidth = newWidth;
    }


    /**
     * Resize with layers
     **/
    public void resize(int requestedWidth, int requestedHeight, int requestedLayers) {
	System.out.println("Call resize with layers");
	resize (requestedWidth, requestedHeight);
	
	// idiot check
	int newLayers = Math.max(1, requestedLayers);
	
	if ( newLayers > layers.size() ) {
	    addLayers( newLayers );
	} else if ( newLayers < layers.size() ) {
	    removeLayers( newLayers);
	}
    }

    private void removeLayers(int newLayers) {
	int layerCount = layers.size();
	while ( layerCount > newLayers ) {
	    // decrement first to avoid out of bounds exception
	    layerCount--;
	    layers.remove(layerCount);
	}
    }


    private void addLayers(int newLayers) {
	for ( int i = layers.size(); i < newLayers; i++) {
	    layers.add( new Layer( layerWidth, layerHeight, LayerType.SPRITE, "Sprite_" + String.valueOf(i), gfx ));
	}
    }


    /**
     * Move the map tiles
     **/
    public void shift(int offX, int offY) {
	System.out.println("Shift to new offset " + offX + ", " + offY + ".");
	for ( Layer l: layers ) {
	    l.shift(offX, offY);
	}
    }

    public void clear() {
	for ( Layer l: layers ) {
	    l.clear();
	}
    }


    /**
     * Provides a no-nonsense integer array version of this map.
     * The numbers are the tile IDs.
     * The dimensions are: x, y, layer.
     **/
    public int[][][] toIntArray() {
	int set[][][] = new int[layerWidth][layerHeight][layers.size()];
	for(int x = 0; x < layerWidth; x++) {
	    for(int y = 0; y < layerHeight; y++) {
		for(int l = 0; l < layers.size(); l++) {
		    set[x][y][l] = layers.get(l).getTileID(x, y);
//		    Tile tile = layers.get(l).getTile(x, y);
//		    if(tile != null) {
//			set[x][y][l] = tile.number;
//		    } else {
//			set[x][y][l] = 0;
//		    }
		}
	    }
	}
	return set;
    }

    /**
     * Means of setting all the tiles on a map easily.
     * You can set a different GraphicsBank as well.
     **/
    public void setAllTiles(int[][][] set, GraphicsBank bank) {
	gfx = bank;
	resize(set.length, set[0].length, set[0][0].length);

	/*
		if(set.length == tiles.length &&
		   set[0].length == tiles[0].length &&
		   set[0][0].length == tiles[0][0].length) {
	 */ 	

	for ( int l = 0; l < layers.size(); l++ ) {
	    for(int x = 0; x < layerWidth; x++) {
		    for(int y = 0; y < layerHeight; y++) {
			layers.get(l).setTile( x, y, bank.getTile(set[x][y][l]) );
		    }
		}
	    
	}
	
	/*
		} else {
			System.out.println("Use resize() Before calling setAllTiles().");
			throw new RuntimeException("The int array provided does not match the map dimensions.");
		}
	 */
    }

    /* Note: Behaviour unknown. */
    public void setTileset(GraphicsBank gfx) {
	int[][][] set = this.toIntArray();
	this.setAllTiles(set, gfx);
    }

    public void addChangeListener(MapChangeListener l) {
	changeListeners.add(l);
    }
    public void removeChangeListener(MapChangeListener l) {
	changeListeners.remove(l);
    }

    public void fireChangingEvent(boolean m) {
	Iterator<MapChangeListener> i = changeListeners.iterator();
	((MapChangeListener)i.next()).mapChanging(m);
    }
    public void fireChangedEvent(boolean m) {
	Iterator<MapChangeListener> i = changeListeners.iterator();
	((MapChangeListener)i.next()).mapChanged(m);
    }


    public int getLayerCount() {
	return this.layers.size();
    }


    public String[] getLayerNames() {
	String[] names = new String[layers.size()];
	for ( int i = 0; i < names.length; i++ ) {
	    names[i] = layers.get(i).name;
	}
	return names;
    }


    public void moveLayer(int oldLayerPosition, int newLayerPosition) {
	Layer layer = layers.get(oldLayerPosition);
	if ( newLayerPosition > oldLayerPosition ) {
	    layers.add(newLayerPosition + 1, layer);
	    layers.remove(oldLayerPosition);
	} else {
	    layers.add(newLayerPosition, layer);
	    layers.remove(oldLayerPosition + 1);
	}
    }


    public void setHideLayers(int layer) {
	for ( int i = 0; i < layers.size(); i++ ) {
	    if ( i == layer ) {
		layers.get(i).setVisible(true);
	    } else {
		layers.get(i).setVisible(false);
	    }
	}
    }


    public Layer getLayer(int z) {
	return this.layers.get(z);
    }


    public int getEvent(int x, int y, int layer) {
	return this.layers.get(layer).getTileID(x, y);
    }


    public int addEvent( int x, int y, int layer ) {
	Event event = new Event( x, y, this.events.size() );
	this.events.add(event);
	this.getLayer(layer).setTile(x, y, event.id);
	return event.id;
    }


}

