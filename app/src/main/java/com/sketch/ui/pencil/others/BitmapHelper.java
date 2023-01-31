package com.sketch.ui.pencil.others;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;

public abstract class BitmapHelper {

    protected Activity activity;
    Handler handler;
    protected int no;

    public abstract Bitmap getSketchFromBH(Bitmap bitmap) throws Throwable;

    public BitmapHelper(Activity activity, Handler handler) {

        this.no = 2;
        this.handler = handler;
        this.activity = activity;
    }

}
