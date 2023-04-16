package wishpool.sketch.ragnarok

import android.graphics.Bitmap
import wishpool.sketch.ragnarok.GrayFilter.changeToGray

object BitmapFilter {

    private const val GRAY_STYLE = 1 // gray scale
    private const val RELIEF_STYLE = 2 // relief
    private const val AVERAGE_BLUR_STYLE = 3 // average blur
    const val OIL_STYLE = 4 // oil painting
    private const val NEON_STYLE = 5 // neon
    private const val PIXELATE_STYLE = 6 // pixelate
    private const val TV_STYLE = 7 // Old TV
    private const val INVERT_STYLE = 8 // invert the colors
    private const val BLOCK_STYLE = 9 // engraving
    private const val OLD_STYLE = 10 // old photo
    private const val SHARPEN_STYLE = 11 // sharpen
    private const val LIGHT_STYLE = 12 // light
    private const val LOMO_STYLE = 13 // lomo
    private const val HDR_STYLE = 14 // HDR
    private const val GAUSSIAN_BLUR_STYLE = 15 // gaussian blur
    private const val SOFT_GLOW_STYLE = 16 // soft glow
    const val SKETCH_STYLE = 17 // sketch style
    private const val MOTION_BLUR_STYLE = 18 // motion blur
    private const val GOTHAM_STYLE = 19 // gotham style


    fun changeStyle(bitmap: Bitmap, styleNo: Int, vararg options: Any?): Bitmap {
        if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.GRAY_STYLE) {
            return changeToGray(bitmap)
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.RELIEF_STYLE) {
            return wishpool.sketch.ragnarok.ReliefFilter.changeToRelief(bitmap)
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.AVERAGE_BLUR_STYLE) {
            return if (options.size < 1) {
                wishpool.sketch.ragnarok.BlurFilter.changeToAverageBlur(bitmap, 5)
            } else wishpool.sketch.ragnarok.BlurFilter.changeToAverageBlur(
                bitmap, (options[0] as Int?)!!
            )
            // maskSize
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.OIL_STYLE) {
            return if (options.size < 1) {
                wishpool.sketch.ragnarok.OilFilter.changeToOil(bitmap, 5)
            } else wishpool.sketch.ragnarok.OilFilter.changeToOil(bitmap, (options[0] as Int?)!!)
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.NEON_STYLE) {
            return if (options.size < 3) {
                wishpool.sketch.ragnarok.NeonFilter.changeToNeon(bitmap, 200, 50, 100)
            } else wishpool.sketch.ragnarok.NeonFilter.changeToNeon(
                bitmap, (options[0] as Int?)!!, (options[1] as Int?)!!, (options[2] as Int?)!!
            )
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.PIXELATE_STYLE) {
            return if (options.size < 1) {
                wishpool.sketch.ragnarok.PixelateFilter.changeToPixelate(bitmap, 10)
            } else wishpool.sketch.ragnarok.PixelateFilter.changeToPixelate(
                bitmap,
                (options[0] as Int?)!!
            )
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.TV_STYLE) {
            return wishpool.sketch.ragnarok.TvFilter.changeToTV(bitmap)
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.INVERT_STYLE) {
            return wishpool.sketch.ragnarok.InvertFilter.chageToInvert(bitmap)
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.BLOCK_STYLE) {
            return wishpool.sketch.ragnarok.BlockFilter.changeToBrick(bitmap)
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.OLD_STYLE) {
            return wishpool.sketch.ragnarok.OldFilter.changeToOld(bitmap)
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.SHARPEN_STYLE) {
            return wishpool.sketch.ragnarok.SharpenFilter.changeToSharpen(bitmap)
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.LIGHT_STYLE) {
            if (options.size < 3) {
                val width = bitmap.width
                val height = bitmap.height
                return wishpool.sketch.ragnarok.LightFilter.changeToLight(
                    bitmap, width / 2, height / 2, Math.min(width / 2, height / 2)
                )
            }
            return wishpool.sketch.ragnarok.LightFilter.changeToLight(
                bitmap, (options[0] as Int?)!!, (options[1] as Int?)!!, (options[2] as Int?)!!
            ) // centerX, centerY, radius
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.LOMO_STYLE) {
            if (options.size < 1) {
                val radius = (bitmap.width / 2 * 95 / 100).toDouble()
                return wishpool.sketch.ragnarok.LomoFilter.changeToLomo(bitmap, radius)
            }
            return wishpool.sketch.ragnarok.LomoFilter.changeToLomo(
                bitmap,
                (options[0] as Double?)!!
            )
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.HDR_STYLE) {
            return wishpool.sketch.ragnarok.HDRFilter.changeToHDR(bitmap)
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.GAUSSIAN_BLUR_STYLE) {
            return if (options.size < 1) {
                wishpool.sketch.ragnarok.GaussianBlurFilter.changeToGaussianBlur(bitmap, 1.2)
            } else wishpool.sketch.ragnarok.GaussianBlurFilter.changeToGaussianBlur(
                bitmap, (options[0] as Double?)!!
            )
            // sigma
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.SOFT_GLOW_STYLE) {
            return if (options.size < 1) {
                wishpool.sketch.ragnarok.SoftGlowFilter.softGlowFilter(bitmap, 0.6)
            } else wishpool.sketch.ragnarok.SoftGlowFilter.softGlowFilter(
                bitmap,
                (options[0] as Double?)!!
            )
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.SKETCH_STYLE) {
            return wishpool.sketch.ragnarok.SketchFilter.changeToSketch(bitmap)
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.MOTION_BLUR_STYLE) {
            return if (options.size < 2) {
                wishpool.sketch.ragnarok.MotionBlurFilter.changeToMotionBlur(bitmap, 5, 1)
            } else wishpool.sketch.ragnarok.MotionBlurFilter.changeToMotionBlur(
                bitmap, (options[0] as Int?)!!, (options[1] as Int?)!!
            )
        } else if (styleNo == wishpool.sketch.ragnarok.BitmapFilter.GOTHAM_STYLE) {
            return wishpool.sketch.ragnarok.GothamFilter.changeToGotham(bitmap)
        }
        return bitmap
    }
}