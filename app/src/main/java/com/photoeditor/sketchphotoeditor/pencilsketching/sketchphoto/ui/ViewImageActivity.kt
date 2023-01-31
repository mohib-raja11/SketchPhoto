package com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.ui

import android.app.AlertDialog
import android.app.WallpaperManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.databinding.ActivityViewTheImageBinding
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class ViewImageActivity : BaseActivity() {

    private var bitmapForShare: Bitmap? = null
    private var imgUrl: String? = null


    private lateinit var binding: ActivityViewTheImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewTheImageBinding.inflate(layoutInflater)

        setContentView(binding.root)

        imgUrl = intent.getStringExtra("ImgUrl")
        binding.apply {
            Picasso.get().load(File(imgUrl!!)).into(mainImageView)

        }
        bitmapForShare = BitmapFactory.decodeFile(imgUrl)
        delay3Seconds()
        setClicks()
    }

    private fun setClicks() {
        binding.apply {
            mainImageView.setOnClickListener { v: View? -> showFullImage() }
            shareBtn.setOnClickListener { v: View? -> shareImage() }
            deleteBtn.setOnClickListener { v: View? -> deleteImage() }
            btnSetWallpaper.setOnClickListener { v: View? -> setWallpaper() }
            ivClose.setOnClickListener { v: View? -> finish() }
        }

    }

    private fun delay3Seconds() {


//        Handler().postDelayed({ tapped = false }, three_seconds.toLong())
    }

    private fun showFullImage() {
        val intent = Intent(this@ViewImageActivity, FullScreenImageActivity::class.java)
        intent.putExtra("path", imgUrl)
        startActivityForResult(intent, 2)
    }

    private fun shareImage() {
        val uri = getImageUri(this, bitmapForShare)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/jpg"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(intent, resources.getString(R.string.share_img_via)))
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap?): Uri {
        val bytes = ByteArrayOutputStream()
        inImage!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun setWallpaper() {
        if (bitmapForShare == null) {
            return
        }
        val myWallpaperManager = WallpaperManager.getInstance(applicationContext)
        try {
            myWallpaperManager.setBitmap(bitmapForShare)
            val toast = Toast.makeText(
                applicationContext, R.string.wall_paper_succesfully_set, Toast.LENGTH_LONG
            )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
            Handler().postDelayed({ finishAffinity() }, 1000)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun deleted() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.removeOpt))
        builder.setMessage(resources.getString(R.string.removeTxt))
        builder.setPositiveButton(
            resources.getString(R.string.yes),
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                val file = File(imgUrl!!)
                if (file.delete()) {
                    setResult(-1)
                    finish()
                    return@OnClickListener
                }
                Toast.makeText(
                    applicationContext, resources.getString(R.string.errorImg), Toast.LENGTH_SHORT
                ).show()
            })
        builder.setNegativeButton(resources.getString(R.string.cancel)) { dialog, id -> dialog.cancel() }
        builder.create().show()
    }

    private fun deleteImage() {
        deleted()
    }
}