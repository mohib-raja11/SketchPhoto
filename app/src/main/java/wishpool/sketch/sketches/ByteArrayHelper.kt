package wishpool.sketch.sketches

class ByteArrayHelper(private val byteInt: Int, fArr: FloatArray) {

    private val byteArray: FloatArray

    init {
        val i2 = byteInt * 1
        require(fArr.size >= i2) { "small data (is " + fArr.size + " requird is " + i2 }
        byteArray = FloatArray(i2)
        System.arraycopy(fArr, 0, byteArray, 0, i2)
    }

    fun setByteInt(): Int {
        return byteInt
    }

    fun setByteArray(): FloatArray {
        val obj = FloatArray(byteArray.size)
        System.arraycopy(byteArray, 0, obj, 0, byteArray.size)
        return obj
    }
}