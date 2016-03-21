package com.sarcastibots.JuddMaps.Map;

import java.net.URL;

import javax.swing.ImageIcon;

public class EventTile extends Tile {
    public EventTile ( int eventNum) {
	super();
	this.path = "/events/event.png";
	URL imgURL = getClass().getResource(path);
	this.image = new ImageIcon(imgURL).getImage();
	this.name = "event";
	this.type = "event";
	this.number = eventNum;
	this.imageWidth = image.getWidth(null);
	this.imageHeight = image.getHeight(null);
	this.zoomWidth = imageWidth;
	this.zoomHeight = imageHeight;
    }
}
