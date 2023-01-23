package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;

import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.pencil.others.BitmapHelper;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.sketches.GalleryFileSizeHelper;

public class FilterHelper extends BitmapHelper {
	public FilterHelper(Activity activity, Handler handler) {
		super(activity, handler);
	}

	@Override
	public Bitmap getSketchFromBH(Bitmap scaleBitmap) {
		return GalleryFileSizeHelper.scaleItBitmap(scaleBitmap,1);
	}
}
