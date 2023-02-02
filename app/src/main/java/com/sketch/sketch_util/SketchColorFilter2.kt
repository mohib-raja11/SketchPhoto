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

class SketchColorFilter2(activity: Activity, handler: Handler?) : BitmapHelper(activity, handler) {
    var colorPencilValue: Int
    var colorPencilValue2: Int
    private val mContext: Context

    init {
        mContext = activity
        colorPencilValue = 2
        colorPencilValue2 = 80
    }

    @Throws(Throwable::class)
    override fun getSketchFromBH(scaleBitmap: Bitmap): Bitmap {
        var bitmap: Bitmap? = null
        val colorLevels: ColorLevels
        val a = GalleryFileSizeHelper.scaleItBitmap(scaleBitmap, 1.0)
        val thresoldHelper = ThresoldHelper()
        thresoldHelper.setThresholdValue(colorPencilValue2)
        var a2 = thresoldHelper.getThresholdBitmap(a)
        GalleryFileSizeHelper.MakeBitmapForMerge(
            mContext,
            a2,
            FilterSizeHelper.getFSHbitmap(a2, R.drawable.sketch_6, R.drawable.sketch_6),
            PorterDuff.Mode.SCREEN
        )
        thresoldHelper.setThresholdValue(120)
        val bitmap3 = thresoldHelper.getThresholdBitmap(a)
        GalleryFileSizeHelper.MakeBitmapForMerge(
            mContext,
            bitmap3,
            FilterSizeHelper.getFSHbitmap(bitmap3, R.drawable.sketch_5, R.drawable.sketch_5),
            PorterDuff.Mode.SCREEN
        )
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(a2, bitmap3, PorterDuff.Mode.DARKEN)
        if (a2 != null && !a2.isRecycled) {
            a2.recycle()
            System.gc()
        }
        thresoldHelper.setThresholdValue(40)
        val bitmap4 = thresoldHelper.getThresholdBitmap(a)
        GalleryFileSizeHelper.MakeBitmapForMerge(
            mContext,
            bitmap4,
            FilterSizeHelper.getFSHbitmap(bitmap4, R.drawable.sketch_1, R.drawable.sketch_1),
            PorterDuff.Mode.SCREEN
        )
        GalleryFileSizeHelper.DrawRectToBitmap(bitmap4, 50.0f)
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(bitmap3, bitmap4, PorterDuff.Mode.MULTIPLY)
        if (bitmap3 != null && !bitmap3.isRecycled) {
            bitmap3.recycle()
            System.gc()
        }

        val str = FilterSizeHelper.hashmap2FSH[Integer.valueOf(colorPencilValue)]

        val secondSketchFilter = SecondSketchFilter()
        secondSketchFilter.getSimpleSketchValue(colorPencilValue)
        a2 = secondSketchFilter.getSimpleSketch(scaleBitmap)
        bitmap = a2
        colorLevels = ColorLevels()
        bitmap = colorLevels.getColorLevelBitmap(bitmap)
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(bitmap, bitmap4, PorterDuff.Mode.MULTIPLY)
        if (bitmap != null && !bitmap.isRecycled) {
            bitmap.recycle()
            System.gc()
        }
        bitmap = GalleryFileSizeHelper.getFilterBitmapMine(a, 90)
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(bitmap4, bitmap, PorterDuff.Mode.SCREEN)
        if (bitmap4 != null && !bitmap4.isRecycled) {
            // bitmap4.recycle();
            System.gc()
        }
        System.gc()
        return bitmap
    }
}