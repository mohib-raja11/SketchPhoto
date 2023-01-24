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

public class SketchColorFilter2 extends BitmapHelper {
    int colorPencilValue;
    int colorPencilValue2;
    private Context d;

    public SketchColorFilter2(Activity activity, Handler handler) {
        super(activity, handler);
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
        Bitmap bitmap4 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, bitmap4,
                FilterSizeHelper.getFSHbitmap(bitmap4, R.drawable.sketch_1,
                        R.drawable.sketch_1), Mode.SCREEN);
        GalleryFileSizeHelper.DrawRectToBitmap(bitmap4, 50.0f);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(bitmap3, bitmap4,
                Mode.MULTIPLY);
        if (bitmap3 != null && !bitmap3.isRecycled()) {
            bitmap3.recycle();
            System.gc();
        }
        String str = (String) FilterSizeHelper.hashmap2FSH.get(Integer
                .valueOf(this.colorPencilValue));


        SecondSketchFilter secondSketchFilter = new SecondSketchFilter();
        secondSketchFilter.getSimpleSketchValue(this.colorPencilValue);
        a2 = secondSketchFilter.getSimpleSketch(scaleBitmap);
        bitmap = a2;

        colorLevels = new ColorLevels();
        bitmap = colorLevels.getColorLevelBitmap(bitmap);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(bitmap, bitmap4,
                Mode.MULTIPLY);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            System.gc();
        }
        bitmap = GalleryFileSizeHelper.getFilterBitmapMine(a, 90);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(bitmap4, bitmap,
                Mode.SCREEN);
        if (bitmap4 != null && !bitmap4.isRecycled()) {
            // bitmap4.recycle();
            System.gc();
        }
        System.gc();
        return bitmap;
    }

}
