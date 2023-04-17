package wishpool.sketch.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast

fun Context.toast(msg: String){
    Toast.makeText(this,msg, Toast.LENGTH_LONG).show()
}

fun Context.openNextActivity(cls : Class<*>){
    val intent = Intent(this,cls)
    startActivity(intent)
}