package wishpool.sketch.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import java.io.ByteArrayOutputStream
import java.io.File


fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun Context.openNextActivity(cls: Class<*>) {
    val intent = Intent(this, cls)
    startActivity(intent)
}


fun Context.getAppSelectedColor(onSelection: (Int) -> Unit) {

    ColorPickerDialogBuilder.with(this)
        .setTitle("Choose color") //                .initialColor(currentBackgroundColor)
        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER).density(12).setOnColorSelectedListener {
            //                        toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
        }.setPositiveButton(
            "ok"
        ) { dialog, selectedColor, allColors -> //                        changeBackgroundColor(selectedColor);

            onSelection(selectedColor)
        }.setNegativeButton(
            "cancel"
        ) { dialog, which -> }.build().show()
}


fun Context.getAppBaseFolderPath(): String {
    val rootPath = filesDir.path
    if (File(rootPath).exists()) {
        return rootPath
    }
    File(rootPath).mkdirs()
    return rootPath
}

fun Context.getAppDrawingFolderPath(): String {

    val subDir = "SketchDrawing"
    val rootPath = "${getAppBaseFolderPath()}/${subDir}/"

    if (File(rootPath).exists()) {
        return rootPath
    }
    File(rootPath).mkdirs()
    return rootPath
}

fun Context.getAppSketchPhotoFolderPath(): String {

    val subDir = "SketchPhoto"
    val rootPath = "${getAppBaseFolderPath()}/${subDir}/"

    if (File(rootPath).exists()) {
        return rootPath
    }
    File(rootPath).mkdirs()
    return rootPath
}


fun Context.getImageUriFromBitmap(inImage: Bitmap): Uri {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(contentResolver, inImage, "Title", null)
    return Uri.parse(path)
}

fun Context.openLink(url: String) {
    this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

}

fun Context.shareApp() {

    try {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sketch Photo Maker")
        var shareMessage =
            "\nLet me recommend you this awesome application to draw the artistic photo sketch\n\n"
        shareMessage = """
            ${shareMessage + "https://play.google.com/store/apps/details?id=wishpool.sketch.photo.drawing"}
            
            
            """.trimIndent()
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        this.startActivity(Intent.createChooser(shareIntent, "choose one"))
    } catch (e: Exception) {

        Log.e(TAG, "shareApp: ${e.message}")
    }

}


