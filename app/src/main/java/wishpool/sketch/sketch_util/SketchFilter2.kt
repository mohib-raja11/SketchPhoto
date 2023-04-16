package wishpool.sketch.sketch_util

import android.app.Activity
import android.content.Context
import wishpool.sketch.ui.pencil.others.BitmapHelper
import kotlin.Throws
import android.graphics.Bitmap
import wishpool.sketch.sketches.ColorLevels
import wishpool.sketch.sketches.GalleryFileSizeHelper
import wishpool.sketch.sketches.ThresoldHelper
import wishpool.sketch.FilterSizeHelper
import wishpool.sketch.R
import android.graphics.PorterDuff
import android.os.Handler
import wishpool.sketch.sketches.FirstSketchFilter

class SketchFilter2(activity: Activity) : wishpool.sketch.ui.pencil.others.BitmapHelper() {
    var penci2SketchValue1 = 6
    var penci2SketchValue2 = 70

    private val mContext: Context

    init {
        mContext = activity
    }

    @Throws(Throwable::class)
    override fun getSketchFromBH(scaleBitmap: Bitmap): Bitmap {
        val colorLevels: wishpool.sketch.sketches.ColorLevels
        var a = wishpool.sketch.sketches.GalleryFileSizeHelper.scaleItBitmap(scaleBitmap, 1.0)
        val thresoldHelper = ThresoldHelper()
        thresoldHelper.setThresholdValue(penci2SketchValue2)
        val a2 = thresoldHelper.getThresholdBitmap(a)
        wishpool.sketch.sketches.GalleryFileSizeHelper.MakeBitmapForMerge(
            mContext,
            a2,
            wishpool.sketch.FilterSizeHelper.getFSHbitmap(a2, R.drawable.sketch_3, R.drawable.sketch_3),
            PorterDuff.Mode.SCREEN
        )
        thresoldHelper.setThresholdValue(140)
        val bitmap3 = thresoldHelper.getThresholdBitmap(a)
        wishpool.sketch.sketches.GalleryFileSizeHelper.MakeBitmapForMerge(
            mContext,
            bitmap3,
            wishpool.sketch.FilterSizeHelper.getFSHbitmap(bitmap3, R.drawable.sketch_5, R.drawable.sketch_5),
            PorterDuff.Mode.SCREEN
        )
        wishpool.sketch.sketches.GalleryFileSizeHelper.mergeBitmapWithPorterMode(a2, bitmap3, PorterDuff.Mode.DARKEN)
        a2.recycle()
        val str = wishpool.sketch.FilterSizeHelper.hashmap1FSH[Integer.valueOf(penci2SketchValue1)]
        val firstSketchFilter = wishpool.sketch.sketches.FirstSketchFilter()
        firstSketchFilter.setSketchValue(penci2SketchValue1)
        val bitmap4 = firstSketchFilter.getFirstSketch(scaleBitmap)
        a = bitmap4
        colorLevels = wishpool.sketch.sketches.ColorLevels()
        a = colorLevels.getColorLevelBitmap(a)
        wishpool.sketch.sketches.GalleryFileSizeHelper.mergeBitmapWithPorterMode(a, bitmap3, PorterDuff.Mode.MULTIPLY)
        a.recycle()
        System.gc()
        return bitmap3
    }
}