package com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;

import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R;
import com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.ui.pencil.others.BitmapHelper;
import com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.sketches.ColorLevels;
import com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.sketches.GalleryFileSizeHelper;
import com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.sketches.SecondSketchFilter;
import com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.sketches.ThresoldHelper;

public class CSketchFilter extends BitmapHelper {
    int comicIntValue1;
    int comicIntValue2;
    private Context d;

    public CSketchFilter(Activity activity, Handler handler) {
        super(activity, handler);
        this.comicIntValue1 = 2;
        this.comicIntValue2 = 90;
        d = activity;
    }


    @Override
    public Bitmap getSketchFromBH(Bitmap scaleBitmap)
            throws Throwable {
        ColorLevels colorLevels;
        Bitmap a = GalleryFileSizeHelper.scaleItBitmap(
                scaleBitmap, 1);
        ThresoldHelper thresoldHelper = new ThresoldHelper();
        thresoldHelper.setThresholdValue(this.comicIntValue2);
        Bitmap a2 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, a2, FilterSizeHelper
                        .getFSHbitmap(a2, R.drawable.sketch_9, R.drawable.sketch_9),
                Mode.SCREEN);
        thresoldHelper.setThresholdValue(30);
        Bitmap bitmap3 = thresoldHelper.getThresholdBitmap(a);

        GalleryFileSizeHelper.mergeBitmapWithPorterMode(a2, bitmap3,
                Mode.MULTIPLY);
        a2.recycle();
        String str = (String) FilterSizeHelper.hashmap2FSH.get(Integer
                .valueOf(this.comicIntValue1));


        SecondSketchFilter secondSketchFilter = new SecondSketchFilter();
        secondSketchFilter.getSimpleSketchValue(this.comicIntValue1);
        Bitmap bitmap4 = secondSketchFilter
                .getSimpleSketch(scaleBitmap);
        a = bitmap4;
        colorLevels = new ColorLevels();
        a = colorLevels.getColorLevelBitmap(a);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(a, bitmap3,
                Mode.MULTIPLY);
        a.recycle();
        System.gc();
        return bitmap3;

    }

}
