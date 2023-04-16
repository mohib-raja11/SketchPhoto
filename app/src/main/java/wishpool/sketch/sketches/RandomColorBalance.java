package wishpool.sketch.sketches;

import android.graphics.Color;

import androidx.core.view.MotionEventCompat;

import java.util.Random;

public final class RandomColorBalance {
    static {
        new Random();
    }

    public static int getActionMask(int i) {
        if (i < 0) {
            return 0;
        }
        if (i > MotionEventCompat.ACTION_MASK) {
            return MotionEventCompat.ACTION_MASK;
        }
        return i;
    }

    public static int getActionColor(int i, int i2) {
        int i3 = (i >> 24) & MotionEventCompat.ACTION_MASK;
        int i4 = (i >> 8) & MotionEventCompat.ACTION_MASK;
        int i5 = i & MotionEventCompat.ACTION_MASK;
        int i6 = (i2 >> 24) & MotionEventCompat.ACTION_MASK;
        int i7 = (i2 >> 16) & MotionEventCompat.ACTION_MASK;
        int i8 = (i2 >> 8) & MotionEventCompat.ACTION_MASK;
        int i9 = i2 & MotionEventCompat.ACTION_MASK;
        int min = Math.min((i >> 16) & MotionEventCompat.ACTION_MASK, i7);
        int min2 = Math.min(i4, i8);
        i4 = Math.min(i5, i9);
        if (i3 != MotionEventCompat.ACTION_MASK) {
            i3 = (i3 * MotionEventCompat.ACTION_MASK) / MotionEventCompat.ACTION_MASK;
            i5 = ((255 - i3) * i6) / MotionEventCompat.ACTION_MASK;
            min = RandomColorBalance.getActionMask(((min * i3) + (i7 * i5)) / MotionEventCompat.ACTION_MASK);
            min2 = RandomColorBalance.getActionMask(((min2 * i3) + (i8 * i5)) / MotionEventCompat.ACTION_MASK);
            i4 = RandomColorBalance.getActionMask(((i4 * i3) + (i9 * i5)) / MotionEventCompat.ACTION_MASK);
            i3 = RandomColorBalance.getActionMask(i3 + i5);
        }
        return ((min2 << 8) | ((min << 16) | (i3 << 24))) | i4;
    }

    public static void getColorRGB(int[] mArray, int[] mArray2, int i, int i2) {
        for (int i3 = 0; i3 < i; i3++) {
            for (int i4 = 0; i4 < i2; i4++) {
                int i5 = mArray[(i4 * i) + i3];
                int i6 = mArray2[(i4 * i) + i3];
                int red = Color.red(i5);
                int green = Color.green(i5);
                i5 = Color.blue(i5);
                int red2 = Color.red(i6);
                int green2 = Color.green(i6);
                i6 = Color.blue(i6);
                mArray2[(i4 * i) + i3] = Color.argb(MotionEventCompat.ACTION_MASK, RandomColorBalance.getRandColorInt(red, red2), RandomColorBalance.getRandColorInt(green, green2), RandomColorBalance.getRandColorInt(i5, i6));
            }
        }
    }

    private static int getRandColorInt(int i, int i2) {
        int i3 = ((i * i2) / (256 - i2)) + i;
        if (i3 > MotionEventCompat.ACTION_MASK) {
            return MotionEventCompat.ACTION_MASK;
        }
        return i3;
    }
}
