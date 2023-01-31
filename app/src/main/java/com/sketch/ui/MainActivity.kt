package com.sketch.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R
import com.sketch.utils.toast

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val en_US = "enUs"

        Log.d("TAG", "onCreate: $en_US")
        mExecutor.apply {
            runWorker {
                runMain {
                    Log.d(tag, "main after no delay")
                }
            }
        }


        mExecutor.apply {
            runWorker(2) {

                runMain {
                    Log.d(tag, "onCreate: on main after 2sec")
                }
            }
        }

        mContext.toast("Main screen $tag")

        startActivity(Intent(this, DashboardActivity::class.java))


    }
}