package com.sketch.sketches;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import androidx.core.view.MotionEventCompat;

public final class ColorLevels extends ImageFilerName {

    private int[][] colorLevelArray;

    public ColorLevels() {
    }

    public Bitmap getColorLevelBitmap(Bitmap bitmap) {
        int i;
        int i2;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] mArray = new int[(width * height)];
        bitmap.getPixels(mArray, 0, width, 0, 0, width, height);
        System.currentTimeMillis();

        i2 = 0;
        int i3 = 0;
        while (i3 < height) {
            i = i2;
            for (i2 = 0; i2 < width; i2++) {
                int i4 = mArray[i];
                if (this.colorLevelArray != null) {
                    i4 = this.colorLevelArray[2][i4 & MotionEventCompat.ACTION_MASK] | (((-16777216 & i4) | (this.colorLevelArray[0][(i4 >> 16) & MotionEventCompat.ACTION_MASK] << 16)) | (this.colorLevelArray[1][(i4 >> 8) & MotionEventCompat.ACTION_MASK] << 8));
                }
                mArray[i] = i4;
                i++;
            }
            i3++;
            i2 = i;
        }
        this.colorLevelArray = null;

        Bitmap createBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        if (createBitmap == null) {
            return bitmap;
        }
        createBitmap.setPixels(mArray, 0, width, 0, 0, width, height);
        bitmap.recycle();
        System.gc();
        return createBitmap;
    }

}
