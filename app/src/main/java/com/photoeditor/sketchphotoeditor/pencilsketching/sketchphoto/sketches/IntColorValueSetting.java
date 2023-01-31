package com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.sketches;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.util.Vector;

public final class IntColorValueSetting implements IntValueInterface {
    private IntColorSetting intColorSetting;
    private int intColor1;
    private int intColor2;
    private int intColor3;
    private Vector[] vectorArray;

    public IntColorValueSetting() {
        int i = 0;
        this.intColor3 = 0;
        setIntColorValue(AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY);
        this.vectorArray = new Vector[6];
        while (i < 6) {
            this.vectorArray[i] = new Vector<Object>();
            i++;
        }
        this.intColorSetting = new IntColorSetting(this);
    }

    public final void setIntColorValue(int i) {
        this.intColor2 = i;
        this.intColor1 = Math.max(AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY, i * 2);
    }


    private void setIntColorMethod(int i) {
        for (int i2 = 4; i2 >= 0; i2--) {
            Vector<?> vector = this.vectorArray[i2];
            if (vector != null && vector.size() > 0) {
                for (int i3 = 0; i3 < vector.size(); i3++) {
                    IntColorSetting intColorSetting = (IntColorSetting) vector.elementAt(i3);
                    if (intColorSetting.colorsettingValue1 > 0) {
                        for (int i4 = 0; i4 < 8; i4++) {
                            IntColorSetting intColorset = intColorSetting.intColorSettingArray[i4];
                            if (intColorset != null) {
                                if (!intColorset.colorSetBoolean) {

                                }
                                intColorSetting.colorsettingValue3 += intColorset.colorsettingValue3;
                                intColorSetting.colorsettingValue4 += intColorset.colorsettingValue4;
                                intColorSetting.colorsettingValue5 += intColorset.colorsettingValue5;
                                intColorSetting.colorsettingValue6 += intColorset.colorsettingValue6;
                                intColorSetting.intColorSettingArray[i4] = null;
                                intColorSetting.colorsettingValue1--;
                                this.intColor3--;
                                this.vectorArray[i2 + 1].removeElement(intColorset);
                            }
                        }
                        intColorSetting.colorSetBoolean = true;
                        this.intColor3++;
                        if (this.intColor3 <= i) {
                            return;
                        }
                    }
                }
                continue;
            }
        }

    }


    private int setIntColorMethod2(IntColorSetting intColorSetting, int[] mArray, int i) {
        if (this.intColor3 > this.intColor2) {
            setIntColorMethod(this.intColor2);
        }
        if (intColorSetting.colorSetBoolean) {
            int i2 = intColorSetting.colorsettingValue3;
            mArray[i] = (intColorSetting.colorsettingValue6 / i2) | ((-16777216 | ((intColorSetting.colorsettingValue4 / i2) << 16)) | ((intColorSetting.colorsettingValue5 / i2) << 8));
            i2 = i + 1;
            intColorSetting.colorsettingValue7 = i;
            return i2;
        }
        int i2 = i;
        for (int i3 = 0; i3 < 8; i3++) {
            if (intColorSetting.intColorSettingArray[i3] != null) {
                intColorSetting.colorsettingValue7 = i2;
                i2 = setIntColorMethod2(intColorSetting.intColorSettingArray[i3], mArray, i2);
            }
        }
        return i2;
    }

    @Override
    public void intValueFunction1(int i) {
        // TODO Auto-generated method stub
        for (int i2 = 4; i2 >= 0; i2--) {
            Vector<?> vector = this.vectorArray[i2];
            if (vector != null && vector.size() > 0) {
                for (int i3 = 0; i3 < vector.size(); i3++) {
                    IntColorSetting intColorSetting = (IntColorSetting) vector.elementAt(i3);
                    if (intColorSetting.colorsettingValue1 > 0) {
                        for (int i4 = 0; i4 < 8; i4++) {
                            IntColorSetting intColorset = intColorSetting.intColorSettingArray[i4];
                            if (intColorset != null) {
                                if (!intColorset.colorSetBoolean) {

                                }
                                intColorSetting.colorsettingValue3 += intColorset.colorsettingValue3;
                                intColorSetting.colorsettingValue4 += intColorset.colorsettingValue4;
                                intColorSetting.colorsettingValue5 += intColorset.colorsettingValue5;
                                intColorSetting.colorsettingValue6 += intColorset.colorsettingValue6;
                                intColorSetting.intColorSettingArray[i4] = null;
                                intColorSetting.colorsettingValue1--;
                                this.intColor3--;
                                this.vectorArray[i2 + 1].removeElement(intColorset);
                            }
                        }
                        intColorSetting.colorSetBoolean = true;
                        this.intColor3++;
                        if (this.intColor3 <= i) {
                            return;
                        }
                    }
                }
                continue;
            }
        }

    }

