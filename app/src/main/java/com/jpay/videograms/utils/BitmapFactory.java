package com.jpay.videograms.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by anguyen on 12/15/2015.
 */
public class BitmapFactory {

    public static Bitmap GetFillBackGroundBitmap(Bitmap bitmap, int dx, int dy, int color) {
        if (bitmap == null || bitmap.getConfig() == null)
            return bitmap;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap newBitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas cs = new Canvas(newBitmap);
        cs.drawColor(Color.BLACK);
        cs.drawBitmap(bitmap, 0, 0, null);

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        paint.setColor(color);
        final Rect src_rect = new Rect(0, 0, width, height);
        int left = dx;
        int top = dy;
        int right = left + width;
        int bottom = top + height;
        final Rect dest_rect = new Rect(left, top, right, bottom);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);

        canvas.drawBitmap(newBitmap, src_rect, dest_rect, paint);
        return output;
    }
}
