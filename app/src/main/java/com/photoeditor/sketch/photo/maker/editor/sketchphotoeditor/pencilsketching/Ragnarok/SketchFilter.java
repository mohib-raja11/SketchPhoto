package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.Ragnarok;

import android.graphics.Bitmap;

class SketchFilter {
	
	static {
		System.loadLibrary("AndroidImageFilter");
	}

	static Bitmap changeToSketch(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		
		int[] returnPixels = NativeFilterFunc.sketchFilter(pixels, width, height);

		return Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888);
		
	}

}
