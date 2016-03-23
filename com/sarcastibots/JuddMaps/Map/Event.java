package com.sarcastibots.JuddMaps.Map;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;


public class Event {
    int id;
    Rectangle location;
    
    List<String> conditions;
    List<String> results;
    
    public Event ( int x, int y, int id) {
	this.id = id;
	int width = 0;
	int height = 0;
	this.location = new Rectangle( x, y, width, height);
	
	conditions = new ArrayList<>();
	results = new ArrayList<>();
    }
    
    public int getID() {
	return this.id;
    }

    public Rectangle getLocation() {
	return this.location;
    }

    public List<String> getConditions() {
	return conditions;
    }
    
    public List<String> getResults() {
	return results;
    }
}
