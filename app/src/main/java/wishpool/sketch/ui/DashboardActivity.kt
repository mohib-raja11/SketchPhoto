package wishpool.sketch.ui

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import com.esafirm.imagepicker.features.*
import com.esafirm.imagepicker.model.Image
import wishpool.sketch.R
import wishpool.sketch.ui.martinbagica.ui.activity.MainActivity
import wishpool.sketch.ui.pencil.ImageRemakeActivity
import wishpool.sketch.utils.getAppDrawingFolderPath
import wishpool.sketch.utils.getAppSketchPhotoFolderPath
import wishpool.sketch.utils.openNextActivity
import wishpool.sketch.utils.toast

class DashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


    }

    override fun onClick(view: View) {
        when (view.id) {
            /*R.id.btnPrivacy -> gotothisLink(AppConstant.privacyLink)
            R.id.btnRateUs -> gotothisLink("market://details?id=$packageName")
            R.id.btnMoreApps -> gotothisLink(AppConstant.moreAppsLink)*/
            R.id.btnHandDrawing -> openNextActivity(MainActivity::class.java)
            R.id.btnStart -> pickImageWithLib()
            R.id.btnGallery -> MyGalleryActivity.start(
                mContext,
                getAppSketchPhotoFolderPath(),
                getString(R.string.my_work)
            )
            R.id.btnGalleryDrawing -> MyGalleryActivity.start(
                mContext,
                getAppDrawingFolderPath(),
                getString(R.string.saved_drawaing)
            )

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
            toast("Editor is not supported in this device.")

            return
        }

        val intent = Intent(this, ImageRemakeActivity::class.java)
        intent.data = uri

        startActivityForResult(intent, 5)
    }


}