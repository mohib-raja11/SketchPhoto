package wishpool.sketch.constant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.text.format.DateFormat;
import android.util.Log;

import wishpool.sketch.utils.AppUtils;

import java.io.FileOutputStream;
import java.util.Date;

import wishpool.sketch.utils.AppUtils;

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
