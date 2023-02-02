package com.sketch.sketches

open class SataticValueInitilizer : ImageFilerName() {

    companion object {
        var staticValue1 = 0
        var staticValue2 = 0
        var staticValue3 = 0

        init {
            staticValue1 = 0
            staticValue2 = 1
            staticValue3 = 2
        }
    }
}