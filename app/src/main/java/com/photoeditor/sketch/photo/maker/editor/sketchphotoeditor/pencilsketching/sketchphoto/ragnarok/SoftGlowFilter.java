package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.ragnarok;

import android.graphics.Bitmap;

class SoftGlowFilter {
    static {
        System.loadLibrary("AndroidImageFilter");
    }

    static Bitmap softGlowFilter(Bitmap bitmap, double blurSigma) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int[] returnPixels = NativeFilterFunc.softGlow(pixels, width, height, blurSigma);

        return Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888);
    }
}
