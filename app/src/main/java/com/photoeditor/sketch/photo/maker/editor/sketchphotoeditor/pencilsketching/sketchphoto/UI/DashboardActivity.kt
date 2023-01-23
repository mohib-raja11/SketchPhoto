package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.esafirm.imagepicker.features.*
import com.esafirm.imagepicker.model.Image
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.gallery.MediaChooser
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.constant.AppConstant
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.pencil.ImageRemakeActivity

class DashboardActivity : AppCompatActivity(), View.OnClickListener {

    val TAG = "DashboardActivity"

    private val PERMISSION_REQUEST_CODE = 1
    private var context: Context? = null
    private var widthPixel = 0
    private val imageBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val filePathList: List<String>? = intent.getStringArrayListExtra("list")
            if (filePathList != null && filePathList.size > 0) {
                StartImageRemaker(Uri.parse(filePathList[0]))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        context = this
        val imageIntentFilter = IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER)
        registerReceiver(imageBroadcastReceiver, imageIntentFilter)
        val displaymetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displaymetrics)
        widthPixel = displaymetrics.widthPixels
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnPrivacy -> gotothisLink(AppConstant.privacyLink)
            R.id.btnRateUs -> gotothisLink("market://details?id=$packageName")
            R.id.btnMoreApps -> gotothisLink(AppConstant.moreAppsLink)
            R.id.btnStart -> pickImageFromGallery()
            R.id.btnGallery -> startActivity(Intent(context, MyGalleryActivity::class.java))
            R.id.btnPickImage -> {
                pickImageWithLib()
            }
        }
    }


    private fun pickImageWithLib() {
        run {

            launcher.launch()


        }
    }

    val launcher = registerImagePicker { result: List<Image> ->
        result.forEach { image ->
            println(image)

            Log.d(TAG, "yes it is nice: ")

            StartImageRemaker(image.uri)
        }
    }

    private fun pickImageFromGallery() {
        /* if (Build.VERSION.SDK_INT >= 23) {
             if (checkPermission()) {
                 MediaChooser.setSelectionLimit(20)
                 val intent = Intent(this@DashboardActivity, ChoosePhotoFragmentActivity::class.java)
                 startActivity(intent)
             } else {
                 requestPermission() // Code for permission
             }
         } else {
             MediaChooser.setSelectionLimit(20)
             val intent = Intent(this@DashboardActivity, ChoosePhotoFragmentActivity::class.java)
             startActivity(intent)
         }*/
    }

    private fun gotothisLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this@DashboardActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@DashboardActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                this@DashboardActivity,
                "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this@DashboardActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }


    @SuppressLint("WrongConstant")
    private fun StartImageRemaker(uri: Uri) {


        val flag: Boolean
        flag =
            (getSystemService("activity") as ActivityManager).deviceConfigurationInfo.reqGlEsVersion >= 0x20000
        if (!flag) {
            Toast.makeText(
                applicationContext,
                "Editor is not supported in this device.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val `as` = arrayOf("CROP")
        val intent = Intent(this, ImageRemakeActivity::class.java)
        intent.data = uri
        intent.putExtra("picresolution", widthPixel)
        intent.putExtra("tool_title", `as`)
        startActivityForResult(intent, 5)
    }

    private fun shareApp() {
        val appPackageName = packageName
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey am using Sketch Photo Application to sketch photo and Editing. You can check out at: https://play.google.com/store/apps/details?id=$appPackageName"
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    private fun showExitDialog() {
        val dialog: AlertDialog
        val builder = AlertDialog.Builder(this, R.style.MyDialogTheme)
        builder.setTitle(getString(R.string.alert))
        builder.setMessage(getString(R.string.exit_message))
        builder.setPositiveButton("Yes") { _, which -> finish() }
        builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
        builder.setNeutralButton(R.string.share_app) { dialog, which ->
            shareApp()
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.show()
        var b = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        b.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        b = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        b.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
    }
}