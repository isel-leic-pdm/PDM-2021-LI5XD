package pt.isel.poo.tile;

/**
 * Listener of tiles touches and moves.
 * @author Palex
 */
public interface OnTileTouchListener {

	/**
	 * When a tile is clicked.
	 * @param xTile x coordinate of the tile clicked
	 * @param yTile y coordinate of the tile clicked
	 * @return true if it has effect
	 */
	boolean onClick(int xTile, int yTile);

	/**
	 * When a tile is dragged.
	 * This method must call "setTile" or "floatTile" of TilePanel to change the tiles positions.
	 * @param xFrom x coordinate of the tile that was trying to drag
	 * @param yFrom y coordinate of the tile that was trying to drag
	 * @param xTo x coordinate to drag to
	 * @param yTo y coordinate to drag to
	 * @return true if the drag can continue
	 */
	boolean onDrag(int xFrom, int yFrom, int xTo, int yTo);

	/**
	 * When a drag ends. (ACTION_UP)
	 * The tile coordinate of this event is the same of last trailing.
	 * @param x x coordinate of last destination of drag
	 * @param y y coordinate of last destination of drag
	 */
	void onDragEnd(int x, int y);

    /**
     * When the drag is canceled. (move out side panel)
     */
	void onDragCancel();
}