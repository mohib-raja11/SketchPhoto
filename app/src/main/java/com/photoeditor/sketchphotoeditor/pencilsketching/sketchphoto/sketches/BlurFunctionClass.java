package com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.sketches;

import static java.lang.Math.ceil;

public final class BlurFunctionClass extends SataticValueInitilizer {
    public static ByteArrayHelper getByteArrayHelper(float f) {
        int i = 0;
        int ceilValue = (int) ceil(f);
        int i2 = (ceilValue * 2) + 1;
        float[] fArr = new float[i2];
        float f2 = f / 3.0f;
        float f3 = (2.0f * f2) * f2;
        float sqrtValue = (f2 * 6.2831855f);
        float f4 = f * f;
        int i3 = 0;
        float f5 = 0.0f;
        for (int i4 = -ceilValue; i4 <= ceilValue; i4++) {
            float f6 = (float) (i4 * i4);
            if (f6 > f4) {
                fArr[i3] = 0.0f;
            } else {
                fArr[i3] = ((float) Math.exp((double) ((-f6) / f3))) / sqrtValue;
            }
            f5 += fArr[i3];
            i3++;
        }
        while (i < i2) {
            fArr[i] = fArr[i] / f5;
            i++;
        }
        return new ByteArrayHelper(i2, fArr);
    }


}
