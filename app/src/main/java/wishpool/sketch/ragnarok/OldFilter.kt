package wishpool.sketch.ragnarok

import android.graphics.Bitmap
import android.graphics.Color

internal object OldFilter {

    fun changeToOld(bitmap: Bitmap): Bitmap {

        val width = bitmap.width
        val height = bitmap.height

        var pixColor: Int
        var pixR: Int
        var pixG: Int
        var pixB: Int
        var newR: Int
        var newG: Int
        var newB: Int
        val pixels = IntArray(width * height)

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        for (i in 0 until height) {
            for (k in 0 until width) {
                pixColor = pixels[width * i + k]
                pixR = Color.red(pixColor)
                pixG = Color.green(pixColor)
                pixB = Color.blue(pixColor)
                newR = (0.393 * pixR + 0.769 * pixG + 0.189 * pixB).toInt()
                newG = (0.349 * pixR + 0.686 * pixG + 0.168 * pixB).toInt()
                newB = (0.272 * pixR + 0.534 * pixG + 0.131 * pixB).toInt()
                val newColor =
                    Color.argb(255, Math.min(newR, 255), Math.min(newG, 255), Math.min(newB, 255))
                pixels[width * i + k] = newColor
            }
        }

        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
    }
}