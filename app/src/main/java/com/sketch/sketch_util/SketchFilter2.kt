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
import com.sketch.sketches.FirstSketchFilter

class SketchFilter2(activity: Activity) : BitmapHelper() {
    var penci2SketchValue1 = 6
    var penci2SketchValue2 = 70

    private val mContext: Context

    init {
        mContext = activity
    }

    @Throws(Throwable::class)
    override fun getSketchFromBH(scaleBitmap: Bitmap): Bitmap {
        val colorLevels: ColorLevels
        var a = GalleryFileSizeHelper.scaleItBitmap(scaleBitmap, 1.0)
        val thresoldHelper = ThresoldHelper()
        thresoldHelper.setThresholdValue(penci2SketchValue2)
        val a2 = thresoldHelper.getThresholdBitmap(a)
        GalleryFileSizeHelper.MakeBitmapForMerge(
            mContext,
            a2,
            FilterSizeHelper.getFSHbitmap(a2, R.drawable.sketch_3, R.drawable.sketch_3),
            PorterDuff.Mode.SCREEN
        )
        thresoldHelper.setThresholdValue(140)
        val bitmap3 = thresoldHelper.getThresholdBitmap(a)
        GalleryFileSizeHelper.MakeBitmapForMerge(
            mContext,
            bitmap3,
            FilterSizeHelper.getFSHbitmap(bitmap3, R.drawable.sketch_5, R.drawable.sketch_5),
            PorterDuff.Mode.SCREEN
        )
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(a2, bitmap3, PorterDuff.Mode.DARKEN)
        a2.recycle()
        val str = FilterSizeHelper.hashmap1FSH[Integer.valueOf(penci2SketchValue1)]
        val firstSketchFilter = FirstSketchFilter()
        firstSketchFilter.setSketchValue(penci2SketchValue1)
        val bitmap4 = firstSketchFilter.getFirstSketch(scaleBitmap)
        a = bitmap4
        colorLevels = ColorLevels()
        a = colorLevels.getColorLevelBitmap(a)
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(a, bitmap3, PorterDuff.Mode.MULTIPLY)
        a.recycle()
        System.gc()
        return bitmap3
    }
}