package wishpool.sketch.ragnarok

import android.graphics.*

internal object GrayFilter {

    @JvmStatic
    fun changeToGray(bitmap: Bitmap): Bitmap {

        val width = bitmap.width
        val height = bitmap.height
        val grayBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(grayBitmap)
        val paint = Paint()
        paint.isAntiAlias = true // 设置抗锯齿
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = filter
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return grayBitmap
    }
}