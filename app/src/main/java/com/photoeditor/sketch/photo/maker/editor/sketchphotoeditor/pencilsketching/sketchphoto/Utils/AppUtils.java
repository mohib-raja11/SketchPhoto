package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.Utils;

import android.content.Context;

import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.constant.AppConstant;

import java.io.File;

public class AppUtils {

    public static String getAppFolderPath(Context context) {
        String directoryName = "Sketch Photo";

        String rootPath = context.getFilesDir().getPath() + "/" + directoryName + "/";

        if(new File(rootPath).exists()){
            return rootPath;
        }

        new File(rootPath).mkdirs();

        return rootPath;
    }

    public static String getAppTempFolderPath(Context context) {
        String directoryName = context.getString(R.string.app_name);

        String rootPath = context.getFilesDir().getPath() + "/" + directoryName + "/";

        if(new File(rootPath).exists()){
            return rootPath;
        }

        new File(rootPath).mkdirs();

        return rootPath;
    }
}
