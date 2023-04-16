package wishpool.sketch.ragnarok

import android.graphics.Bitmap

internal object GothamFilter {

    init {
        System.loadLibrary("AndroidImageFilter")
    }

    fun changeToGotham(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val returnPixles = wishpool.sketch.ragnarok.NativeFilterFunc.gothamFilter(pixels, width, height)
        return Bitmap.createBitmap(returnPixles, width, height, Bitmap.Config.ARGB_8888)
    }
}