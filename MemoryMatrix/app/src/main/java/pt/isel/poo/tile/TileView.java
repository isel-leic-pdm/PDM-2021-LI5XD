package pt.isel.poo.tile;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TileView extends View {
    private static final int MIN_SIDE = 30;

    private Tile tile = defaultTile;

    public TileView(Context context) {
        super(context);
    }

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTile(Tile t) {
        tile = t;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (tile!=null) tile.draw(canvas,getWidth());
    }

    @Override
    protected void onMeasure(int wMS, int hMS) {
        int w = MeasureSpec.getSize(wMS);
        int h = MeasureSpec.getSize(hMS);
        if (MeasureSpec.getMode(hMS)== MeasureSpec.UNSPECIFIED)
            h = getSuggestedMinimumHeight();
        if (MeasureSpec.getMode(wMS)== MeasureSpec.UNSPECIFIED)
            w = getSuggestedMinimumWidth();
        int side = Math.min(w,h);
        if (side<MIN_SIDE) side = MIN_SIDE;
        setMeasuredDimension(side,side);
    }

    @Override
    protected int getSuggestedMinimumWidth() { return MIN_SIDE; }
    @Override
    protected int getSuggestedMinimumHeight() { return MIN_SIDE; }

    private static Paint paint = new Paint();
    static {
        paint.setColor(Color.DKGRAY);
    }
    private static Tile defaultTile = new Tile() {
        @Override
        public void draw( Canvas canvas, int side) {
            //canvas.drawRect(0,0,side,side,paint);
            canvas.drawLine(0,0,side,side,paint);
            canvas.drawLine(0,side,side,0,paint);
        }

        @Override
        public boolean setSelect(boolean selected) { return false; }
    };
}
