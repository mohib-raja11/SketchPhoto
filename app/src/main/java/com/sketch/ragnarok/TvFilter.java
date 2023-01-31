package com.sketch.ragnarok;

import android.graphics.Bitmap;

class TvFilter {

    static {
        System.loadLibrary("AndroidImageFilter");
    }

    static Bitmap changeToTV(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int[] resultPixels = NativeFilterFunc.tvFilter(pixels, width, height);

        return Bitmap.createBitmap(resultPixels, width, height, Bitmap.Config.ARGB_8888);
    }
}
