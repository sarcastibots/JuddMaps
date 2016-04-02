package com.sarcastibots.JuddMaps.Map;

public interface MapChangeListener {
	public void mapChanging(boolean major);
	public void mapChanged(boolean major);
}
