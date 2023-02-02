package com.sketch.sketches


object BlurFunctionClass : SataticValueInitilizer() {
    @JvmStatic
    fun getByteArrayHelper(f: Float): ByteArrayHelper {
        var i = 0
        val ceilValue = Math.ceil(f.toDouble()).toInt()
        val i2 = ceilValue * 2 + 1
        val fArr = FloatArray(i2)
        val f2 = f / 3.0f
        val f3 = 2.0f * f2 * f2
        val sqrtValue = f2 * 6.2831855f
        val f4 = f * f
        var i3 = 0
        var f5 = 0.0f
        for (i4 in -ceilValue..ceilValue) {
            val f6 = (i4 * i4).toFloat()
            if (f6 > f4) {
                fArr[i3] = 0.0f
            } else {
                fArr[i3] = Math.exp((-f6 / f3).toDouble()).toFloat() / sqrtValue
            }
            f5 += fArr[i3]
            i3++
        }
        while (i < i2) {
            fArr[i] = fArr[i] / f5
            i++
        }
        return ByteArrayHelper(i2, fArr)
    }
}