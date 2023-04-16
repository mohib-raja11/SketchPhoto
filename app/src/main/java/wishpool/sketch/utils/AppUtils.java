package wishpool.sketch.utils;

import android.content.Context;

import wishpool.sketch.R;

import java.io.File;

public class AppUtils {

    public static String getAppFolderPath(Context context) {
        String directoryName = "Sketch Photo";

        String rootPath = context.getFilesDir().getPath() + "/" + directoryName + "/";

        if (new File(rootPath).exists()) {
            return rootPath;
        }

        new File(rootPath).mkdirs();

        return rootPath;
    }

}
