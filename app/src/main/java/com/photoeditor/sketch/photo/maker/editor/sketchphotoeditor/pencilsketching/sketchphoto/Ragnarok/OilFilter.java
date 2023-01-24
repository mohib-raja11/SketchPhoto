package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.Ragnarok;

import android.graphics.Bitmap;

class OilFilter {
    static {
        System.loadLibrary("AndroidImageFilter");
    }

    static Bitmap changeToOil(Bitmap bitmap, int oilRange) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int[] returnPixels = NativeFilterFunc.oilFilter(pixels, width, height, oilRange);
        return Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888);
    }
}
