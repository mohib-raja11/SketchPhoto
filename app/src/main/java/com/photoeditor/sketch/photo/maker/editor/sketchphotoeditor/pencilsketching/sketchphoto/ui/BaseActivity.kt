package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.utils.Executor

open class BaseActivity : AppCompatActivity(), View.OnClickListener {

    protected val mExecutor = Executor()
    protected val tag: String = this.javaClass.simpleName
    protected lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
    }

    override fun onClick(view: View) {
        Log.d(tag, "onClick: clicked id = " + view.id)
    }

    override fun onDestroy() {
        super.onDestroy()
        mExecutor.release()
    }

}
