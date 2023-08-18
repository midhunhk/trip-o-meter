package com.ae.apps.lib.custom.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * An ImageView that shows the source image within a Round shape.
 * This should be similar to how Google+ shows profile images.
 * Use it like you would an ImageView.
 *
 * @author based on the stackoverflow question at
 * <p>
 * <a href="http://stackoverflow.com/questions/16208365/create-a-circular-image-view-in-android">...</a>
 */
public class RoundedImageView extends AppCompatImageView {

    private static final int RADIUS_DIFF = 10;

    public RoundedImageView(Context context) {
        super(context);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap originalBitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);

        int imageRadius = getImageRadiusSize();

        // Create the rounded bitmap and display it using the Canvas
        Bitmap roundBitmap = getCroppedBitmap(bitmap, imageRadius);
        canvas.drawBitmap(roundBitmap, 0, 0, null);
    }

    /**
     * Get radius size for the rounded image
     * We use height for calculating the radius
     *
     * @return image radius
     */
    protected int getImageRadiusSize() {
        int imageHeight = getHeight();
        int minHeight = (getSuggestedMinimumHeight() > 0) ? getSuggestedMinimumHeight() : imageHeight;
        int maxHeight = minHeight + RADIUS_DIFF;

        // getMaxHeight() is min api 16
        // int maxHeight = (getMaxHeight() > 0) ? getMaxHeight() : imageHeight;

        // Check if image height is atleast minheight
        if (imageHeight <= minHeight) {
            return minHeight;
        }

        // Check if image size is at most maxHeight
        return Math.min(imageHeight, maxHeight);
    }

    /**
     * Creates the cropped bitmap in a circular shape
     *
     * @param bitmap Source bitmap which should be in a rectangular shape
     * @param radius The radius for the Round shape
     */
    protected Bitmap getCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap scaledBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius) {
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        } else {
            scaledBitmap = bitmap;
        }
        Bitmap output = Bitmap.createBitmap(scaledBitmap.getWidth(), scaledBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // Create the drawing tools and draw
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, radius, radius);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);

        // TODO : remove hardcoding here
        paint.setColor(getResources().getColor(android.R.color.white));
        float cx = scaledBitmap.getWidth() / 2;
        float cy = scaledBitmap.getHeight() / 2;
        canvas.drawCircle(cx + 0.7f, cy + 0.7f, cx + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(scaledBitmap, rect, rect, paint);
        return output;
    }

}