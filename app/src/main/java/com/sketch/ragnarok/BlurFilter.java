package com.sketch.ragnarok;

import android.graphics.Bitmap;

class BlurFilter {
    static {
        System.loadLibrary("AndroidImageFilter");
    }

    static Bitmap changeToAverageBlur(Bitmap bitmap, int maskSize) {
        if (maskSize % 2 == 0) {
            throw new IllegalArgumentException(String.format("the maskSize must odd, but %d is an even", maskSize));
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int[] returnPixels = NativeFilterFunc.averageSmooth(pixels, width, height, maskSize);

        return Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888);
    }
}
