package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI.pencil.others;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;

public class WeakRefHandler extends Handler {
	protected WeakReference<Activity> weakRef;

	public WeakRefHandler(Activity activity) {
		this.weakRef = new WeakReference<Activity>(activity);
	}

}
