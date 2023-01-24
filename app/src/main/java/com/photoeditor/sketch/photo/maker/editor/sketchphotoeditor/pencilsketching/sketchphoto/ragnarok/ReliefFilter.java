package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.ragnarok;

import android.graphics.Bitmap;


class ReliefFilter {

    static {
        System.loadLibrary("AndroidImageFilter");
    }

    static Bitmap changeToRelief(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int[] returnPixels = NativeFilterFunc.reliefFilter(pixels, width, height);

        // set the saturation
//		Canvas c = new Canvas(returnBitmap);
//		Paint paint = new Paint();
//		ColorMatrix cm = new ColorMatrix();
//		cm.setSaturation(0);
//		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
//		paint.setColorFilter(f);
//		c.drawBitmap(returnBitmap, 0, 0, paint);

        return Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888);
    }


}