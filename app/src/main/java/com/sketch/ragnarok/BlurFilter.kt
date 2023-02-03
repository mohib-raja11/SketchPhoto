package com.sketch.ragnarok

import android.graphics.Bitmap

internal object BlurFilter {

    init {
        System.loadLibrary("AndroidImageFilter")
    }

    fun changeToAverageBlur(bitmap: Bitmap, maskSize: Int): Bitmap {
        require(maskSize % 2 != 0) {
            String.format(
                "the maskSize must odd, but %d is an even", maskSize
            )
        }
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val returnPixels = NativeFilterFunc.averageSmooth(pixels, width, height, maskSize)
        return Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888)
    }
}