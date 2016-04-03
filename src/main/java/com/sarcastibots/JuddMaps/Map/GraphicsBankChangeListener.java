package com.sarcastibots.JuddMaps.Map;

public interface GraphicsBankChangeListener {
    /* Large change happened such as loading a tileset */
    public void tilesetUpdated(GraphicsBank bank);
    /* A single tile was removed */
    public void tileRemoved(GraphicsBank bank, Tile removed);
    /* A single tile was added */
    public void tileAdded(GraphicsBank bank, Tile added);
}