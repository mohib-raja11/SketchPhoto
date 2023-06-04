package wishpool.sketch.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import wishpool.sketch.R
import wishpool.sketch.utils.Executor

open class BaseActivity : AppCompatActivity(), View.OnClickListener {

    protected val mExecutor = Executor()
    protected val tag: String = this.javaClass.simpleName
    protected val TAG: String = this.javaClass.simpleName
    protected lateinit var mContext: Context

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
    }

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
