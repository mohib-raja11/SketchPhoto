package com.sketch.ragnarok

import android.graphics.Bitmap

internal object PixelateFilter {
    init {
        System.loadLibrary("AndroidImageFilter")
    }

    fun changeToPixelate(bitmap: Bitmap, pixelSize: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val returnPixels = NativeFilterFunc.pxelateFilter(pixels, width, height, pixelSize)
        return Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888)
    }
}