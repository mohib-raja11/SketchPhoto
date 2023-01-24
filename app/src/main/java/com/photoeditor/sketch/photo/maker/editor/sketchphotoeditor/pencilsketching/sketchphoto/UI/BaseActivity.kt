package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.Utils.Executor

open class BaseActivity : AppCompatActivity(), View.OnClickListener {

    val mExecutor = Executor()
    val TAG = this.javaClass.simpleName
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
    }

    override fun onClick(view: View) {
        Log.d(TAG, "onClick: clicked id = " + view.id)
    }

    override fun onDestroy() {
        super.onDestroy()
        mExecutor.release()
    }

}
