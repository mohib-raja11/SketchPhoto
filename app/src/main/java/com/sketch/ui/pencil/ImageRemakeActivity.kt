package com.sketch.ui.pencil

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.exifinterface.media.ExifInterface
import com.sketch.*
import com.sketch.databinding.*
import com.sketch.ragnarok.BitmapFilter
import com.sketch.sketch_util.CSketchFilter
import com.sketch.sketch_util.SketchColorFilter2
import com.sketch.sketch_util.SketchFilter
import com.sketch.sketch_util.SketchFilter2
import com.sketch.sketches.SecondSketchFilter
import com.sketch.ui.BaseActivity
import com.sketch.ui.PhotoShare_Activity
import com.sketch.utils.AppUtils
import com.sketch.utils.toast
import java.io.*
import java.nio.IntBuffer
import java.util.*

class ImageRemakeActivity : BaseActivity() {

    companion object {
        var pic_result: Bitmap? = null
        var pic_forSketch: Bitmap? = null
        var pic_forDraw: Bitmap? = null
    }

    private var MaxResolution = 0
    private var imageheight = 0
    private var imagewidth = 0

    private var colorPencilBitmap: Bitmap? = null
    private var colorPencil2Bitmap: Bitmap? = null

    private var pencilsketchBitmap: Bitmap? = null
    private var pencil2Bitmap: Bitmap? = null
    private var simpleSketchbitmap1: Bitmap? = null
    private var simpleSketchbitmap2: Bitmap? = null
    private var comicBitmap: Bitmap? = null

    private var Imagepath: String? = null
    private var sendimagepath: String = ""

    private var Orientation: Float? = null

    private lateinit var anim_bottom_show: Animation
    private lateinit var anim_btnapply: Animation
    private lateinit var animhidebtn: Animation
    private lateinit var animsgallerybtn: Animation
    private lateinit var animshowbtndown: Animation
    private lateinit var animshowbtnup: Animation

    private var MoveBack = false
    private var moveforword = true
    private var lineOne = true
    private var sketchFirstTimeDone = false

