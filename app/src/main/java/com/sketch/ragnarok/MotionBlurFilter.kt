package com.sketch.ragnarok

import android.graphics.Bitmap

internal object MotionBlurFilter {

    init {
        System.loadLibrary("AndroidImageFilter")
    }

    fun changeToMotionBlur(bitmap: Bitmap, xSpeed: Int, ySpeed: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val returnPixels = NativeFilterFunc.motionBlurFilter(pixels, width, height, xSpeed, ySpeed)
        return Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888)
    }
}