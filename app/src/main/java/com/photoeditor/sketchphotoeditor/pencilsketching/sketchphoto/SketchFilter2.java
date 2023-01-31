package com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;

import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R;
import com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.ui.pencil.others.BitmapHelper;
import com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.sketches.ColorLevels;
import com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.sketches.FirstSketchFilter;
import com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.sketches.GalleryFileSizeHelper;
import com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.sketches.ThresoldHelper;

public class SketchFilter2 extends BitmapHelper {
    int penci2SketchValue1;
    int penci2SketchValue2;
    private final Context d;

    public SketchFilter2(Activity activity, Handler handler) {
        super(activity, handler);
        this.penci2SketchValue1 = 6;
        this.penci2SketchValue2 = 70;
        d = activity;
    }

    @Override
    public Bitmap getSketchFromBH(Bitmap scaleBitmap) throws Throwable {
        ColorLevels colorLevels;

        Bitmap a = GalleryFileSizeHelper.scaleItBitmap(scaleBitmap, 1);
        ThresoldHelper thresoldHelper = new ThresoldHelper();
        thresoldHelper.setThresholdValue(this.penci2SketchValue2);
        Bitmap a2 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, a2, FilterSizeHelper.getFSHbitmap(a2, R.drawable.sketch_3, R.drawable.sketch_3), Mode.SCREEN);
        thresoldHelper.setThresholdValue(140);
        Bitmap bitmap3 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, bitmap3, FilterSizeHelper.getFSHbitmap(bitmap3, R.drawable.sketch_5, R.drawable.sketch_5), Mode.SCREEN);

        GalleryFileSizeHelper.mergeBitmapWithPorterMode(a2, bitmap3, Mode.DARKEN);
        a2.recycle();
        String str = FilterSizeHelper.hashmap1FSH.get(Integer.valueOf(this.penci2SketchValue1));

        FirstSketchFilter firstSketchFilter = new FirstSketchFilter();
        firstSketchFilter.setSketchValue(this.penci2SketchValue1);
        Bitmap bitmap4 = firstSketchFilter.getFirstSketch(scaleBitmap);
        a = bitmap4;
        colorLevels = new ColorLevels();

        a = colorLevels.getColorLevelBitmap(a);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(a, bitmap3, Mode.MULTIPLY);
        a.recycle();
        System.gc();
        return bitmap3;

    }
}