    private lateinit var binding: ActivityImageRemakeBinding


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageRemakeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val displaymetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displaymetrics)
        MaxResolution = displaymetrics.widthPixels

        initializeViews()
    }

    private fun initializeViews() {

        binding.apply {

            progressView.visibility = View.GONE
            viewGallery.setVisibility(View.VISIBLE)

            picCropImageView.setGuidelines(1)
            picCropImageView.setImageResource(0)

            picCropImageView.setVisibility(View.GONE)
            effectsHolderLayout.setVisibility(View.GONE)
            cropOptionsHolderLayout.setVisibility(View.GONE)

            tvEditor.setText(R.string.editor)

            doneEditLayoutBtn.setOnClickListener {

                saveBitmap(UUID.randomUUID().toString(), 100, pic_result)

                val file = File(sendimagepath)
                if (file.exists()) {
                    val uri = Uri.fromFile(file)
                    val intent = Intent(this@ImageRemakeActivity, PhotoShare_Activity::class.java)
                    intent.data = uri
                    startActivity(intent)
                }
            }

            picCropViewDone.setOnClickListener {

                if (cropOptionsHolderLayout.visibility == View.VISIBLE) {

                    val bitmap = picCropImageView.croppedImage

                    pic_result = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                    pic_forSketch = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                    pic_forDraw = bitmap.copy(Bitmap.Config.ARGB_8888, true)

                    cropOptionsHolderLayout.visibility = View.GONE
                    picCropImageView.visibility = View.GONE

                    picCropImageView.imageResource = 0
                    ivImageMaker.visibility = View.VISIBLE
                    ivImageMaker.setImageBitmap(pic_result)

                    sketchAsnyTaskFirst()
                }

                if (effectsHolderLayout.visibility == View.VISIBLE) {
                    effectsHolderLayout.visibility = View.GONE
                }

                AnimationviewTop(doneEditLayoutBtn, picCropViewDone, 9)
                viewGallery.startAnimation(anim_bottom_show)
                viewGallery.visibility = View.VISIBLE
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.pic_eff_image)
                setIcon_Effects()

                if (bitmap != null && !bitmap.isRecycled) {
                    bitmap.recycle()
                    System.gc()
                }

                checkcropIV()

                Animationview(viewGallery, effectsHolderLayout)
                AnimationviewTop(doneEditLayoutBtn, picCropViewDone, 1)
            }

        }

        animhidebtn = AnimationUtils.loadAnimation(this, R.anim.hide_button_anims)
        animsgallerybtn = AnimationUtils.loadAnimation(this, R.anim.rightleft_gallery_anims)
        animshowbtnup = AnimationUtils.loadAnimation(this, R.anim.show_button_anims_up)
        anim_btnapply = AnimationUtils.loadAnimation(this, R.anim.show_button_anims_up)
        animshowbtndown = AnimationUtils.loadAnimation(this, R.anim.show_button_anims_down)
        anim_bottom_show = AnimationUtils.loadAnimation(this, R.anim.hide_button_anims_up)

        loadImageAsycTask()

    }

    private fun getAspectRatio(s: String, f: Float) {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(s, options)
        val f1 = options.outWidth.toFloat() / options.outHeight.toFloat()
        val f2: Float
        val f3: Float
        if (f1 > 1.0f) {
            f3 = f
            f2 = f3 / f1
        } else {
            f2 = f
            f3 = f2 * f1
        }
        imagewidth = f3.toInt()
        imageheight = f2.toInt()
    }

    fun getResizedOriginalBitmap(s: String?, f2: Float): Bitmap? {

        val options: BitmapFactory.Options
        var i: Int
        var j: Int
        val k: Int
        val l: Int
        var i1: Int
        var f: Float
        var f1: Float
        var bitmap: Bitmap?
        var matrix: Matrix
        try {

            options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(FileInputStream(s), null, options)
            i = options.outWidth
            j = options.outHeight
            k = imagewidth
            l = imageheight

        } catch (filenotfoundexception: FileNotFoundException) {
            return null
        }

        i1 = 1
        do {
            if (i / 2 <= k) {
                f = k.toFloat() / i.toFloat()
                f1 = l.toFloat() / j.toFloat()
                options.inJustDecodeBounds = false
                options.inDither = false
                options.inSampleSize = i1
                options.inScaled = false
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                try {
                    bitmap = BitmapFactory.decodeStream(FileInputStream(s), null, options)
                    matrix = Matrix()
                    matrix.postScale(f, f1)
                    matrix.postRotate(f2)
                    return Bitmap.createBitmap(
                        bitmap!!, 0, 0, bitmap.width, bitmap.height, matrix, true
                    )
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
            i /= 2
            j /= 2
            i1 *= 2
        } while (true)
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        return if (cursor == null) {
            uri.path
        } else {
            cursor.moveToFirst()
            @SuppressLint("Range") val s = cursor.getString(cursor.getColumnIndex("_data"))
            cursor.close()
            s
        }
    }

    private fun getImageOrientation(s: String): Float {
        val i: Int
        val f: Float
        i = try {
            ExifInterface(s).getAttributeInt("Orientation", 1)
        } catch (ioexception: IOException) {
            ioexception.printStackTrace()
            return 0.0f
        }
        if (i == 6) {
            f = 90f
        } else {
            if (i == 3) {
                return 180f
            }
            f = 0.0f
            if (i == 8) {
                return 270f
            }
        }
        return f
    }

    @SuppressLint("ResourceType")
    private fun stringMatching() {

        val viewBinding = PicBtnLayoutBinding.inflate(layoutInflater)
        viewBinding.apply {

            btnImage.setOnClickListener(this@ImageRemakeActivity)
            btnTxt.setOnClickListener(this@ImageRemakeActivity)

            btnTxt.text = getString(R.string.edt_crop)
            btnImage.setImageResource(R.drawable.pic_crop)

            btnImage.id = 2
            btnTxt.id = 2

            setIcon_Crop()

            binding.viewGallery.addView(root)

        }

    }

    private fun setIcon_Crop() {

        binding.apply {

            ivImageMaker.visibility = View.GONE
            picCropImageView.setImageBitmap(pic_result)
            picCropImageView.visibility = View.VISIBLE
            Animationview(viewGallery, cropOptionsHolderLayout)
            AnimationviewTop(picCropViewDone, doneEditLayoutBtn, 2)

            val croppStyles = arrayOf(
                "custom",
                "1:1",
                "2:1",
                "1:2",
                "3:2",
                "2:3",
                "4:3",
                "4:6",
                "4:5",
                "5:6",
                "5:7",
                "9:16",
                "16:9"
            )

            croppStyles.forEachIndexed { index, crop ->
                run {

                    val picCropViewBinding = PicCropItemViewBinding.inflate(layoutInflater)

                    picCropViewBinding.apply {

                        tvCrop.id = index
                        tvCrop.text = croppStyles[index]

                        cropOptionsHolderLayout.addView(root)

                        tvCrop.setOnClickListener {
                            when (tvCrop.id) {
                                0 -> {
                                    picCropImageView.setFixedAspectRatio(false)
                                    return@setOnClickListener
                                }
                                1 -> {
                                    picCropImageView.setFixedAspectRatio(true)
                                    picCropImageView.setAspectRatio(1, 1)
                                    return@setOnClickListener
                                }
                                2 -> {
                                    picCropImageView.setFixedAspectRatio(true)
                                    picCropImageView.setAspectRatio(2, 1)
                                    return@setOnClickListener
                                }
                                3 -> {
                                    picCropImageView.setFixedAspectRatio(true)
                                    picCropImageView.setAspectRatio(1, 2)
                                    return@setOnClickListener
                                }
                                4 -> {
                                    picCropImageView.setFixedAspectRatio(true)
                                    picCropImageView.setAspectRatio(3, 2)
                                    return@setOnClickListener
                                }
                                5 -> {
                                    picCropImageView.setFixedAspectRatio(true)
                                    picCropImageView.setAspectRatio(2, 3)
                                    return@setOnClickListener
                                }
                                6 -> {
                                    picCropImageView.setFixedAspectRatio(true)
                                    picCropImageView.setAspectRatio(4, 3)
                                    return@setOnClickListener
                                }
                                7 -> {
                                    picCropImageView.setFixedAspectRatio(true)
                                    picCropImageView.setAspectRatio(4, 6)
                                    return@setOnClickListener
                                }
                                8 -> {
                                    picCropImageView.setFixedAspectRatio(true)
                                    picCropImageView.setAspectRatio(4, 5)
                                    return@setOnClickListener
                                }
                                9 -> {
                                    picCropImageView.setFixedAspectRatio(true)
                                    picCropImageView.setAspectRatio(5, 6)
                                    return@setOnClickListener
                                }
                                10 -> {
                                    picCropImageView.setFixedAspectRatio(true)
                                    picCropImageView.setAspectRatio(5, 7)
                                    return@setOnClickListener
                                }
                                11 -> {
                                    picCropImageView.setFixedAspectRatio(true)
                                    picCropImageView.setAspectRatio(8, 10)
                                    return@setOnClickListener
                                }
                                12 -> picCropImageView.setFixedAspectRatio(true)
                                else -> return@setOnClickListener
                            }
                            picCropImageView.setAspectRatio(16, 9)
                        }


                    }

                }
            }
        }
    }


    data class EffectItem(val name: String, val icon: Int)

    private fun setIcon_Effects() {

        val effectsArray = arrayListOf<EffectItem>()
        effectsArray.add(EffectItem("Color", R.drawable.pic_eff_0))
        effectsArray.add(EffectItem("Pencil 1", R.drawable.pic_eff_1))
        effectsArray.add(EffectItem("Color 2", R.drawable.pic_eff_2))
        effectsArray.add(EffectItem("Pencil 2", R.drawable.pic_eff_3))
        effectsArray.add(EffectItem("Pencil 3", R.drawable.pic_eff_4))
        effectsArray.add(EffectItem("Pencil 4", R.drawable.pic_eff_5))
        effectsArray.add(EffectItem("Pencil 5", R.drawable.pic_eff_6))
        effectsArray.add(EffectItem("Sepia", R.drawable.pic_eff_7))



        effectsArray.forEachIndexed() { index, _ ->

            run {
                val picEffectViewBinding = PicEffectLayoutBinding.inflate(layoutInflater)

                picEffectViewBinding.apply {

                    image.id = index
                    image.layoutParams = LinearLayout.LayoutParams(-2, -2)
                    val bitmap = BitmapFactory.decodeResource(resources, effectsArray[index].icon)

                    txtView.text = effectsArray[index].name

                    image.setImageBitmap(bitmap)
                    binding.effectsHolderLayout.addView(root)
                    image.setOnClickListener {

                        Log.d("clickedId", "onClick: " + image.id)

                        when (image.id) {
                            0, 1, 2, 3, 4, 5, 6, 7 -> if (sketchFirstTimeDone) {
                                sketchAsnyTask(image.id)
                            }
                            else -> if (sketchFirstTimeDone) {
                                sketchAsnyTask(image.id)
                            }
                        }
                    }

                }
            }
        }
    }

    override fun onBackPressed() {
        ExitDidalog(getString(R.string.pic_exit_txt))
    }

    fun Animationview(hideanimview: View?, showanimview: View?) {
        hideanimview!!.startAnimation(animhidebtn)
        animhidebtn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation) {
                hideanimview.visibility = View.GONE
                showanimview!!.startAnimation(animsgallerybtn)
                showanimview.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationStart(animation: Animation) {
                hideanimview.visibility = View.VISIBLE
            }
        })
    }

    fun AnimationviewTop(showanimview: View?, hideanimview: View?, Btnid: Int) {
        hideanimview!!.startAnimation(animshowbtnup)
        binding.tvEditor.startAnimation(animshowbtnup)
        animshowbtnup.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation) {

                binding.tvEditor.startAnimation(animshowbtndown)

                hideanimview.visibility = View.GONE
                showanimview!!.visibility = View.VISIBLE
                showanimview.startAnimation(animshowbtndown)
                binding.apply {
                    when (Btnid) {
                        1 -> {
                            tvEditor.text = getString(R.string.edt_effect)
                            return
                        }
                        2 -> {
                            tvEditor.text = getString(R.string.edt_crop)
                            return
                        }
                        3 -> {
                            tvEditor.text = getString(R.string.edt_vintage)
                            return
                        }
                        4 -> {
                            tvEditor.text = getString(R.string.edt_frame)
                            return
                        }
                        5 -> {
                            tvEditor.text = getString(R.string.edt_overlay)
                            return
                        }
                        6 -> {
                            tvEditor.text = getString(R.string.edt_reset)
                            return
                        }
                        7 -> {
                            tvEditor.text = getString(R.string.edt_border)
                            return
                        }
                        8 -> {
                            tvEditor.text = getString(R.string.edt_orientation)
                            return
                        }
                        9 -> tvEditor.text = getString(R.string.edt_editor)
                        else -> {
                            tvEditor.text = getString(R.string.edt_editor)
                            return
                        }
                    }

                }
            }

            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationStart(animation: Animation) {}
        })
    }

    fun ExitDidalog(s: String?) {
        val exit_dialog = Dialog(this)
        exit_dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        val exitDialogBinding = PicResetDialogBinding.inflate(layoutInflater)

        exitDialogBinding.apply {
            exit_dialog.setContentView(root)

            picResetTxt.text = s

            picResetTxt.text = getString(R.string.leave_edt)

            picDialogYes.text = getString(R.string.keep_edt)
            picDialogYes.setOnClickListener {
                exit_dialog.dismiss()
                val intent = Intent()
                setResult(RESULT_CANCELED, intent)
                finish()
            }
            picDialogNo.setOnClickListener { exit_dialog.dismiss() }
            exit_dialog.show()
        }

    }

    private fun checkcropIV() {

        binding.apply {
            if (picCropImageView.visibility == View.VISIBLE) {
                picCropImageView.visibility = View.GONE
                ivImageMaker.visibility = View.VISIBLE
            }
            if (cropOptionsHolderLayout.visibility == View.VISIBLE) {
                cropOptionsHolderLayout.visibility = View.GONE
            }
            if (effectsHolderLayout.visibility == View.VISIBLE) {
                effectsHolderLayout.visibility = View.GONE
            }
        }

    }

    fun PicMakerDidalog(s: String?) {
        val pic_maker_dialog = Dialog(this)
        pic_maker_dialog.window!!.setBackgroundDrawable(ColorDrawable(0))

        val picResetDialogBinding = PicResetDialogBinding.inflate(layoutInflater)

        picResetDialogBinding.apply {

            pic_maker_dialog.setContentView(root)

            picResetTxt.text = s

            picDialogYes.text = getString(R.string.reset_edt)
            picDialogNo.text = getString(R.string.continue_edt)
            picDialogYes.setOnClickListener { view: View? ->
                val bitmap = pic_result
                if (Imagepath != null) {
                    pic_maker_dialog.dismiss()
                    Orientation = getImageOrientation(Imagepath!!)
                    getAspectRatio(Imagepath!!, MaxResolution.toFloat())
                    pic_result = getResizedOriginalBitmap(Imagepath, Orientation!!)
                    binding.ivImageMaker.setImageBitmap(pic_result)

                    mContext.toast("Your original image is back !!!")

                    if (bitmap != null && !bitmap.isRecycled) {
                        bitmap.recycle()
                        System.gc()
                    }
                } else {

                    mContext.toast("Invalid image path.")
                }
            }
            picDialogNo.setOnClickListener { view: View? -> pic_maker_dialog.dismiss() }
            pic_maker_dialog.show()
        }

    }

    override fun onClick(view: View) {

        binding.apply {

            val viewID = view.id

            if (viewID == 2) {
                if (effectsHolderLayout.visibility == View.VISIBLE) {
                    effectsHolderLayout.visibility = View.GONE
                }
            }
            if (viewID == 8) {
                checkcropIV()
                AnimationviewTop(picCropViewDone, doneEditLayoutBtn, 8)
            }
            if (viewID == 1) {
                checkcropIV()

                Animationview(viewGallery, effectsHolderLayout)
                AnimationviewTop(picCropViewDone, doneEditLayoutBtn, 1)
            }
            if (viewID == 3) {
                checkcropIV()

                AnimationviewTop(picCropViewDone, doneEditLayoutBtn, 3)
            }
            if (viewID == 4) {
                checkcropIV()

                AnimationviewTop(picCropViewDone, doneEditLayoutBtn, 4)
            }
            if (viewID == 5) {
                checkcropIV()

                AnimationviewTop(picCropViewDone, doneEditLayoutBtn, 5)
                return
            }
            if (viewID == 7) {
                checkcropIV()
            }
            if (viewID == 6) {
                checkcropIV()
                PicMakerDidalog("You are loosing your edited image.Do you want to reset?")
            }
        }


    }

    public override fun onDestroy() {
        super.onDestroy()

        if (GPUImageFilterTools.overlayBmp != null && !GPUImageFilterTools.overlayBmp.isRecycled) {
            GPUImageFilterTools.overlayBmp.recycle()
            GPUImageFilterTools.overlayBmp = null
            System.gc()
        }

        if (pic_result != null && !pic_result!!.isRecycled) {
            pic_result!!.recycle()
            pic_result = null
            System.gc()
        }

        animhidebtn.cancel()
        animsgallerybtn.cancel()
        animshowbtnup.cancel()
        animshowbtndown.cancel()
        anim_bottom_show.cancel()
        anim_btnapply.cancel()

        unbindDrawables(binding.mainLayout)
    }

    private fun unbindDrawables(view: View) {
        try {
            if (view.background != null) {
                view.background.callback = null
            }
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    unbindDrawables(view.getChildAt(i))
                }
                view.removeAllViews()
            }
        } catch (e: Exception) {
            Log.e(tag, "unbindDrawables: ${e.message}")
        }
    }

    fun saveBitmap(s: String?, i: Int, bitmap: Bitmap?) {
        val rootFolder = AppUtils.getAppFolderPath(this)

        val flag: Boolean
        BitmapFactory.Options().inSampleSize = 5
        sendimagepath =
            StringBuilder(rootFolder).append(File.separator).append(s).append(".jpg").toString()

        val file = File(sendimagepath)

        flag = file.exists()
        var `as`: Array<String?>? = null

        try {
            if (flag) {
                file.delete()
                file.createNewFile()
            } else {
                file.createNewFile()
            }
            val fileoutputstream = FileOutputStream(file)
            val bufferedoutputstream = BufferedOutputStream(fileoutputstream)
            bitmap!!.compress(Bitmap.CompressFormat.PNG, i, bufferedoutputstream)
            bufferedoutputstream.flush()
            bufferedoutputstream.close()
            `as` = arrayOfNulls(1)
            `as`[0] = file.toString()

        } catch (nullpointerexception: NullPointerException) {
            Log.e("TAG", "saveBitmap: NullPointerException = " + nullpointerexception.message)
        } catch (e: IOException) {
            Log.e("TAG", "saveBitmap: IOException = " + e.message)
        }

        MediaScannerConnection.scanFile(this, `as`, null) { _: String?, _: Uri? -> }
    }

    fun getSketchBitmap(bm1: Bitmap?, type: Int): Bitmap? {

        var sketchBitmap = bm1

        when (type) {
            0 -> {
                val sketchColorFilter2 = SketchColorFilter2(this@ImageRemakeActivity)

                if (colorPencil2Bitmap == null) {
                    try {
                        colorPencil2Bitmap = sketchColorFilter2.getSketchFromBH(bm1!!)
                        sketchBitmap = colorPencil2Bitmap
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                } else {
                    sketchBitmap = colorPencil2Bitmap
                }
            }
            1 -> {
                val sketchFilter = SketchFilter(
                    this@ImageRemakeActivity
                )
                if (pencilsketchBitmap == null) {
                    try {
                        pencilsketchBitmap = sketchFilter.getSketchFromBH(bm1!!)
                        sketchBitmap = pencilsketchBitmap
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                } else {
                    sketchBitmap = pencilsketchBitmap
                }
            }
            2 -> {
                val printFilter = SketchColorFilter(this@ImageRemakeActivity)
                if (colorPencilBitmap == null) {
                    try {
                        colorPencilBitmap = printFilter.getSketchFromBH(bm1)
                        sketchBitmap = colorPencilBitmap
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                } else {
                    sketchBitmap = colorPencilBitmap
                }
            }
            3 -> {
                val sketchFilter2 = SketchFilter2(
                    this@ImageRemakeActivity
                )
                if (pencil2Bitmap == null) {
                    try {
                        pencil2Bitmap = sketchFilter2.getSketchFromBH(bm1!!)
                        sketchBitmap = pencil2Bitmap
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                } else {
                    sketchBitmap = pencil2Bitmap
                }
            }
            4 -> {
                val cSketchFilter = CSketchFilter(
                    this@ImageRemakeActivity
                )
                if (comicBitmap == null) {
                    try {
                        comicBitmap = cSketchFilter.getSketchFromBH(bm1!!)
                        sketchBitmap = comicBitmap
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                } else {
                    sketchBitmap = comicBitmap
                }
            }
            5 -> if (simpleSketchbitmap1 == null) {
                try {
                    val greyScaleCoppy = toGrayscale(bm1)
                    val invertCopy = invert(greyScaleCoppy)
                    val blurImage = fastblur(invertCopy, 7)
                    if (!invertCopy.isRecycled) {
                        invertCopy.recycle()
                        System.gc()
                    }
                    simpleSketchbitmap1 = ColorDodgeBlend(blurImage, greyScaleCoppy)
                    if (!greyScaleCoppy.isRecycled) {
                        greyScaleCoppy.recycle()
                        System.gc()
                    }
                    if (!blurImage!!.isRecycled) {
                        blurImage.recycle()
                        System.gc()
                    }
                    simpleSketchbitmap1 = toGrayscale(simpleSketchbitmap1)
                    sketchBitmap = simpleSketchbitmap1
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            } else {
                sketchBitmap = simpleSketchbitmap1
            }
            6 -> {
                val secondSketchFilter = SecondSketchFilter()
                if (simpleSketchbitmap2 == null) {
                    try {
                        simpleSketchbitmap2 = secondSketchFilter.getSimpleSketch(bm1!!)
                        sketchBitmap = simpleSketchbitmap2
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                } else {
                    sketchBitmap = simpleSketchbitmap2
                }
            }
            7 -> if (simpleSketchbitmap2 == null) {
                try {
                    val secondSketchFilter1 = SecondSketchFilter()
                    simpleSketchbitmap2 = secondSketchFilter1.getSimpleSketch(bm1!!)
                    sketchBitmap = ConvertToSepia(simpleSketchbitmap2)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            } else {
                sketchBitmap = ConvertToSepia(simpleSketchbitmap2)
            }
            8 -> sketchBitmap = BitmapFilter.changeStyle(bm1, BitmapFilter.OIL_STYLE)
            9 -> sketchBitmap = BitmapFilter.changeStyle(bm1, BitmapFilter.SKETCH_STYLE)
        }
        return sketchBitmap
        // result = Color
    }

    fun ConvertToSepia(sampleBitmap: Bitmap?): Bitmap {
        val sepMat = floatArrayOf(
            0.3930000066757202f,
            0.7689999938011169f,
            0.1889999955892563f,
            0f,
            0f,
            0.3490000069141388f,
            0.6859999895095825f,
            0.1679999977350235f,
            0f,
            0f,
            0.2720000147819519f,
            0.5339999794960022f,
            0.1309999972581863f,
            0f,
            0f,
            0f,
            0f,
            0f,
            1f,
            0f,
            0f,
            0f,
            0f,
            0f,
            1f
        )
        val sepiaMatrix = ColorMatrix()
        sepiaMatrix.set(sepMat)
        val colorFilter = ColorMatrixColorFilter(sepiaMatrix)
        val rBitmap = sampleBitmap!!.copy(Bitmap.Config.ARGB_8888, true)
        val paint = Paint()
        paint.colorFilter = colorFilter
        val myCanvas = Canvas(rBitmap)
        myCanvas.drawBitmap(rBitmap, 0f, 0f, paint)
        return rBitmap
    }

    fun fastblur(sentBitmap: Bitmap, radius: Int): Bitmap? {

        val bitmap = sentBitmap.copy(sentBitmap.config, true)
        if (radius < 1) {
            return null
        }
        val w = bitmap.width
        val h = bitmap.height
        val pix = IntArray(w * h)
        Log.e("pix", w.toString() + " " + h + " " + pix.size)
        bitmap.getPixels(pix, 0, w, 0, 0, w, h)
        val wm = w - 1
        val hm = h - 1
        val wh = w * h
        val div = radius + radius + 1
        val r = IntArray(wh)
        val g = IntArray(wh)
        val b = IntArray(wh)
        var rsum: Int
        var gsum: Int
        var bsum: Int
        var x: Int
        var y: Int
        var i: Int
        var p: Int
        var yp: Int
        var yi: Int
        var yw: Int
        val vmin = IntArray(Math.max(w, h))
        var divsum = div + 1 shr 1
        divsum *= divsum
        val dv = IntArray(256 * divsum)
        i = 0
        while (i < 256 * divsum) {
            dv[i] = i / divsum
            i++
        }
        yi = 0
        yw = yi
        val stack = Array(div) { IntArray(3) }
        var stackpointer: Int
        var stackstart: Int
        var sir: IntArray
        var rbs: Int
        val r1 = radius + 1
        var routsum: Int
        var goutsum: Int
        var boutsum: Int
        var rinsum: Int
        var ginsum: Int
        var binsum: Int
        y = 0
        while (y < h) {
            bsum = 0
            gsum = bsum
            rsum = gsum
            boutsum = rsum
            goutsum = boutsum
            routsum = goutsum
            binsum = routsum
            ginsum = binsum
            rinsum = ginsum
            i = -radius
            while (i <= radius) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))]
                sir = stack[i + radius]
                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff
                rbs = r1 - Math.abs(i)
                rsum += sir[0] * rbs
                gsum += sir[1] * rbs
                bsum += sir[2] * rbs
                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }
                i++
            }
            stackpointer = radius
            x = 0
            while (x < w) {
                r[yi] = dv[rsum]
                g[yi] = dv[gsum]
                b[yi] = dv[bsum]
                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum
                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]
                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm)
                }
                p = pix[yw + vmin[x]]
                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
                rsum += rinsum
                gsum += ginsum
                bsum += binsum
                stackpointer = (stackpointer + 1) % div
                sir = stack[stackpointer % div]
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]
                yi++
                x++
            }
            yw += w
            y++
        }
        x = 0
        while (x < w) {
            bsum = 0
            gsum = bsum
            rsum = gsum
            boutsum = rsum
            goutsum = boutsum
            routsum = goutsum
            binsum = routsum
            ginsum = binsum
            rinsum = ginsum
            yp = -radius * w
            i = -radius
            while (i <= radius) {
                yi = Math.max(0, yp) + x
                sir = stack[i + radius]
                sir[0] = r[yi]
                sir[1] = g[yi]
                sir[2] = b[yi]
                rbs = r1 - Math.abs(i)
                rsum += r[yi] * rbs
                gsum += g[yi] * rbs
                bsum += b[yi] * rbs
                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }
                if (i < hm) {
                    yp += w
                }
                i++
            }
            yi = x
            stackpointer = radius
            y = 0
            while (y < h) {
                pix[yi] =
                    -0x1000000 and pix[yi] or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]
                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum
                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]
                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w
                }
                p = x + vmin[y]
                sir[0] = r[p]
                sir[1] = g[p]
                sir[2] = b[p]
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
                rsum += rinsum
                gsum += ginsum
                bsum += binsum
                stackpointer = (stackpointer + 1) % div
                sir = stack[stackpointer]
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]
                yi += w
                y++
            }
            x++
        }
        Log.e("pix", w.toString() + " " + h + " " + pix.size)
        bitmap.setPixels(pix, 0, w, 0, 0, w, h)
        return bitmap
    }

    fun invert(original: Bitmap): Bitmap {
        // Create mutable Bitmap to invert, argument true makes it mutable
        val inversion = original.copy(Bitmap.Config.ARGB_8888, true)
        val RGB_MASK = 0x00FFFFFF
        // Get info about Bitmap
        val width = inversion.width
        val height = inversion.height
        val pixels = width * height
        val pixel = IntArray(pixels)
        inversion.getPixels(pixel, 0, width, 0, 0, width, height)
        for (i in 0 until pixels) pixel[i] = pixel[i] xor RGB_MASK
        inversion.setPixels(pixel, 0, width, 0, 0, width, height)
        return inversion
    }

    fun ColorDodgeBlend(source: Bitmap?, layer: Bitmap): Bitmap {
        val base = source!!.copy(Bitmap.Config.ARGB_8888, true)
        val blend = layer.copy(Bitmap.Config.ARGB_8888, false)
        val buffBase = IntBuffer.allocate(base.width * base.height)
        base.copyPixelsToBuffer(buffBase)
        buffBase.rewind()
        val buffBlend = IntBuffer.allocate(blend.width * blend.height)
        blend.copyPixelsToBuffer(buffBlend)
        buffBlend.rewind()
        val buffOut = IntBuffer.allocate(base.width * base.height)
        buffOut.rewind()

        while (buffOut.position() < buffOut.limit()) {
            val filterInt = buffBlend.get()
            val srcInt = buffBase.get()
            val redValueFilter = Color.red(filterInt)
            val greenValueFilter = Color.green(filterInt)
            val blueValueFilter = Color.blue(filterInt)
            val redValueSrc = Color.red(srcInt)
            val greenValueSrc = Color.green(srcInt)
            val blueValueSrc = Color.blue(srcInt)
            val redValueFinal = colordodge(redValueFilter, redValueSrc)
            val greenValueFinal = colordodge(greenValueFilter, greenValueSrc)
            val blueValueFinal = colordodge(blueValueFilter, blueValueSrc)
            val pixel = Color.argb(255, redValueFinal, greenValueFinal, blueValueFinal)
            // here
            buffOut.put(pixel)
        }
        buffOut.rewind()
        base.copyPixelsFromBuffer(buffOut)
        blend.recycle()
        return base
    }

    private fun colordodge(in1: Int, in2: Int): Int {
        val image = in2.toFloat()
        val mask = in1.toFloat()
        return (if (image == 255f) image else Math.min(
            255f, (mask.toLong() shl 8) / (255 - image)
        )).toInt()
    }

    fun toGrayscale(bmpOriginal: Bitmap?): Bitmap {
        val width: Int
        val height: Int
        height = bmpOriginal!!.height
        width = bmpOriginal.width
        val bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val c = Canvas(bmpGrayscale)
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        val f = ColorMatrixColorFilter(cm)
        paint.colorFilter = f
        c.drawBitmap(bmpOriginal, 0f, 0f, paint)
        return bmpGrayscale
    }

    var getimage = false


    fun showProgress(msg: String) {
        binding.apply {
            tvProgressMsg.text = msg
            progressView.visibility = View.VISIBLE
        }
    }

    fun hideProgress() {
        mExecutor.apply {
            runWorker(1000, {

                runMain {
                    binding.progressView.visibility = View.GONE
                }
            })
        }
    }

    fun loadImageAsycTask() {

        showProgress("Loading...")

        mExecutor.runWorker {

            val imageuri = intent.data

            if (imageuri == null) {
                getimage = false
            } else {
                Imagepath = getRealPathFromURI(imageuri)
                if (Imagepath != null && (Imagepath!!.endsWith(".png") || Imagepath!!.endsWith(".jpg") || Imagepath!!.endsWith(
                        ".jpeg"
                    ) || Imagepath!!.endsWith(".bmp"))
                ) {
                    val Orientation = getImageOrientation(Imagepath!!)
                    getAspectRatio(Imagepath!!, MaxResolution.toFloat())
                    pic_result = getResizedOriginalBitmap(Imagepath, Orientation)
                    pic_forSketch = getResizedOriginalBitmap(Imagepath, Orientation)
                    pic_forDraw = getResizedOriginalBitmap(Imagepath, Orientation)
                    getimage = true
                }
            }
            mExecutor.runMain {
                if (getimage) {
                    if (pic_result == null || pic_result!!.height <= 5 || pic_result!!.width <= 5) {

                        mContext.toast("Image Format not supported .")

                        finish()
                    } else {
                        binding.ivImageMaker.setImageBitmap(pic_result)
                        stringMatching()
                    }
                } else {

                    mContext.toast("Unsupported media file.")

                    finish()
                }

                hideProgress()
            }

        }
    }

    private fun sketchAsnyTaskFirst() {

        showProgress("Sketching...")

        mExecutor.runWorker {
            val eff = getSketchBitmap(pic_forDraw, 6)
            mExecutor.runMain {
                pic_result = eff
                binding.viewContainer.visibility = View.VISIBLE
                binding.viewContainer.addView(BlurView())
                binding.ivImageMaker.visibility = View.INVISIBLE
                hideProgress()
            }

        }
    }

    fun sketchAsnyTask(viewId: Int) {

        showProgress("Sketching...")

        mExecutor.runWorker {

            pic_result = getSketchBitmap(pic_forSketch, viewId)

            mExecutor.runMain {
                binding.ivImageMaker.setImageBitmap(pic_result)
                hideProgress()
            }
        }
    }

    inner class BlurView : View(mContext) {

        var mImagePos = PointF()
        var mImageSource = PointF()
        var mImageTarget = PointF()

        var mInterpolateTime: Long = 0

        var bitmap: Bitmap? = null
        var bmOverlay: Bitmap

        var pcanvas: Canvas

        var x = 0
        var y = 0
        var r = 0

        var mWidth = 0
        var mHeight = 0

        var Tilltime = 0
        private val mPaint: Paint

        init {
            isFocusable = true
            setBackgroundColor(Color.TRANSPARENT)
            mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mPaint.color = Color.TRANSPARENT
            mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
            mPaint.isAntiAlias = true
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            bmOverlay = Bitmap.createBitmap(
                pic_forDraw!!.width, pic_forDraw!!.height, Bitmap.Config.ARGB_8888
            )
            pcanvas = Canvas(bmOverlay)
            pic_forDraw!!.eraseColor(Color.WHITE)
        }

        @SuppressLint("DrawAllocation")
        override fun onDraw(canvas: Canvas) {
            // draw a circle that is erasing bitmap
            super.onDraw(canvas)
            canvas.drawBitmap(pic_result!!, 0f, 0f, null)
            pcanvas.drawBitmap(pic_forDraw!!, 0f, 0f, null)

            pcanvas.drawCircle(
                mImagePos.x, mImagePos.y, (pic_result!!.height / 20).toFloat(), mPaint
            )

            update()
            invalidate()
            canvas.drawBitmap(bmOverlay, 0f, 0f, null)
            val bitmapCanvas = Canvas(pic_forDraw!!)
            bitmapCanvas.drawBitmap(pic_result!!, 0f, 0f, null)
            bitmapCanvas.drawBitmap(bmOverlay, 0f, 0f, null)
            canvas.drawBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.drawing_hand),
                mImagePos.x,
                mImagePos.y,
                null
            )
        }

        fun update() {

            if (Tilltime < 400) {

                val INTERPOLATION_LENGTH: Long = 200
                val time = SystemClock.uptimeMillis()
                if (time - mInterpolateTime > INTERPOLATION_LENGTH) {
                    mImageSource.set(mImageTarget)
                    mImageTarget.x = (Math.random() * pic_forDraw!!.width).toFloat()
                    mImageTarget.y = (Math.random() * pic_forDraw!!.height).toFloat()
                    mInterpolateTime = time
                }
                var t = (time - mInterpolateTime).toFloat() / INTERPOLATION_LENGTH
                t = t * t * (3 - 2 * t)
                mImagePos.x = mImageSource.x + (mImageTarget.x - mImageSource.x) * t
                mImagePos.y = mImageSource.y + (mImageTarget.y - mImageSource.y) * t
                Tilltime++

            } else {
                if (lineOne) {
                    mImagePos.y = 0f
                    lineOne = false
                }

                if (mImagePos.y <= pic_forDraw!!.height) {
                    if (mImagePos.x <= 0) {
                        moveforword = true
                        MoveBack = false

                        mImagePos.y += (pic_result!!.height / 20).toFloat()
                    } else if (mImagePos.x >= pic_forDraw!!.width) {
                        moveforword = false
                        MoveBack = true

                        mImagePos.y += (pic_result!!.height / 20).toFloat()
                    }
                    if (moveforword) {
                        mImagePos.x += (pic_forDraw!!.width / 20).toFloat()
                    } else if (MoveBack) {
                        mImagePos.x -= (pic_result!!.width / 20).toFloat()
                    }
                } else {
                    sketchFirstTimeDone = true
                    binding.viewContainer.visibility = INVISIBLE
                    binding.ivImageMaker.visibility = VISIBLE
                    binding.ivImageMaker.setImageBitmap(pic_result)
                }
            }
        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            if (pic_result != null) {
                setMeasuredDimension(pic_result!!.width, pic_result!!.height)
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouchEvent(ev: MotionEvent): Boolean {
            when (ev.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    x = ev.x.toInt()
                    y = ev.y.toInt()
                    invalidate()
                    Log.i("TAG", "onTouchEvent: x= $x")
                }
                MotionEvent.ACTION_UP -> Log.i("TAG", "onTouchEvent: x= $x")
            }
            return true
        }
    }

}