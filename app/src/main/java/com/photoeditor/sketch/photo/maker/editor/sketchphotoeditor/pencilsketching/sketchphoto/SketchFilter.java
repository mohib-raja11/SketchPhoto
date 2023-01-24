package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;

import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.ui.pencil.others.BitmapHelper;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.sketches.ColorLevels;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.sketches.GalleryFileSizeHelper;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.sketches.SecondSketchFilter;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.sketches.ThresoldHelper;

public class SketchFilter extends BitmapHelper {
    int pencilSketchValue1;
    int pencilSketchValue2;
    private final Context d;

    public SketchFilter(Activity activity, Handler handler) {
        super(activity, handler);
        this.pencilSketchValue1 = 2;
        this.pencilSketchValue2 = 80;
        d = activity;
    }

    @Override
    public Bitmap getSketchFromBH(Bitmap scaleBitmap)
            throws Throwable {
        ColorLevels colorLevels;
        Bitmap a = GalleryFileSizeHelper.scaleItBitmap(
                scaleBitmap, 1);
        ThresoldHelper thresoldHelper = new ThresoldHelper();
        thresoldHelper.setThresholdValue(this.pencilSketchValue2);
        Bitmap a2 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, a2, FilterSizeHelper
                        .getFSHbitmap(a2, R.drawable.sketch_6, R.drawable.sketch_6),
                Mode.SCREEN);
        thresoldHelper.setThresholdValue(120);
        Bitmap bitmap3 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, bitmap3,
                FilterSizeHelper.getFSHbitmap(bitmap3, R.drawable.sketch_5,
                        R.drawable.sketch_5), Mode.SCREEN);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(a2, bitmap3,
                Mode.DARKEN);
        if (a2 != null && !a2.isRecycled()) {
            a2.recycle();
            System.gc();
        }

        thresoldHelper.setThresholdValue(40);
        a2 = thresoldHelper.getThresholdBitmap(a);

        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, a2, FilterSizeHelper
                        .getFSHbitmap(a2, R.drawable.sketch_1, R.drawable.sketch_1),
                Mode.SCREEN);
        GalleryFileSizeHelper.DrawRectToBitmap(a2, 50.0f);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(bitmap3, a2,
                Mode.MULTIPLY);
        bitmap3.recycle();
        String str = FilterSizeHelper.hashmap2FSH.get(Integer
                .valueOf(this.pencilSketchValue1));


        SecondSketchFilter secondSketchFilter = new SecondSketchFilter();
        secondSketchFilter.getSimpleSketchValue(this.pencilSketchValue1);
        Bitmap bitmap4 = secondSketchFilter
                .getSimpleSketch(scaleBitmap);
        a = bitmap4;

        colorLevels = new ColorLevels();
        a = colorLevels.getColorLevelBitmap(a);

        GalleryFileSizeHelper.mergeBitmapWithPorterMode(a, a2,
                Mode.MULTIPLY);
        a.recycle();
        System.gc();
        return a2;

    }
}
