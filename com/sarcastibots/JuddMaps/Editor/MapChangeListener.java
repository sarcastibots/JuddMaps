package com.sarcastibots.JuddMaps.Editor;

interface MapChangeListener {
	public void mapChanging(boolean major);
	public void mapChanged(boolean major);
}
