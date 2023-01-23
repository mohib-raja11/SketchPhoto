package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.constant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.text.format.DateFormat;
import android.util.Log;

import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.Utils.AppUtils;

import java.io.FileOutputStream;
import java.util.Date;

public class SaveToStorageUtil {


    public static String generateImageName() {
        Log.d("date", new Date() + "");
        return "IMG-" + DateFormat.format("yyyyMMdd-hh:mm ss", new Date()) + ".jpg";
    }


    public static String saveReal(Bitmap bitmap, Context context) {
        return saveReal(bitmap, generateImageName(), context);
    }

    public static String saveReal(Bitmap bitmap, String imgName, Context context) {
        String path = AppUtils.getAppFolderPath(context) + imgName;
        try {
            FileOutputStream out = new FileOutputStream(path);
            bitmap.compress(CompressFormat.JPEG, 100, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

}
