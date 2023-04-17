package wishpool.sketch.ui.martinbagica.domain.manager

import android.content.Context
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.core.content.FileProvider
import wishpool.sketch.utils.getAppDrawingFolderPath
import wishpool.sketch.utils.getImageUriFromBitmap
import java.io.File
import java.io.FileOutputStream
import java.util.*

object FileManager {
    fun saveBitmap(context: Context, bitmap: Bitmap): Uri? {
        val dir = File(context.getAppDrawingFolderPath())
        val name = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + ".png"
        val file = File(dir, name)
        val fOut: FileOutputStream
        try {
            fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()
            return FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".fileprovider", file
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}