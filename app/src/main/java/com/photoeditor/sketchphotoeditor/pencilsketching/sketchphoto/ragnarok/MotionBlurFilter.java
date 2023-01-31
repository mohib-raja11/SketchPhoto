package com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.ragnarok;

import android.graphics.Bitmap;

class MotionBlurFilter {

    static {
        System.loadLibrary("AndroidImageFilter");
    }

    static Bitmap changeToMotionBlur(Bitmap bitmap, int xSpeed, int ySpeed) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int[] returnPixels = NativeFilterFunc.motionBlurFilter(pixels, width, height, xSpeed, ySpeed);
        return Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888);
    }
}
