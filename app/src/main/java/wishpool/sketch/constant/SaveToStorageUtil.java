package wishpool.sketch.constant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.text.format.DateFormat;
import android.util.Log;

import wishpool.sketch.utils.Ext_funsKt;


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
        String path = Ext_funsKt.getAppSketchPhotoFolderPath(context) + imgName;
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