    @Override
    public void intValueFunction2(int[] mArray, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = mArray[i2 + 0];
            int i4 = (i3 >> 16) & MotionEventCompat.ACTION_MASK;
            int i5 = (i3 >> 8) & MotionEventCompat.ACTION_MASK;
            int i6 = i3 & MotionEventCompat.ACTION_MASK;
            int i7 = 0;
            IntColorSetting intColorSetting = this.intColorSetting;
            while (i7 <= 5) {
                IntColorSetting intColorset;
                int i8 = AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS >> i7;
                i3 = (i4 & i8) != 0 ? 4 : 0;
                if ((i5 & i8) != 0) {
                    i3 += 2;
                }
                if ((i8 & i6) != 0) {
                    i3++;
                }
                IntColorSetting colorSet2 = intColorSetting.intColorSettingArray[i3];
                if (colorSet2 == null) {
                    intColorSetting.colorsettingValue1++;
                    colorSet2 = new IntColorSetting(this);
                    colorSet2.intColorSetting = intColorSetting;
                    intColorSetting.intColorSettingArray[i3] = colorSet2;
                    intColorSetting.colorSetBoolean = false;
                    this.vectorArray[i7].addElement(colorSet2);
                    if (i7 == 5) {
                        colorSet2.colorSetBoolean = true;
                        colorSet2.colorsettingValue3 = 1;
                        colorSet2.colorsettingValue4 = i4;
                        colorSet2.colorsettingValue5 = i5;
                        colorSet2.colorsettingValue6 = i6;
                        colorSet2.colorsettingValue2 = i7;
                        this.intColor3++;
                        break;
                    }
                    intColorset = colorSet2;
                } else if (colorSet2.colorSetBoolean) {
                    colorSet2.colorsettingValue3++;
                    colorSet2.colorsettingValue4 += i4;
                    colorSet2.colorsettingValue5 += i5;
                    colorSet2.colorsettingValue6 += i6;
                    break;
                } else {
                    intColorset = colorSet2;
                }
                i7++;
                intColorSetting = intColorset;
            }

            if (this.intColor3 > this.intColor1) {
                setIntColorMethod(this.intColor1);
            }
        }
    }

    @Override
    public int[] intArrayValueFunction() {
        int[] mArray = new int[this.intColor3];
        setIntColorMethod2(this.intColorSetting, mArray, 0);
        return mArray;
    }

    @Override
    public int intValueFunction3(int i) {
        int i2 = (i >> 16) & MotionEventCompat.ACTION_MASK;
        int i3 = (i >> 8) & MotionEventCompat.ACTION_MASK;
        int i4 = i & MotionEventCompat.ACTION_MASK;
        int i5 = 0;
        IntColorSetting intColorSetting = this.intColorSetting;
        while (i5 <= 5) {
            int i6;
            int i7 = AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS >> i5;
            if ((i2 & i7) != 0) {
                i6 = 4;
            } else {
                i6 = 0;
            }
            if ((i3 & i7) != 0) {
                i6 += 2;
            }
            if ((i7 & i4) != 0) {
                i6++;
            }
            IntColorSetting intColorset = intColorSetting.intColorSettingArray[i6];
            if (intColorset == null) {
                return intColorSetting.colorsettingValue7;
            }
            if (intColorset.colorSetBoolean) {
                return intColorset.colorsettingValue7;
            }
            i5++;
            intColorSetting = intColorset;
        }

        return 0;
    }
}
