package pt.isel.poo.tile;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Makes all the animations of the mosaics made in TilePanel
 * @see TilePanel#getAnimator()
 * @author Palex
 */
public class Animator {
	private LinkedList<AnimTile> anims = new LinkedList<>();  // All active animations
	private TilePanel panel; // The TilePanel in used

	private long nextTime;  // time for the next animation
	private int adjust = 0; // steps to subtract in next animation

	// Time interval for steps of animations (in milisecs)
	static int STEP_TIME = 50;  //[15..60]

	// The listener of animations ends
	private OnFinishAnimationListener listener;
	private Object listenerTag; // Tag to give the listener

    /**
     * Constructor.
     * Use by method getAnimator() of TilePanel
     * @param p The Tile Panel
     */
    Animator(TilePanel p) {  panel = p;  }

    /**
	 * Trigger for the listener of animations ends
     * Defines the listener called when the current animations finish
     * @param tag Tag to give the listener
     * @param l Listener to call when hen the current animations finish
	 */
	public void triggerOnFinishAnimations(Object tag, OnFinishAnimationListener l) {
		if (listener!=null) 
			throw new IllegalStateException("last listener is not trigger");
		if (anims.size()==0) 
			l.onFinish(tag);
		else {
			listener = l;
			listenerTag = tag;
		}
	}

    /**
     * Adds a generic animation defined by a AnimTile object
     * The animation starts immediately
     * @see AnimTile
     * @param anim The animation to start
     */
	public void addAnim(AnimTile anim) {
		anims.add(anim);
		if (anims.size()==1) {
			panel.postInvalidateDelayed(STEP_TIME);
			nextTime = System.currentTimeMillis()+STEP_TIME;
		}		
	}

    /**
     * Adds a generic animation defined by a AnimTile object
     * The animation starts when the animation "before" ends
     * @see AnimTile
     * @param anim The animation to start
     * @param before The animation which is expected to end
     */
	public void addAnimAfter(AnimTile anim, AnimTile before) {
		if (before==null || before.steps<=0)
			addAnim(anim);
		else
            before.after = anim;
	}

    /**
     * Indicates if there are animations in progress
     * @return true if there are animations
     */
	public boolean hasAnimations() { return anims.size()>0; }

    /**
     * Get the last scheduled animation of a tile
     * @param tile the tile of the animation to find
     * @return The last animation of that tile
     */
	public AnimTile getAnim(Tile tile) {
	   if (anims.size()==0) return null;
	   for(AnimTile as : anims)
		   if (as.tile==tile) {
               while(as.after!=null && as.tile==tile) as = as.after;
               return as;
           }
	   return null;
	}

	// Draw animations. Called by onDraw() of TilePanel
	void drawAnims(Canvas canvas) {
	   long tm = System.currentTimeMillis();
	   if (anims.size()==0 || nextTime > tm) return;
	   ListIterator<AnimTile> i = anims.listIterator();   	// Get iterator
	   while( i.hasNext() ) {						
		   AnimTile a = i.next();					// Next animation
		   if (adjust>0) a.steps -= Math.min(adjust, a.steps - 1);
           a.stepDraw(canvas, panel.sideTile);			// draw in next position
		   if (--a.steps<=0) {                            // Last step?
               if (a.after != null) i.set(a.after);        // Swap with the next
               else i.remove();                            // Remove from animation list
           }
	   }
	   adjust = 0;
	   if (anims.size()==0) {
		   if (listener!=null) {
			   OnFinishAnimationListener l = listener;
			   listener = null;
			   l.onFinish(listenerTag);
		   }
	   } else {
		   tm = System.currentTimeMillis();
		   nextTime += STEP_TIME;						// Time for next animation
		   long remain = nextTime - tm;
		   if (remain < 0) {
			   adjust = (int)(-remain / STEP_TIME) + 1;
			   remain += adjust*STEP_TIME;
			   nextTime += adjust*STEP_TIME;
			   //System.out.println("adjust="+adjust);
		   }
		   panel.postInvalidateDelayed(remain);			// draw past remain time
	   }
	}

    // Animation to move a tile over the grid from one position to another
    private class FloatTile extends AnimTile {
		int fx,fy;  // final position
		
		FloatTile(int xF, int yF, int xTo, int yTo, int tm) {
			this(xF,yF,xTo,yTo, tm, null);
		}
        FloatTile(int xF, int yF, int xTo, int yTo, int tm, Tile t) {
            super(xF,yF,tm, Animator.this.panel,t);
            Rect r = panel.tileRect(xTo, yTo);
            fx = r.left; fy = r.top;
        }
		public void stepDraw(Canvas cv, int side) {
			x += (fx-x)/steps;
			y += (fy-y)/steps;
			super.stepDraw(cv,side);
		}		
	}

