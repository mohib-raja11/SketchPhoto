package wishpool.sketch.sketches

import android.graphics.Bitmap
import android.graphics.Color

class SecondSketchFilter : ImageFilerName() {
    var value = 2
    fun getSimpleSketch(bitmap: Bitmap): Bitmap {
        var i: Int
        var j: Int
        System.currentTimeMillis()
        val width = bitmap.width
        val height = bitmap.height
        val mArray = IntArray(width * height)
        bitmap.getPixels(mArray, 0, width, 0, 0, width, height)
        i = 0
        while (i < height) {
            j = 0
            while (j < width) {
                val k = i * width + j
                val red = Color.red(mArray[k]) * 28 + Color.green(
                    mArray[k]
                ) * 151 + Color.blue(mArray[k]) * 77 shr 8
                mArray[k] = Color.rgb(255 - red, 255 - red, 255 - red)
                j++
            }
            i++
        }
        val mArray2 = IntArray(width * height)
        val minBlurValue = MinBlurValue()
        minBlurValue.minBlurVal = value
        i = 0
        System.currentTimeMillis()
        var i4 = 0
        while (i4 < height) {
            var red = 0
            var i5 = i
            while (red < width) {
                i = -1
                var k: Int
                k = -1
                while (k <= 1) {
                    j = i4 + k
                    if (j >= 0 && j < height) {
                        val i6 = j * width
                        j = -minBlurValue.minBlurVal
                        while (true) {
                            var i7 = minBlurValue.minBlurVal
                            if (j > 0) {
                                break
                            }
                            i7 = red + j
                            if (i7 >= 0 && i7 < width) {
                                i = wishpool.sketch.sketches.RandomColorBalance.getActionColor(i, mArray[i7 + i6])
                            }
                            j++
                        }
                    }
                    k++
                }
                k = i5 + 1
                mArray2[i5] = i
                red++
                i5 = k
            }
            i4++
            i = i5
        }
        bitmap.getPixels(mArray, 0, width, 0, 0, width, height)
        simpleRGB(mArray, width, height)
        wishpool.sketch.sketches.RandomColorBalance.getColorRGB(mArray, mArray2, width, height)
        val createBitmap = Bitmap.createBitmap(
            width, height, Bitmap.Config.ARGB_8888
        )
        createBitmap.setPixels(mArray2, 0, width, 0, 0, width, height)
        System.gc()
        return createBitmap
    }

    fun getSimpleSketchValue(i: Int) {
        value = i
    }

    companion object {
        fun simpleRGB(mArray: IntArray, i: Int, j: Int) {
            for (k in 0 until j) {
                for (i4 in 0 until i) {
                    val i5 = k * i + i4
                    val red = Color.red(mArray[i5]) * 28 + Color.green(
                        mArray[i5]
                    ) * 151 + Color.blue(mArray[i5]) * 77 shr 8
                    mArray[i5] = Color.rgb(red, red, red)
                }
            }
        }
    }
}