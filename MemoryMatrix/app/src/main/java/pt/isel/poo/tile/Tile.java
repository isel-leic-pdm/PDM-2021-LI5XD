package pt.isel.poo.tile;

import android.graphics.Canvas;

/**
 * Interface to implemented by each Tile 
 * @author Palex
 */
public interface Tile {
	
	/**
	 * To draw the tile
	 * @param canvas To draw the tile
	 * @param side The width of tile in pixels
	 */
	void draw(Canvas canvas, int side);
	/**
	 * To select or not select the tile. Only a tile can be selected in the panel. 
	 * Implement only if it involves change in presentation.
	 * @param selected 
	 * @return true if change presentation
	 */
	 boolean setSelect(boolean selected);
}
