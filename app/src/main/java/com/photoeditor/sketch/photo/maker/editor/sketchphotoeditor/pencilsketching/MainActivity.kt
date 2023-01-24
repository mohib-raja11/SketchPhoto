package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.ui.BaseActivity
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.ui.DashboardActivity
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.Utils.toast

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        startActivity(Intent(this, DashboardActivity::class.java))

        mExecutor.runMain {
            Log.d(tag, "")

        }

        mContext.toast("Main screen ${tag}")

    }
}