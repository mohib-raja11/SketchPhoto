package wishpool.sketch.ragnarok

import android.graphics.Bitmap
import wishpool.sketch.ragnarok.NativeFilterFunc

internal object BlockFilter {

    init {
        System.loadLibrary("AndroidImageFilter")
    }

    fun changeToBrick(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val returnPixels = wishpool.sketch.ragnarok.NativeFilterFunc.blockFilter(pixels, width, height)
        return Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888)
    }
}