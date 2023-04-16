package wishpool.sketch.sketches;

final class IntColorSetting {
    int colorsettingValue1;
    int colorsettingValue2;
    IntColorSetting intColorSetting;
    IntColorSetting[] intColorSettingArray;
    boolean colorSetBoolean;
    int colorsettingValue3;
    int colorsettingValue4;
    int colorsettingValue5;
    int colorsettingValue6;
    int colorsettingValue7;
    final IntColorValueSetting intColorValueSetting;

    IntColorSetting(IntColorValueSetting intColorValueSetting) {
        this.intColorValueSetting = intColorValueSetting;
        this.intColorSettingArray = new IntColorSetting[8];
    }
}
