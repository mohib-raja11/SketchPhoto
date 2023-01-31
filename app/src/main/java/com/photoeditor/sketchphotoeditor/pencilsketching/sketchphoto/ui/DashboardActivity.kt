package com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.ui

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.esafirm.imagepicker.features.*
import com.esafirm.imagepicker.model.Image
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R
import com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.constant.AppConstant
import com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.ui.pencil.ImageRemakeActivity

class DashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnPrivacy -> gotothisLink(AppConstant.privacyLink)
            R.id.btnRateUs -> gotothisLink("market://details?id=$packageName")
            R.id.btnMoreApps -> gotothisLink(AppConstant.moreAppsLink)
            R.id.btnStart -> pickImageWithLib()
            R.id.btnGallery -> startActivity(Intent(mContext, MyGalleryActivity::class.java))

        }
    }


    private fun pickImageWithLib() {
        run {
            launcher.launch()

        }
    }

    private val launcher = registerImagePicker { result: List<Image> ->
        result.forEach { image ->
            println(image)

            Log.d(tag, "yes it is nice: ")

            startImageRemaker(image.uri)
        }
    }


    private fun gotothisLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }


    @SuppressLint("WrongConstant")
    private fun startImageRemaker(uri: Uri) {


        val flag =
            (getSystemService("activity") as ActivityManager).deviceConfigurationInfo.reqGlEsVersion >= 0x20000

        if (!flag) {
            Toast.makeText(
                applicationContext, "Editor is not supported in this device.", Toast.LENGTH_SHORT
            ).show()

            return
        }

        val `as` = arrayOf("CROP")
        val intent = Intent(this, ImageRemakeActivity::class.java)
        intent.data = uri
        intent.putExtra("tool_title", `as`)
        startActivityForResult(intent, 5)
    }


}