	/**
	 * Animate the tile from one position to another.
     * If the tile already has animations in progress, it will be scheduled for after the last ever scheduled
	 * @param xFrom  x coordinate of original position
	 * @param yFrom  y coordinate of original position
	 * @param xTo  x coordinate of destination position
	 * @param yTo  y coordinate of destination position
	 * @param time total time of animation 
	 * @return 
	 */
	public AnimTile floatTile(int xFrom, int yFrom, int xTo, int yTo, int time) {
		Tile t = panel.getTile(xFrom, yFrom);
		panel.setTileNoInvalidate(xTo,yTo, t);
		AnimTile ma = new FloatTile(xFrom, yFrom, xTo, yTo, time, t);
		addAnimAfter(ma, getAnim(t));
		panel.setTileNoInvalidate(xFrom, yFrom, null);
		return ma;
	}

    /**
     * Animate the tile from external position to a internal.
     * @param xFrom  x coordinate of external position
     * @param yFrom  y coordinate of external position
     * @param xTo  x coordinate of destination position
     * @param yTo  y coordinate of destination position
     * @param time total time of animation
     * @param newTile tile to put on destination
     * @return
     */
    public AnimTile entryTile(int xFrom, int yFrom, int xTo, int yTo, int time, Tile newTile) {
        panel.setTileNoInvalidate(xTo, yTo, newTile);
        AnimTile ma = new FloatTile(xFrom, yFrom, xTo, yTo, time, newTile);
        addAnim(ma);
        return ma;
    }

    /**
     * Animate the tile from one position to a external position.
     * If the tile already has animations in progress, it will be scheduled for after the last ever scheduled
     * @param xFrom  x coordinate of start position
     * @param yFrom  y coordinate of start position
     * @param xTo  x coordinate of external position
     * @param yTo  y coordinate of external position
     * @param time total time of animation
     * @return
     */
    public AnimTile exitTile(int xFrom, int yFrom, int xTo, int yTo, int time) {
        Tile t = panel.getTile(xFrom, yFrom);
        AnimTile ma = new FloatTile(xFrom, yFrom, xTo, yTo, time, t);
        panel.setTileNoInvalidate(xFrom, yFrom, null);
        addAnimAfter(ma, getAnim(t));
        return ma;
    }

    // Animation to expand a tile from zero size to the natural size
    private class ExpandTile extends AnimTile {
		float factor = 0.1F;
		int xf, yf;
		Tile t; int xt, yt;
		
		ExpandTile(int x, int y, int tm, Tile newTile, TilePanel p) {
			super(x, y, tm, p);
			if (newTile!=null) {
				t = newTile;
				xt = x; yt = y;
			}
			float dif = p.sideTile*(1-factor)/2;
			xf = this.x; yf = this.y;
			this.x+=(int)dif; this.y+=(int)dif;
		}
		public void stepDraw(Canvas cv, int side) {
			if (t!=null) {
				panel.setTileNoInvalidate(xt, yt, t);
				tile = t;
				t=null;
			}
			x += (xf-x)/steps;
			y += (yf-y)/steps;
			factor += (1-factor)/steps;
			super.stepDraw(cv, (int)(side*factor));
		}				
	}
	
	/**
	 * Expands the size of the tile from zero to the natural dimension
     * The animation may starts immediately (before==null)
     * The animation starts when the animation "before" ends
	 * @param x coordinate of the tile to expand
	 * @param y coordinate of the tile to expand
	 * @param time total time of animation
     * @param before The animation which is expected to end. null if the animation is to start immediately
	 * @param newTile New tile to put in the location (x,y)
	 */
	public AnimTile expandTile(int x, int y, int time, AnimTile before, Tile newTile) {
		AnimTile et = new ExpandTile(x, y, time, newTile, Animator.this.panel);
		addAnimAfter(et, before);
		return et;
	}

    // Animation to contract a tile from the natural size to zero size
	private class ShrinkTile extends AnimTile {
		int xf=-1, yf;
        float totalSteps;

		ShrinkTile(int x, int y, int tm, TilePanel p) {
			super(x, y, tm, p);
            totalSteps = steps;
		}
		public void stepDraw(Canvas cv, int side) {
            if (xf<0) {
                xf = x+side/2;
                yf = y+side/2;
            }
			x += (xf-x)/steps;
			y += (yf-y)/steps;
			super.stepDraw(cv, (int) (side * (steps - 1) / totalSteps));
        }
	}

	/**
	 * Expands the size of the mosaic from zero to the natural dimension
     * The animation may starts immediately (before==null)
     * The animation starts when the animation "before" ends
     * @param x coordinate of the tile to shrink
     * @param y coordinate of the tile to shrink
     * @param time total time of animation
     * @param before The animation which is expected to end. null if the animation is to start immediately
	 */
	public AnimTile shrinkTile(int x, int y, int time, AnimTile before) {
		AnimTile st = new ShrinkTile(x, y, time, Animator.this.panel);
        if (before!=null) addAnimAfter(st,before);
		else addAnim(st);
		return st;
	}
}