package com.sketch.ragnarok

import android.graphics.Bitmap

internal object LightFilter {

    init {
        System.loadLibrary("AndroidImageFilter")
    }

    fun changeToLight(bitmap: Bitmap, centerX: Int, centerY: Int, radius: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val returnPixels =
            NativeFilterFunc.lightFilter(pixels, width, height, centerX, centerY, radius)
        return Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888)
    }
}