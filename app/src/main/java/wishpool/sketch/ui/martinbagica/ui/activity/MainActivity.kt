package wishpool.sketch.ui.martinbagica.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.core.content.ContextCompat
import wishpool.sketch.R
import wishpool.sketch.ui.BaseActivity
import wishpool.sketch.ui.martinbagica.domain.manager.FileManager
import wishpool.sketch.ui.martinbagica.ui.component.DrawingView
import wishpool.sketch.ui.martinbagica.ui.dialog.StrokeSelectorDialog
import wishpool.sketch.utils.getAppSelectedColor
import wishpool.sketch.utils.toast

class MainActivity : BaseActivity() {
    private var mCurrentBackgroundColor = 0
    private var mCurrentColor = 0
    private var mCurrentStroke = 0
    private lateinit var mDrawingView: DrawingView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        initDrawingView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun clearDrawingWork() {
        AlertDialog.Builder(this).setTitle("Clear canvas")
            .setMessage("Are you sure you want to clear the canvas?")
            .setPositiveButton("Yes") { dialog, which -> mDrawingView.clearCanvas() }
            .setNegativeButton("Cancel", null).show()
    }

    private fun initDrawingView() {
        mDrawingView = findViewById(R.id.main_drawing_view)
        mCurrentBackgroundColor = ContextCompat.getColor(this, android.R.color.white)
        mCurrentColor = ContextCompat.getColor(this, android.R.color.black)
        mCurrentStroke = 10
        mDrawingView.setBackgroundColor(mCurrentBackgroundColor)
        mDrawingView.setPaintColor(mCurrentColor)
        mDrawingView.setPaintStrokeWidth(mCurrentStroke)
        setClicks()
    }

    private fun setClicks() {
        findViewById<View>(R.id.main_fill_iv).setOnClickListener { view: View? -> onBackgroundFillOptionClick() }
        findViewById<View>(R.id.main_color_iv).setOnClickListener { view: View? -> onColorOptionClick() }
        findViewById<View>(R.id.main_stroke_iv).setOnClickListener { view: View? -> onStrokeOptionClick() }
        findViewById<View>(R.id.main_undo_iv).setOnClickListener { view: View? -> onUndoOptionClick() }
        findViewById<View>(R.id.main_redo_iv).setOnClickListener { view: View? -> onRedoOptionClick() }
        findViewById<View>(R.id.ivDownload).setOnClickListener { view: View? -> requestPermissionsAndSaveBitmap() }
        findViewById<View>(R.id.ivClear).setOnClickListener { view: View? -> clearDrawingWork() }


    }

    private fun startFillBackgroundDialog() {

        getAppSelectedColor {
            mCurrentBackgroundColor = it
            mDrawingView.setBackgroundColor(mCurrentBackgroundColor)
        }
    }

    private fun startColorPickerDialog() {


        getAppSelectedColor {
            mCurrentColor = it
            mDrawingView.setPaintColor(mCurrentColor)
        }
    }

    private fun startStrokeSelectorDialog() {
        val dialog = StrokeSelectorDialog.newInstance(mCurrentStroke, MAX_STROKE_WIDTH)
        dialog.setOnStrokeSelectedListener { stroke: Int ->
            mCurrentStroke = stroke
            mDrawingView.setPaintStrokeWidth(mCurrentStroke)
        }
        dialog.show(supportFragmentManager, "StrokeSelectorDialog")
    }

    private fun startShareDialog(uri: Uri) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_SUBJECT, "")
        intent.putExtra(Intent.EXTRA_TEXT, "")
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(intent, "Share Image"))
    }

    private fun requestPermissionsAndSaveBitmap() {
        val uri = FileManager.saveBitmap(this, mDrawingView.bitmap)
        toast("Drawing saved successfully.")
//        startShareDialog(uri!!)
    }

    fun onBackgroundFillOptionClick() {
        startFillBackgroundDialog()
    }

    fun onColorOptionClick() {
        startColorPickerDialog()
    }

    fun onStrokeOptionClick() {
        startStrokeSelectorDialog()
    }

    fun onUndoOptionClick() {
        mDrawingView.undo()
    }

    fun onRedoOptionClick() {
        mDrawingView.redo()
    }

    companion object {
        private const val MAX_STROKE_WIDTH = 50
    }
}