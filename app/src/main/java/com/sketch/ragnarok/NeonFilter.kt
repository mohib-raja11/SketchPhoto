package com.sketch.ragnarok

import android.graphics.Bitmap

internal object NeonFilter {
    init {
        System.loadLibrary("AndroidImageFilter")
    }

    fun changeToNeon(bitmap: Bitmap, r: Int, g: Int, b: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val returnPixels = NativeFilterFunc.neonFilter(pixels, width, height, r, g, b)
        return Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888)
    }
}