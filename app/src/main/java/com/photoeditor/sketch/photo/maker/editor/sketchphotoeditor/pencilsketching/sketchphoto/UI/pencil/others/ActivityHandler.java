package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI.pencil.others;

import android.app.Activity;

import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI.pencil.ImageRemakeActivity;

public final class ActivityHandler extends WeakRefHandler {
	final ImageRemakeActivity imageRemake;

	public ActivityHandler(ImageRemakeActivity sketchEdit, Activity activity) {
		super(activity);
		this.imageRemake = sketchEdit;

	}

}
