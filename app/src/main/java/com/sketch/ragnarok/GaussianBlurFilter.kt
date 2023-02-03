package com.sketch.ragnarok

import android.graphics.Bitmap

internal object GaussianBlurFilter {

    init {
        System.loadLibrary("AndroidImageFilter")
    }

    fun changeToGaussianBlur(bitmap: Bitmap, sigma: Double): Bitmap {
        val ksize = (sigma * 3 + 1).toInt()
        require(ksize != 1) { String.format("sigma %f is too small", sigma) }
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val returnPixels = NativeFilterFunc.discreteGaussianBlur(pixels, width, height, sigma)
        return Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888)
    }
}