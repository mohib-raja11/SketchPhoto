package wishpool.sketch.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.exifinterface.media.ExifInterface
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.GPUImageFilter
import wishpool.sketch.R
import wishpool.sketch.databinding.ActivityColorEditingBinding
import wishpool.sketch.databinding.ColorPickerDialogBinding
import wishpool.sketch.ui.pencil.GPUImageFilterTools
import wishpool.sketch.ui.pencil.GPUImageFilterTools.Applyeffects
import wishpool.sketch.utils.getImageUriFromBitmap
import wishpool.sketch.utils.toast
import java.io.*

class ColorEditingActivity : BaseActivity() {

    private var mFilter: GPUImageFilter? = null
    private var mGPUImage: GPUImage? = null

    private var shareuri: Uri? = null

    private var shareBitmap: Bitmap? = null

    private var screenwidth = 0
    private var imagewidth = 0
    private var imageheight = 0
    private var currentapiVersion = 0
    private var maxResolution = 0

    private var savedok = false
    private var getimage = false

    private lateinit var binding: ActivityColorEditingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityColorEditingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initViews()
        val displaymetrics = DisplayMetrics()
        mGPUImage = GPUImage(this)
        windowManager.defaultDisplay.getMetrics(displaymetrics)
        screenwidth = displaymetrics.widthPixels
        maxResolution = screenwidth
        currentapiVersion = Build.VERSION.SDK_INT
        loadImageAsycTask()

    }

    private fun initViews() {

        binding.apply {

            binding.rlProgress.visibility = View.GONE

            greenButton.setOnClickListener {
                EffectAsnycFun(0)
                doneButton.visibility = View.VISIBLE
                resetButton.visibility = View.VISIBLE
            }
            blueButton.setOnClickListener {
                EffectAsnycFun(1)
                doneButton.visibility = View.VISIBLE
                resetButton.visibility = View.VISIBLE
            }
            redButton.setOnClickListener {
                EffectAsnycFun(2)
                doneButton.visibility = View.VISIBLE
                resetButton.visibility = View.VISIBLE
            }
            resetButton.setOnClickListener {
                loadImageAsycTask()
                colorLayout.visibility = View.INVISIBLE
            }
            doneButton.setOnClickListener {
                colorLayout.visibility = View.INVISIBLE
            }
            effectButton.setOnClickListener { colorLayout.visibility = View.VISIBLE }
            effectButton.setOnLongClickListener {

                imageColorPickerDialog { red, green, blue ->
                    run {

                        GPUImageFilterTools.redValue = red
                        GPUImageFilterTools.greenValue = green
                        GPUImageFilterTools.blueValue = blue

                        EffectAsnycFun(-1)
                        doneButton.visibility = View.VISIBLE
                        resetButton.visibility = View.VISIBLE

                    }
                }
                true
            }

            btnDelete.setOnClickListener { deleteCurrentImage() }
            saveBtn.setOnClickListener {
                val bitmap = mGPUImage!!.bitmapWithFilterApplied
                saveImage(bitmap)
            }
            shareBtn.setOnClickListener {
                val bitmap = mGPUImage!!.bitmapWithFilterApplied
                val uri = getImageUriFromBitmap(bitmap)
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/jpg"
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                startActivity(
                    Intent.createChooser(
                        intent, resources.getString(R.string.share_img_via)
                    )
                )
            }
        }
    }

    private val intentExtra: Unit
        get() {
            val intent = intent
            shareuri = intent.data
            maxResolution = intent.getIntExtra("picresolution", screenwidth)
        }

    private fun getRealPathFromURI(uri: Uri?): String? {
        val cursor = contentResolver.query(uri!!, null, null, null, null)
        return if (cursor == null) {
            uri.path
        } else {
            cursor.moveToFirst()
            @SuppressLint("Range") val s = cursor.getString(cursor.getColumnIndex("_data"))
            cursor.close()
            s
        }
    }

    private fun getImageOrientation(fileName: String): Float {
        val f: Float
        val i: Int = try {
            ExifInterface(fileName).getAttributeInt("Orientation", 1)
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

    private fun getAspectRatio(pathName: String, f: Float) {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(pathName, options)
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

    private fun saveImage(bitmap: Bitmap?) {
        wishpool.sketch.constant.SaveToStorageUtil.saveReal(bitmap, mContext)

        mContext.toast(getString(R.string.img_saved_successfully))

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(mContext, DashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }, 1000)
    }

    override fun onDestroy() {
        if (savedok) {
            if (shareBitmap != null && !shareBitmap!!.isRecycled) {
                shareBitmap!!.recycle()
                shareBitmap = null
                System.gc()
            }

            savedok = false
            shareuri = null

        } else {
            if (shareuri == null) {
                if (shareBitmap != null && !shareBitmap!!.isRecycled) {
                    shareBitmap!!.recycle()
                    shareBitmap = null
                    System.gc()
                }
                savedok = false
                shareuri = null
            } else {
                val file = File(shareuri!!.path)
                if (file.exists()) {
                    file.delete()
                }
                if (currentapiVersion > 18) {
                    scanFile(shareuri!!.path!!, true)
                }
                if (currentapiVersion <= 18) {
                    try {
                        Log.d(TAG, "onDestroy: ")
                        //MR: file now auto updated into local storage that's why commented
                        
                        /*val intent = Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE")
                        intent.data = Uri.fromFile(shareuri!!.path?.let { File(it) })
                        sendBroadcast(intent)*/
                    } catch (nullpointerexception: NullPointerException) {
                        nullpointerexception.printStackTrace()
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                }
            }
        }
        super.onDestroy()
    }

    private fun scanFile(file: String, isDelete: Boolean) {
        try {
            MediaScannerConnection.scanFile(applicationContext, arrayOf(file), null) { _, uri ->
                if (isDelete && uri != null) {
                    applicationContext.contentResolver.delete(uri, null, null)
                }
            }
            return
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    private fun Image_effect(pos: Int) {
        Applyeffects(pos) { gpuimagefilter: GPUImageFilter? ->
            switchFilterTo(gpuimagefilter)
            mGPUImage!!.requestRender()
        }
    }

    fun switchFilterTo(gpuimagefilter: GPUImageFilter?) {
        if (mFilter == null || gpuimagefilter != null) {
            mFilter = gpuimagefilter
            mGPUImage!!.setFilter(mFilter)
        }
    }

    private fun deleteCurrentImage() {
        val intent = Intent(mContext, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    fun loadImageAsycTask() {
        getimage = false
        binding.rlProgress.visibility = View.VISIBLE
        intentExtra
        mExecutor.runWorker {
            val orientation: Float
            val imagepath = getRealPathFromURI(shareuri)
            if (imagepath != null && imagepath.endsWith(".png") || imagepath!!.endsWith(".jpg") || imagepath.endsWith(
                    ".jpeg"
                ) || imagepath.endsWith(".bmp")
            ) {
                orientation = java.lang.Float.valueOf(getImageOrientation(imagepath))
                getAspectRatio(imagepath, maxResolution.toFloat())
                shareBitmap = getResizedOriginalBitmap(imagepath, orientation)
                getimage = true
            }
            runOnUiThread {
                if (getimage) {
                    if (shareBitmap == null || shareBitmap!!.height <= 5 || shareBitmap!!.width <= 5) {
                        Toast.makeText(
                            applicationContext, "Image Format not supported .", Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        binding.picImageView.setImageBitmap(shareBitmap)
                        mGPUImage!!.setImage(shareBitmap)
                    }
                } else {
                    Toast.makeText(
                        applicationContext, "Unsupported media file.", Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
                binding.rlProgress.visibility = View.GONE
            }
        }
    }

    private fun EffectAsnycFun(param: Int) {

        var eff: Bitmap? = null

        mExecutor.apply {
            runWorker {
                Image_effect(param)

                runMain {

                    binding.apply {
                        val bitmap = eff
                        try {
                            eff = mGPUImage!!.getBitmapWithFilterApplied(shareBitmap)
                            picImageView.setImageBitmap(eff)
                        } catch (nullpointerexception: NullPointerException) {
                            picImageView.setImageBitmap(shareBitmap)
                            mGPUImage!!.setImage(shareBitmap)
                        } catch (outofmemoryerror: OutOfMemoryError) {
                            picImageView.setImageBitmap(shareBitmap)
                            mGPUImage!!.setImage(shareBitmap)
                            System.gc()
                        } catch (ignored: Exception) {
                        }
                        if (bitmap != null && !bitmap.isRecycled) {
                            bitmap.recycle()
                            System.gc()
                        }
                    }
                }
            }
        }
    }

    fun imageColorPickerDialog(callBack: (Float, Float, Float) -> Unit) {
        val exit_dialog = Dialog(this)
        exit_dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        val exitDialogBinding = ColorPickerDialogBinding.inflate(layoutInflater)

        exitDialogBinding.apply {
            exit_dialog.setContentView(root)
            yesBtn.setOnClickListener {
                exit_dialog.dismiss()

                val red = etRed.text.toString().toFloat()
                val green = etGreen.text.toString().toFloat()
                val blue = etBlue.text.toString().toFloat()

                callBack.invoke(red, green, blue)
            }
            noBtn.setOnClickListener { exit_dialog.dismiss() }
            exit_dialog.show()
        }
    }
}