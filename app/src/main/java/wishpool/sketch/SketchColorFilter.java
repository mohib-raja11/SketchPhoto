package wishpool.sketch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;

import wishpool.sketch.R;
import wishpool.sketch.ui.pencil.others.BitmapHelper;
import wishpool.sketch.sketches.ColorLevels;
import wishpool.sketch.sketches.GalleryFileSizeHelper;
import wishpool.sketch.sketches.SecondSketchFilter;
import wishpool.sketch.sketches.ThresoldHelper;

import wishpool.sketch.sketches.ColorLevels;
import wishpool.sketch.sketches.GalleryFileSizeHelper;
import wishpool.sketch.sketches.SecondSketchFilter;
import wishpool.sketch.sketches.ThresoldHelper;
import wishpool.sketch.ui.pencil.others.BitmapHelper;

public class SketchColorFilter extends BitmapHelper {
    int colorPencilValue;
    int colorPencilValue2;
    private final Context d;

    public SketchColorFilter(Activity activity) {
        d = activity;
        this.colorPencilValue = 2;
        this.colorPencilValue2 = 80;
    }

    @Override
    public Bitmap getSketchFromBH(Bitmap scaleBitmap) throws Throwable {
        Bitmap bitmap = null;
        ColorLevels colorLevels;
        Bitmap a = GalleryFileSizeHelper.scaleItBitmap(scaleBitmap, 1);
        ThresoldHelper thresoldHelper = new ThresoldHelper();
        thresoldHelper.setThresholdValue(this.colorPencilValue2);
        Bitmap a2 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, a2, FilterSizeHelper.getFSHbitmap(a2, R.drawable.sketch_7, R.drawable.sketch_7), Mode.SCREEN);
        thresoldHelper.setThresholdValue(120);
        Bitmap bitmap3 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, bitmap3, FilterSizeHelper.getFSHbitmap(bitmap3, R.drawable.sketch_8, R.drawable.sketch_8), Mode.SCREEN);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(a2, bitmap3, Mode.DARKEN);
        if (a2 != null && !a2.isRecycled()) {
            a2.recycle();
            System.gc();
        }
        thresoldHelper.setThresholdValue(40);
        Bitmap bitmap4 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, bitmap4, FilterSizeHelper.getFSHbitmap(bitmap4, R.drawable.sketch_1, R.drawable.sketch_1), Mode.SCREEN);
        GalleryFileSizeHelper.DrawRectToBitmap(bitmap4, 50.0f);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(bitmap3, bitmap4, Mode.MULTIPLY);
        if (bitmap3 != null && !bitmap3.isRecycled()) {
            bitmap3.recycle();
            System.gc();
        }

        SecondSketchFilter secondSketchFilter = new SecondSketchFilter();
        a2 = secondSketchFilter.getSimpleSketch(scaleBitmap);

        colorLevels = new ColorLevels();

        a2 = colorLevels.getColorLevelBitmap(a2);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(a2, bitmap4, Mode.MULTIPLY);
        a2 = GalleryFileSizeHelper.getFilterBitmapMine(a, 90);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(bitmap4, a2, Mode.SCREEN);
        if (bitmap4 != null && !bitmap4.isRecycled()) {
            bitmap4.recycle();
            System.gc();
        }

        System.gc();

        return a2;

    }

}
