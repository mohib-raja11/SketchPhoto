package wishpool.sketch.sketch_util

import android.app.Activity
import android.content.Context
import wishpool.sketch.ui.pencil.others.BitmapHelper
import kotlin.Throws
import android.graphics.Bitmap
import wishpool.sketch.sketches.ColorLevels
import wishpool.sketch.sketches.GalleryFileSizeHelper
import wishpool.sketch.FilterSizeHelper

import android.graphics.PorterDuff
import wishpool.sketch.R
import wishpool.sketch.sketches.SecondSketchFilter
import wishpool.sketch.sketches.ThresoldHelper

class CSketchFilter(activity: Activity) : BitmapHelper() {

    var comicIntValue1 = 2
    var comicIntValue2 = 90

    private val mContext: Context

    init {
        mContext = activity
    }

    @Throws(Throwable::class)
    override fun getSketchFromBH(scaleBitmap: Bitmap): Bitmap {

        var a = GalleryFileSizeHelper.scaleItBitmap(scaleBitmap, 1.0)
        val thresoldHelper = ThresoldHelper()

        thresoldHelper.setThresholdValue(comicIntValue2)
        val a2 = thresoldHelper.getThresholdBitmap(a)
        GalleryFileSizeHelper.MakeBitmapForMerge(
            mContext, a2, FilterSizeHelper.getFSHbitmap(
                a2, R.drawable.sketch_9, R.drawable.sketch_9
            ), PorterDuff.Mode.SCREEN
        )

        thresoldHelper.setThresholdValue(30)
        val bitmap3 = thresoldHelper.getThresholdBitmap(a)
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(
            a2, bitmap3, PorterDuff.Mode.MULTIPLY
        )
        a2.recycle()
        val str = FilterSizeHelper.hashmap2FSH[Integer.valueOf(comicIntValue1)]
        val secondSketchFilter = SecondSketchFilter()
        secondSketchFilter.getSimpleSketchValue(comicIntValue1)
        val bitmap4 = secondSketchFilter.getSimpleSketch(scaleBitmap)
        a = bitmap4
        val colorLevels = ColorLevels()
        a = colorLevels.getColorLevelBitmap(a)
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(
            a, bitmap3, PorterDuff.Mode.MULTIPLY
        )
        a.recycle()
        System.gc()

        return bitmap3
    }
}