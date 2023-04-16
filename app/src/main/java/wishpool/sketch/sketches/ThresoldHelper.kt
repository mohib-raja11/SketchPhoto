package wishpool.sketch.sketches

import wishpool.sketch.sketches.ImageFilerName
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.view.MotionEventCompat

class ThresoldHelper : ImageFilerName() {
    private var thresoldValue = 102
    fun setThresholdValue(i: Int) {
        thresoldValue = i
    }

    fun getThresholdBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val mArray = IntArray(width * height)
        bitmap.getPixels(mArray, 0, width, 0, 0, width, height)
        for (i in 0 until width) {
            for (i2 in 0 until height) {
                var i3 = mArray[i2 * width + i]
                i3 =
                    if ((i3 and MotionEventCompat.ACTION_MASK) * 77 + ((16711680 and i3 shr 16) * 28 + (MotionEventCompat.ACTION_POINTER_INDEX_MASK and i3 shr 8) * 151) shr 8 > thresoldValue) MotionEventCompat.ACTION_MASK else 0
                mArray[i2 * width + i] = Color.rgb(i3, i3, i3)
            }
        }
        val createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        createBitmap.setPixels(mArray, 0, width, 0, 0, width, height)
        System.gc()
        return createBitmap
    }
}