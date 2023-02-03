package com.sketch.ragnarok

import android.graphics.Bitmap

internal object ReliefFilter {
    init {
        System.loadLibrary("AndroidImageFilter")
    }

    fun changeToRelief(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val returnPixels = NativeFilterFunc.reliefFilter(pixels, width, height)

        // set the saturation
//		Canvas c = new Canvas(returnBitmap);
//		Paint paint = new Paint();
//		ColorMatrix cm = new ColorMatrix();
//		cm.setSaturation(0);
//		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
//		paint.setColorFilter(f);
//		c.drawBitmap(returnBitmap, 0, 0, paint);
        return Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888)
    }
}