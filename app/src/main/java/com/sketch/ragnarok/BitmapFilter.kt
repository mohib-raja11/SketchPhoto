package com.sketch.ragnarok

import android.graphics.Bitmap
import com.sketch.ragnarok.GrayFilter.changeToGray

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
        if (styleNo == GRAY_STYLE) {
            return changeToGray(bitmap)
        } else if (styleNo == RELIEF_STYLE) {
            return ReliefFilter.changeToRelief(bitmap)
        } else if (styleNo == AVERAGE_BLUR_STYLE) {
            return if (options.size < 1) {
                BlurFilter.changeToAverageBlur(bitmap, 5)
            } else BlurFilter.changeToAverageBlur(
                bitmap, (options[0] as Int?)!!
            )
            // maskSize
        } else if (styleNo == OIL_STYLE) {
            return if (options.size < 1) {
                OilFilter.changeToOil(bitmap, 5)
            } else OilFilter.changeToOil(bitmap, (options[0] as Int?)!!)
        } else if (styleNo == NEON_STYLE) {
            return if (options.size < 3) {
                NeonFilter.changeToNeon(bitmap, 200, 50, 100)
            } else NeonFilter.changeToNeon(
                bitmap, (options[0] as Int?)!!, (options[1] as Int?)!!, (options[2] as Int?)!!
            )
        } else if (styleNo == PIXELATE_STYLE) {
            return if (options.size < 1) {
                PixelateFilter.changeToPixelate(bitmap, 10)
            } else PixelateFilter.changeToPixelate(bitmap, (options[0] as Int?)!!)
        } else if (styleNo == TV_STYLE) {
            return TvFilter.changeToTV(bitmap)
        } else if (styleNo == INVERT_STYLE) {
            return InvertFilter.chageToInvert(bitmap)
        } else if (styleNo == BLOCK_STYLE) {
            return BlockFilter.changeToBrick(bitmap)
        } else if (styleNo == OLD_STYLE) {
            return OldFilter.changeToOld(bitmap)
        } else if (styleNo == SHARPEN_STYLE) {
            return SharpenFilter.changeToSharpen(bitmap)
        } else if (styleNo == LIGHT_STYLE) {
            if (options.size < 3) {
                val width = bitmap.width
                val height = bitmap.height
                return LightFilter.changeToLight(
                    bitmap, width / 2, height / 2, Math.min(width / 2, height / 2)
                )
            }
            return LightFilter.changeToLight(
                bitmap, (options[0] as Int?)!!, (options[1] as Int?)!!, (options[2] as Int?)!!
            ) // centerX, centerY, radius
        } else if (styleNo == LOMO_STYLE) {
            if (options.size < 1) {
                val radius = (bitmap.width / 2 * 95 / 100).toDouble()
                return LomoFilter.changeToLomo(bitmap, radius)
            }
            return LomoFilter.changeToLomo(bitmap, (options[0] as Double?)!!)
        } else if (styleNo == HDR_STYLE) {
            return HDRFilter.changeToHDR(bitmap)
        } else if (styleNo == GAUSSIAN_BLUR_STYLE) {
            return if (options.size < 1) {
                GaussianBlurFilter.changeToGaussianBlur(bitmap, 1.2)
            } else GaussianBlurFilter.changeToGaussianBlur(
                bitmap, (options[0] as Double?)!!
            )
            // sigma
        } else if (styleNo == SOFT_GLOW_STYLE) {
            return if (options.size < 1) {
                SoftGlowFilter.softGlowFilter(bitmap, 0.6)
            } else SoftGlowFilter.softGlowFilter(bitmap, (options[0] as Double?)!!)
        } else if (styleNo == SKETCH_STYLE) {
            return SketchFilter.changeToSketch(bitmap)
        } else if (styleNo == MOTION_BLUR_STYLE) {
            return if (options.size < 2) {
                MotionBlurFilter.changeToMotionBlur(bitmap, 5, 1)
            } else MotionBlurFilter.changeToMotionBlur(
                bitmap, (options[0] as Int?)!!, (options[1] as Int?)!!
            )
        } else if (styleNo == GOTHAM_STYLE) {
            return GothamFilter.changeToGotham(bitmap)
        }
        return bitmap
    }
}