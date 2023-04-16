package wishpool.sketch.ragnarok

import android.graphics.Bitmap

internal object SketchFilter {
    init {
        System.loadLibrary("AndroidImageFilter")
    }

    fun changeToSketch(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val returnPixels = wishpool.sketch.ragnarok.NativeFilterFunc.sketchFilter(pixels, width, height)
        return Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888)
    }
}