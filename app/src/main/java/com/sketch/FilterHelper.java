package com.sketch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;

import com.sketch.ui.pencil.others.BitmapHelper;
import com.sketch.sketches.GalleryFileSizeHelper;

public class FilterHelper extends BitmapHelper {
    public FilterHelper(Activity activity, Handler handler) {
        super(activity, handler);
    }

    @Override
    public Bitmap getSketchFromBH(Bitmap scaleBitmap) {
        return GalleryFileSizeHelper.scaleItBitmap(scaleBitmap, 1);
    }
}
