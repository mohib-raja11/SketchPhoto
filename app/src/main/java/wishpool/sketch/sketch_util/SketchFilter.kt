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
import wishpool.sketch.sketches.SecondSketchFilter

class SketchFilter(activity: Activity) : wishpool.sketch.ui.pencil.others.BitmapHelper() {

    var pencilSketchValue1 = 2
    var pencilSketchValue2 = 80
    private val d: Context

    init {
        d = activity
    }

    @Throws(Throwable::class)
    override fun getSketchFromBH(scaleBitmap: Bitmap): Bitmap {
        val colorLevels: wishpool.sketch.sketches.ColorLevels
        var a = wishpool.sketch.sketches.GalleryFileSizeHelper.scaleItBitmap(
            scaleBitmap, 1.0
        )
        val thresoldHelper = ThresoldHelper()
        thresoldHelper.setThresholdValue(pencilSketchValue2)
        var a2 = thresoldHelper.getThresholdBitmap(a)
        wishpool.sketch.sketches.GalleryFileSizeHelper.MakeBitmapForMerge(
            d,
            a2,
            wishpool.sketch.FilterSizeHelper.getFSHbitmap(a2, R.drawable.sketch_6, R.drawable.sketch_6),
            PorterDuff.Mode.SCREEN
        )
        thresoldHelper.setThresholdValue(120)
        val bitmap3 = thresoldHelper.getThresholdBitmap(a)
        wishpool.sketch.sketches.GalleryFileSizeHelper.MakeBitmapForMerge(
            d, bitmap3, wishpool.sketch.FilterSizeHelper.getFSHbitmap(
                bitmap3, R.drawable.sketch_5, R.drawable.sketch_5
            ), PorterDuff.Mode.SCREEN
        )
        wishpool.sketch.sketches.GalleryFileSizeHelper.mergeBitmapWithPorterMode(
            a2, bitmap3, PorterDuff.Mode.DARKEN
        )
        if (a2 != null && !a2.isRecycled) {
            a2.recycle()
            System.gc()
        }
        thresoldHelper.setThresholdValue(40)
        a2 = thresoldHelper.getThresholdBitmap(a)
        wishpool.sketch.sketches.GalleryFileSizeHelper.MakeBitmapForMerge(
            d,
            a2,
            wishpool.sketch.FilterSizeHelper.getFSHbitmap(a2, R.drawable.sketch_1, R.drawable.sketch_1),
            PorterDuff.Mode.SCREEN
        )
        wishpool.sketch.sketches.GalleryFileSizeHelper.DrawRectToBitmap(a2, 50.0f)
        wishpool.sketch.sketches.GalleryFileSizeHelper.mergeBitmapWithPorterMode(
            bitmap3, a2, PorterDuff.Mode.MULTIPLY
        )
        bitmap3.recycle()
        val str = wishpool.sketch.FilterSizeHelper.hashmap2FSH[Integer.valueOf(pencilSketchValue1)]
        val secondSketchFilter = SecondSketchFilter()
        secondSketchFilter.getSimpleSketchValue(pencilSketchValue1)
        val bitmap4 = secondSketchFilter.getSimpleSketch(scaleBitmap)
        a = bitmap4
        colorLevels = wishpool.sketch.sketches.ColorLevels()
        a = colorLevels.getColorLevelBitmap(a)
        wishpool.sketch.sketches.GalleryFileSizeHelper.mergeBitmapWithPorterMode(
            a, a2, PorterDuff.Mode.MULTIPLY
        )
        a.recycle()
        System.gc()
        return a2
    }
}