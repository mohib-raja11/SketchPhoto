package com.sketch.sketch_util

import android.app.Activity
import android.content.Context
import com.sketch.ui.pencil.others.BitmapHelper
import kotlin.Throws
import android.graphics.Bitmap
import com.sketch.sketches.ColorLevels
import com.sketch.sketches.GalleryFileSizeHelper
import com.sketch.sketches.ThresoldHelper
import com.sketch.FilterSizeHelper
import com.sketch.R
import android.graphics.PorterDuff
import android.os.Handler
import com.sketch.sketches.SecondSketchFilter

class SketchFilter(activity: Activity) : BitmapHelper() {

    var pencilSketchValue1 = 2
    var pencilSketchValue2 = 80
    private val d: Context

    init {
        d = activity
    }

    @Throws(Throwable::class)
    override fun getSketchFromBH(scaleBitmap: Bitmap): Bitmap {
        val colorLevels: ColorLevels
        var a = GalleryFileSizeHelper.scaleItBitmap(
            scaleBitmap, 1.0
        )
        val thresoldHelper = ThresoldHelper()
        thresoldHelper.setThresholdValue(pencilSketchValue2)
        var a2 = thresoldHelper.getThresholdBitmap(a)
        GalleryFileSizeHelper.MakeBitmapForMerge(
            d,
            a2,
            FilterSizeHelper.getFSHbitmap(a2, R.drawable.sketch_6, R.drawable.sketch_6),
            PorterDuff.Mode.SCREEN
        )
        thresoldHelper.setThresholdValue(120)
        val bitmap3 = thresoldHelper.getThresholdBitmap(a)
        GalleryFileSizeHelper.MakeBitmapForMerge(
            d, bitmap3, FilterSizeHelper.getFSHbitmap(
                bitmap3, R.drawable.sketch_5, R.drawable.sketch_5
            ), PorterDuff.Mode.SCREEN
        )
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(
            a2, bitmap3, PorterDuff.Mode.DARKEN
        )
        if (a2 != null && !a2.isRecycled) {
            a2.recycle()
            System.gc()
        }
        thresoldHelper.setThresholdValue(40)
        a2 = thresoldHelper.getThresholdBitmap(a)
        GalleryFileSizeHelper.MakeBitmapForMerge(
            d,
            a2,
            FilterSizeHelper.getFSHbitmap(a2, R.drawable.sketch_1, R.drawable.sketch_1),
            PorterDuff.Mode.SCREEN
        )
        GalleryFileSizeHelper.DrawRectToBitmap(a2, 50.0f)
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(
            bitmap3, a2, PorterDuff.Mode.MULTIPLY
        )
        bitmap3.recycle()
        val str = FilterSizeHelper.hashmap2FSH[Integer.valueOf(pencilSketchValue1)]
        val secondSketchFilter = SecondSketchFilter()
        secondSketchFilter.getSimpleSketchValue(pencilSketchValue1)
        val bitmap4 = secondSketchFilter.getSimpleSketch(scaleBitmap)
        a = bitmap4
        colorLevels = ColorLevels()
        a = colorLevels.getColorLevelBitmap(a)
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(
            a, a2, PorterDuff.Mode.MULTIPLY
        )
        a.recycle()
        System.gc()
        return a2
    }
}