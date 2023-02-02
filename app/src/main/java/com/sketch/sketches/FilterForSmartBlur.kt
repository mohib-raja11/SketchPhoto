package com.sketch.sketches

import com.sketch.sketches.BlurFunctionClass.getByteArrayHelper
import android.graphics.Bitmap
import androidx.core.view.MotionEventCompat

class FilterForSmartBlur(private val filterApplyInterface: FilterApplyInterface?) :
    ImageFilerName() {
    private var smartBlurValue1 = 50
    private var smartBlurValue2 = 10
    private var smartValue3 = 0
    fun getSmartBluredBitmap(bitmap: Bitmap): Bitmap {
        System.currentTimeMillis()
        val width = bitmap.width
        val height = bitmap.height
        val mArray = IntArray(width * height)
        val mArray2 = IntArray(width * height)
        bitmap.getPixels(mArray, 0, width, 0, 0, width, height)
        val a = getByteArrayHelper(smartBlurValue1.toFloat())
        if (filterApplyInterface != null) {
            filterApplyInterface.filterApplyInterfaceFunction(0)
        }
        smartValue3 = 1
        makeItSmartBlur(a, mArray, mArray2, width, height)
        if (filterApplyInterface != null) {
            filterApplyInterface.filterApplyInterfaceFunction(50)
        }
        smartValue3 = 2
        makeItSmartBlur(a, mArray2, mArray, height, width)
        if (filterApplyInterface != null) {
            filterApplyInterface.filterApplyInterfaceFunction(100)
        }
        val createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        createBitmap.setPixels(mArray, 0, width, 0, 0, width, height)
        System.gc()
        return createBitmap
    }

    private fun makeItSmartBlur(
        byteArrayHelper: ByteArrayHelper, mArray: IntArray, mArray2: IntArray, i: Int, i2: Int
    ) {
        val b = byteArrayHelper.setByteArray()
        val a = byteArrayHelper.setByteInt() / 2
        for (i3 in 0 until i2) {
            val i4 = i3 * i
            var i5 = 0
            var i6 = i3
            while (i5 < i) {
                var f = 0.0f
                var f2 = 0.0f
                var f3 = 0.0f
                var f4 = 0.0f
                var i7 = mArray[i4 + i5]
                val i8 = i7 shr 24 and MotionEventCompat.ACTION_MASK
                val i9 = i7 shr 16 and MotionEventCompat.ACTION_MASK
                val i10 = i7 shr 8 and MotionEventCompat.ACTION_MASK
                val i11 = i7 and MotionEventCompat.ACTION_MASK
                var f5 = 0.0f
                var f6 = 0.0f
                var f7 = 0.0f
                var f8 = 0.0f
                var i12 = -a
                while (i12 <= a) {
                    var f9: Float
                    var f10: Float
                    val f11 = b[a + i12]
                    if (f11 != 0.0f) {
                        var i13: Int
                        var i14: Int
                        var i15: Int
                        i7 = i5 + i12
                        if (i7 < 0 || i7 >= i) {
                            i7 = i5
                        }
                        i7 = mArray[i7 + i4]
                        var i16 = i7 shr 24 and MotionEventCompat.ACTION_MASK
                        val i17 = i7 shr 16 and MotionEventCompat.ACTION_MASK
                        val i18 = i7 shr 8 and MotionEventCompat.ACTION_MASK
                        val i19 = i7 and MotionEventCompat.ACTION_MASK
                        i7 = i8 - i16
                        if (i7 >= 0) {
                            if (i7 <= 0) {
                                f9 = f5 + f11
                                f5 = f4 + i16.toFloat() * f11
                                i13 = i9 - i17
                                i16 = -smartBlurValue2
                                if (i13 >= 0) {
                                    i16 = smartBlurValue2
                                    if (i13 <= 0) {
                                        f4 = f6 + f11
                                        f6 = f + i17.toFloat() * f11
                                        i14 = i10 - i18
                                        i16 = -smartBlurValue2
                                        if (i14 >= 0) {
                                            i16 = smartBlurValue2
                                            if (i14 <= 0) {
                                                f = f7 + f11
                                                f7 = f2 + i18.toFloat() * f11
                                                i15 = i11 - i19
                                                i16 = -smartBlurValue2
                                                if (i15 >= 0) {
                                                    i16 = smartBlurValue2
                                                    if (i15 <= 0) {
                                                        f2 = f8 + f11
                                                        f8 = f7
                                                        f7 = f5
                                                        f5 = f9
                                                        f9 = f2
                                                        f2 = f3 + i19.toFloat() * f11
                                                    }
                                                }
                                                f2 = f3
                                                f10 = f9
                                                f9 = f8
                                                f8 = f7
                                                f7 = f5
                                                f5 = f10
                                            }
                                        }
                                        f = f7
                                        f7 = f2
                                        i15 = i11 - i19
                                        i16 = -smartBlurValue2
                                        if (i15 >= 0) {
                                            i16 = smartBlurValue2
                                            if (i15 <= 0) {
                                                f2 = f8 + f11
                                                f8 = f7
                                                f7 = f5
                                                f5 = f9
                                                f9 = f2
                                                f2 = f3 + i19.toFloat() * f11
                                            }
                                        }
                                        f2 = f3
                                        f10 = f9
                                        f9 = f8
                                        f8 = f7
                                        f7 = f5
                                        f5 = f10
                                    }
                                }
                                f4 = f6
                                f6 = f
                                i14 = i10 - i18
                                i16 = -smartBlurValue2
                                if (i14 >= 0) {
                                    i16 = smartBlurValue2
                                    if (i14 <= 0) {
                                        f = f7 + f11
                                        f7 = f2 + i18.toFloat() * f11
                                        i15 = i11 - i19
                                        i16 = -smartBlurValue2
                                        if (i15 >= 0) {
                                            i16 = smartBlurValue2
                                            if (i15 <= 0) {
                                                f2 = f8 + f11
                                                f8 = f7
                                                f7 = f5
                                                f5 = f9
                                                f9 = f2
                                                f2 = f3 + i19.toFloat() * f11
                                            }
                                        }
                                        f2 = f3
                                        f10 = f9
                                        f9 = f8
                                        f8 = f7
                                        f7 = f5
                                        f5 = f10
                                    }
                                }
                                f = f7
                                f7 = f2
                                i15 = i11 - i19
                                i16 = -smartBlurValue2
                                if (i15 >= 0) {
                                    i16 = smartBlurValue2
                                    if (i15 <= 0) {
                                        f2 = f8 + f11
                                        f8 = f7
                                        f7 = f5
                                        f5 = f9
                                        f9 = f2
                                        f2 = f3 + i19.toFloat() * f11
                                    }
                                }
                                f2 = f3
                                f10 = f9
                                f9 = f8
                                f8 = f7
                                f7 = f5
                                f5 = f10
                            }
                        }
                        f9 = f5
                        f5 = f4
                        i13 = i9 - i17
                        i16 = -smartBlurValue2
                        if (i13 >= 0) {
                            i16 = smartBlurValue2
                            if (i13 <= 0) {
                                f4 = f6 + f11
                                f6 = f + i17.toFloat() * f11
                                i14 = i10 - i18
                                i16 = -smartBlurValue2
                                if (i14 >= 0) {
                                    i16 = smartBlurValue2
                                    if (i14 <= 0) {
                                        f = f7 + f11
                                        f7 = f2 + i18.toFloat() * f11
                                        i15 = i11 - i19
                                        i16 = -smartBlurValue2
                                        if (i15 >= 0) {
                                            i16 = smartBlurValue2
                                            if (i15 <= 0) {
                                                f2 = f8 + f11
                                                f8 = f7
                                                f7 = f5
                                                f5 = f9
                                                f9 = f2
                                                f2 = f3 + i19.toFloat() * f11
                                            }
                                        }
                                        f2 = f3
                                        f10 = f9
                                        f9 = f8
                                        f8 = f7
                                        f7 = f5
                                        f5 = f10
                                    }
                                }
                                f = f7
                                f7 = f2
                                i15 = i11 - i19
                                i16 = -smartBlurValue2
                                if (i15 >= 0) {
                                    i16 = smartBlurValue2
                                    if (i15 <= 0) {
                                        f2 = f8 + f11
                                        f8 = f7
                                        f7 = f5
                                        f5 = f9
                                        f9 = f2
                                        f2 = f3 + i19.toFloat() * f11
                                    }
                                }
                                f2 = f3
                                f10 = f9
                                f9 = f8
                                f8 = f7
                                f7 = f5
                                f5 = f10
                            }
                        }
                        f4 = f6
                        f6 = f
                        i14 = i10 - i18
                        i16 = -smartBlurValue2
                        if (i14 >= 0) {
                            i16 = smartBlurValue2
                            if (i14 <= 0) {
                                f = f7 + f11
                                f7 = f2 + i18.toFloat() * f11
                                i15 = i11 - i19
                                i16 = -smartBlurValue2
                                if (i15 >= 0) {
                                    i16 = smartBlurValue2
                                    if (i15 <= 0) {
                                        f2 = f8 + f11
                                        f8 = f7
                                        f7 = f5
                                        f5 = f9
                                        f9 = f2
                                        f2 = f3 + i19.toFloat() * f11
                                    }
                                }
                                f2 = f3
                                f10 = f9
                                f9 = f8
                                f8 = f7
                                f7 = f5
                                f5 = f10
                            }
                        }
                        f = f7
                        f7 = f2
                        i15 = i11 - i19
                        i16 = -smartBlurValue2
                        if (i15 >= 0) {
                            i16 = smartBlurValue2
                            if (i15 <= 0) {
                                f2 = f8 + f11
                                f8 = f7
                                f7 = f5
                                f5 = f9
                                f9 = f2
                                f2 = f3 + i19.toFloat() * f11
                            }
                        }
                        f2 = f3
                        f10 = f9
                        f9 = f8
                        f8 = f7
                        f7 = f5
                        f5 = f10
                    } else {
                        f9 = f8
                        f8 = f2
                        f2 = f3
                        f10 = f
                        f = f7
                        f7 = f4
                        f4 = f6
                        f6 = f10
                    }
                    i12++
                    f3 = f2
                    f2 = f8
                    f8 = f9
                    f10 = f6
                    f6 = f4
                    f4 = f7
                    f7 = f
                    f = f10
                }
                val f12 = if (f5 == 0.0f) i8.toFloat() else f4 / f5
                f4 = if (f6 == 0.0f) i9.toFloat() else f / f6
                mArray2[i6] =
                    RandomColorBalance.getActionMask(((if (f8 == 0.0f) i11.toFloat() else f3 / f8).toDouble() + 0.5).toInt()) or (RandomColorBalance.getActionMask(
                        ((if (f7 == 0.0f) i10.toFloat() else f2 / f7).toDouble() + 0.5).toInt()
                    ) shl 8 or (RandomColorBalance.getActionMask((f4.toDouble() + 0.5).toInt()) shl 16 or (RandomColorBalance.getActionMask(
                        (f12.toDouble() + 0.5).toInt()
                    ) shl 24)))
                i5++
                i6 += i2
            }
            if (i3 % 100 == 0 && filterApplyInterface != null) {
                var d = i3.toDouble() * 1.0 / i2.toDouble() * 100.0
                if (smartValue3 == 1) {
                    d /= 2.0
                } else if (smartValue3 == 2) {
                    d = d / 2.0 + 50.0
                }
                filterApplyInterface.filterApplyInterfaceFunction(d.toInt())
            }
        }
    }

    fun setSmartBlurValue1() {
        smartBlurValue1 = 40
    }

    fun setSmartBlurValue2() {}
    fun setSmartBlurValue3() {
        smartBlurValue2 = 30
    }
}