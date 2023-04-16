package wishpool.sketch.ragnarok

import android.graphics.Bitmap

internal object OilFilter {
    init {
        System.loadLibrary("AndroidImageFilter")
    }

    fun changeToOil(bitmap: Bitmap, oilRange: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val returnPixels = wishpool.sketch.ragnarok.NativeFilterFunc.oilFilter(pixels, width, height, oilRange)
        return Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888)
    }
}