package com.sketch.ragnarok

import android.graphics.Bitmap

internal object TvFilter {
    init {
        System.loadLibrary("AndroidImageFilter")
    }

    fun changeToTV(bitmap: Bitmap): Bitmap {
        val height = bitmap.height
        val width = bitmap.width
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val resultPixels = NativeFilterFunc.tvFilter(pixels, width, height)
        return Bitmap.createBitmap(resultPixels, width, height, Bitmap.Config.ARGB_8888)
    }
}