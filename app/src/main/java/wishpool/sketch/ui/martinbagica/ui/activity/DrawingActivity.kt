package wishpool.sketch.ui.martinbagica.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import wishpool.sketch.GlobalActivity
import wishpool.sketch.R
import wishpool.sketch.databinding.ActivityDrawingBinding
import wishpool.sketch.ui.BaseActivity
import wishpool.sketch.ui.martinbagica.domain.manager.FileManager
import wishpool.sketch.ui.martinbagica.ui.dialog.StrokeSelectorDialog
import wishpool.sketch.utils.getAppSelectedColor
import wishpool.sketch.utils.toast

class DrawingActivity : BaseActivity() {
    private var mCurrentBackgroundColor = 0
    private var mCurrentColor = 0
    private var mCurrentStroke = 0

    lateinit var binding: ActivityDrawingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDrawingView()
    }

    private fun clearDrawingWork() {
        AlertDialog.Builder(this).setTitle("Clear canvas")
            .setMessage("Are you sure you want to clear the canvas?")
            .setPositiveButton("Yes") { dialog, which -> binding.mainDrawingView.clearCanvas() }
            .setNegativeButton("Cancel", null).show()
    }

    private fun initDrawingView() {
        mCurrentBackgroundColor = ContextCompat.getColor(this, android.R.color.white)
        mCurrentColor = ContextCompat.getColor(this, android.R.color.black)
        mCurrentStroke = 10

        binding.apply {

            mainDrawingView.setBackgroundColor(mCurrentBackgroundColor)
            mainDrawingView.setPaintColor(mCurrentColor)
            mainDrawingView.setPaintStrokeWidth(mCurrentStroke)
        }
        setClicks()
    }

    private fun setClicks() {
        binding.apply {

            mainFillIv.setOnClickListener { onBackgroundFillOptionClick() }
            mainColorIv.setOnClickListener { onColorOptionClick() }
            mainStrokeIv.setOnClickListener { onStrokeOptionClick() }
            mainUndoIv.setOnClickListener { onUndoOptionClick() }
            mainRedoIv.setOnClickListener { onRedoOptionClick() }
            ivDownload.setOnClickListener { requestPermissionsAndSaveBitmap() }
            ivClear.setOnClickListener { clearDrawingWork() }
            ivBack.setOnClickListener { finish() }

        }
    }

    private fun startFillBackgroundDialog() {

        getAppSelectedColor {
            mCurrentBackgroundColor = it
            binding.mainDrawingView.setBackgroundColor(mCurrentBackgroundColor)
        }
    }

    private fun startColorPickerDialog() {


        getAppSelectedColor {
            mCurrentColor = it
            binding.mainDrawingView.setPaintColor(mCurrentColor)
        }
    }

    private fun startStrokeSelectorDialog() {
        val dialog = StrokeSelectorDialog.newInstance(mCurrentStroke, MAX_STROKE_WIDTH)
        dialog.setOnStrokeSelectedListener { stroke: Int ->
            mCurrentStroke = stroke
            binding.mainDrawingView.setPaintStrokeWidth(mCurrentStroke)
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
        FileManager.saveBitmap(this, binding.mainDrawingView.bitmap)
        toast("Drawing saved successfully.")
//        startShareDialog(uri!!)
    }

    private fun onBackgroundFillOptionClick() {
        startFillBackgroundDialog()
    }

    private fun onColorOptionClick() {
        startColorPickerDialog()
    }

    private fun onStrokeOptionClick() {
        startStrokeSelectorDialog()
    }

    private fun onUndoOptionClick() {
        binding.mainDrawingView.undo()
    }

    private fun onRedoOptionClick() {
        binding.mainDrawingView.redo()
    }

    companion object {
        private const val MAX_STROKE_WIDTH = 50
    }
}