package pt.isel.poo.tile;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Base type for each animation.
 * @author Palex
 */
public abstract class AnimTile {
	/**
	 * Tile to animate
	 */
	protected Tile tile;
	/**
	 * Tile current position in this animation.
	 * In pixel units.
	 */
	protected int x,y;
	/**
	 * Steps remaining to complete animation
	 */
	protected int steps;
	/**
	 * Animation started after this
	 */
	protected AnimTile after;
	
	/**
	 * Constructs an animation to the tile of the indicated position
	 * @param x Column of the tile
	 * @param y Line of the tile 
	 * @param time Total time of the animation 
	 * @param p TilePanel where the animation runs
	 */
	protected AnimTile(int x, int y, int time, TilePanel p) {
        this(x,y,time,p,null);
	}

	/**
	 * Constructs an animation to the tile of the indicated position
	 * @param x Column of the tile
	 * @param y Line of the tile
	 * @param time Total time of the animation
	 * @param p TilePanel where the animation runs
	 * @param t Tile to put
	 */
    protected AnimTile(int x, int y, int time, TilePanel p, Tile t) {
        Rect r = p.tileRect(x, y);
        this.x = r.left; this.y = r.top;
        this.tile = t==null ? p.getTile(x, y) : t ;
        steps = Math.max(time / Animator.STEP_TIME, 1);
    }

    /**
	 * Called by the animator in each step of the animation.
	 * Must be invoked to redefine this method in subclasses.
	 * @param cv 
	 * @param side
	 */
	public void stepDraw(Canvas cv, int side) {
		cv.save();
		cv.clipRect(x, y, x + side, y + side);
		cv.translate(x, y);
		if (tile==null)
			System.out.println("StepDraw: tile=null");
		tile.draw(cv,side);
		cv.restore();
	}
}