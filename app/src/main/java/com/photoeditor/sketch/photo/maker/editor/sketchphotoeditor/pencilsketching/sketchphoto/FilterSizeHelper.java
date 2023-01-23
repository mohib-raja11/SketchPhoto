package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto;

import android.graphics.Bitmap;
import java.util.HashMap;

public final class FilterSizeHelper {
	public static int[] intArrayFSH;
	public static final HashMap<Integer, String> hashmap1FSH;
	public static final HashMap<Integer, String> hashmap2FSH;
	private static int intFSH;

	public static int getFSHbitmap(Bitmap bitmap, int i, int i2) {
		return Math.min(bitmap.getWidth(), bitmap.getHeight()) > 480 ? i2 : i;
	}

	static {
		intArrayFSH = new int[] { 640, 480, 320 };
		intFSH = 0;
		hashmap1FSH = new LineHelper();
		hashmap2FSH = new LineHelper_2();
	}

	public static void setIntFSH(int i) {
		intFSH = i;
	}

	public static int getIntFSH() {
		return intFSH;
	}
}
