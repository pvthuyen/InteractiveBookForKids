package com.example.pvthuyen.interactivebookforkids;

import android.graphics.Bitmap;

/**
 * Created by pvthuyen on 6/29/15.
 */
public final class BitmapScaler {
    static float screenHeight;
    static float screenWidth;
    public static Bitmap rescale(Bitmap realImage, boolean vertical, boolean filter) {
        float maxImageSize;
        if (vertical)
            maxImageSize = screenWidth;
        else
            maxImageSize = screenHeight;
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}
