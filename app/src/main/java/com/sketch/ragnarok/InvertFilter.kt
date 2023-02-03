package com.sketch.ragnarok

import android.graphics.Bitmap
import android.graphics.Color

internal object InvertFilter {

    fun chageToInvert(bitmap: Bitmap): Bitmap {

        val width = bitmap.width
        val height = bitmap.height
        val returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val colorArray = IntArray(width * height)
        var r: Int
        var g: Int
        var b: Int
        bitmap.getPixels(colorArray, 0, width, 0, 0, width, height)
        for (x in 0 until width) {
            for (y in 0 until height) {
                r = 255 - Color.red(colorArray[y * width + x])
                g = 255 - Color.green(colorArray[y * width + x])
                b = 255 - Color.blue(colorArray[y * width + x])
                colorArray[y * width + x] = Color.rgb(r, g, b)
                returnBitmap.setPixel(x, y, colorArray[y * width + x])
            }
        }
        return returnBitmap
    }
}