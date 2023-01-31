package com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.ragnarok;

import android.graphics.Bitmap;

class NeonFilter {

    static {
        System.loadLibrary("AndroidImageFilter");
    }


    static Bitmap changeToNeon(Bitmap bitmap, int r, int g, int b) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();


        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int[] returnPixels = NativeFilterFunc.neonFilter(pixels, width, height, r, g, b);

        return Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888);
    }
}
