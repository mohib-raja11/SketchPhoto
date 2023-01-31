package com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.ui.pencil.others;

import android.app.Activity;

import com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.ui.pencil.ImageRemakeActivity;

public final class ActivityHandler extends WeakRefHandler {
    final ImageRemakeActivity imageRemake;

    public ActivityHandler(ImageRemakeActivity sketchEdit, Activity activity) {
        super(activity);
        this.imageRemake = sketchEdit;

    }

}
