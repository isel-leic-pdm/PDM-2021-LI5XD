package pt.isel.poo.tile;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.SparseArray;

/**
 * Helps in the manipulation of images.<br/>
 * Loads the image to a bitmap, in memory, when it is first displayed.
 */
public class Img {
    private static SparseArray<Bitmap> cache = new SparseArray<>();
    private final int id;             // Resource id of the image data.
    private final Resources res;      // Resource object of the context.
    private final Updater updater;    // Updater to use at the end of asynchronous image loading.

    /**
     * Constructs the object representing the image.
     * @param ctx The context of activity for resources
     * @param id The resource id of the image data
     */
    public Img(Context ctx, int id) {
        this(ctx,id,null);
    }

    /**
     * Constructs the object representing the image to be loaded asynchronously.
     * @param ctx The context of activity for resources
     * @param id The resource id of the image data
     * @param updater Used at the end of asynchronous image loading.
     */
    public Img(Context ctx, int id, Updater updater) {
        this.id = id;
        res = ctx.getResources();
        this.updater = updater;
    }

    /**
     * Draws the image by resizing it to the width and height indicated.
     * @param canvas Canvas used to draw the image
     * @param width The width where the image will be displayed
     * @param height The height where the image will be displayed
     * @param p Paint used to draw
     */
    public void draw(Canvas canvas, int width, int height, Paint p) {
        draw(canvas,width,height,0,p);
    }

    private static Matrix m = new Matrix(); // Used in image transformations
    /**
     * Draws the image by resizing it to the indicated width and height,<br/>
     * and rotating it to the indicated angle.<br/>
     * Loads the  image to memory only on the first call of this method,<br/>
     * assuming that the following calls used similar width and height to the first call.
     * @param canvas Canvas used to draw the image
     * @param width The width where the image will be displayed
     * @param height The height where the image will be displayed
     * @param angle Rotation of the image in degrees (0..360)
     * @param p Paint used to draw
     */
    public void draw(Canvas canvas, int width, int height, float angle, Paint p) {
        Bitmap bitmap = getBitMap(width,height);
        if (bitmap==null) return;  // If it is loaded later asynchronously
        m.reset();
        m.postScale((float)width/bitmap.getWidth(), (float)height/bitmap.getHeight());
        if (angle!=0)
            m.postRotate(angle,(float)width/2,(float)height/2);
        canvas.drawBitmap(bitmap, m, p);
    }

    /**
     * Obtain the bitmap for the dimensions indicated as parameters and<br/>
     * of the image whose identifier was indicated in the constructor.
     * Checks whether the image already exists in the cache and has a sufficient size.
     * @param width of image
     * @param height of image
     * @return the bitmap. Can be null if the image is loaded asynchronously
     */
    private Bitmap getBitMap(int width, int height) {
        Bitmap bm = cache.get(id);  // Find in cache
        if (bm==null || bm.getWidth()<width || bm.getHeight()<height ) {
            if (updater==null) {    // Immediate loading.
                bm = load(width, height);
                cache.put(id, bm);
            } else                  // Asynchronous loading.
                new LoaderTask().execute(width,height);
        }
        return bm;
    }

    /**
     * Loads in memory the bitmap of the image with a size appropriate to the presentation area.
     * Calculates the scale factor (power of 2).
     * Can be time consuming for large images.
     * @param width
     * @param height
     * @return the bitmap loaded
     */
    private Bitmap load(int width, int height) {
        Bitmap bitmap;
        int inSampleSize = 1;
        final BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,id,opts);
        final int h = opts.outHeight, w = opts.outWidth;
        while (h/(2*inSampleSize) > height && w/(2*inSampleSize) > width) inSampleSize *= 2;
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = inSampleSize;
        bitmap = BitmapFactory.decodeResource(res,id,opts);
        return bitmap;
    }

    /**
     * Interface used in asynchronous image loading.
     */
    public interface Updater {
        /**
         * Called at the end of asynchronous image loading.
         * @param img
         */
        void updateImage(Img img);
    }

    /**
     * Task to load the image asynchronously
     */
    private class LoaderTask extends AsyncTask<Integer, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Integer... dims) {
            return load(dims[0],dims[1]);
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            cache.put(id,bitmap);
            updater.updateImage(Img.this);
        }
    }
}
