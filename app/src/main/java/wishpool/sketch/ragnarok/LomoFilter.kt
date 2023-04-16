package wishpool.sketch.ragnarok

import android.graphics.*

internal object LomoFilter {

    init {
        System.loadLibrary("AndroidImageFilter")
    }

    fun changeToLomo(bitmap: Bitmap, roundRadius: Double): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnBitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        val scaleValue = 95 * 1.0f / 127
        val scaleMatrix = ColorMatrix()
        scaleMatrix.reset()
        scaleMatrix.setScale(
            (scaleValue + 0.2).toFloat(),
            (scaleValue + 0.4).toFloat(),
            (scaleValue + 0.2).toFloat(),
            1f
        )
        val satMatrix = ColorMatrix()
        satMatrix.reset()
        satMatrix.setSaturation(0.85f)
        val allMatrix = ColorMatrix()
        allMatrix.reset()
        allMatrix.postConcat(scaleMatrix)
        allMatrix.postConcat(satMatrix)
        paint.colorFilter = ColorMatrixColorFilter(allMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        val pixels = IntArray(width * height)
        returnBitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val resultPixels = wishpool.sketch.ragnarok.NativeFilterFunc.lomoAddBlckRound(pixels, width, height, roundRadius)
        returnBitmap.setPixels(resultPixels, 0, width, 0, 0, width, height)
        return returnBitmap
    }
}