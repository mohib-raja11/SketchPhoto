/*
package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketch.artist;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R;


public final class SavingHelper {
	public static String fileName;
	public static String picName;

	static {
		fileName = ".temp";
		picName = new StringBuilder(Environment
				.getExternalStorageDirectory().getAbsolutePath()).append(
				"/Sketch").toString();
	}

	public static void tempFileCreator(Context context) {
		if (SavingHelper.isCardMounted()) {
			File file = new File(new StringBuilder(Environment
					.getExternalStorageDirectory().getAbsolutePath())
					.append("/").append(context.getString(R.string.app_name))
					.append("/").append(fileName).toString());
			if (file.exists()) {
				File[] listFiles = file.listFiles();
				if (listFiles != null) {
					for (File delete : listFiles) {
						delete.delete();
					}
				}
			}
		}
	}

	public static boolean isCardMounted() {
		return Environment.getExternalStorageState().equals("mounted");
	}

	public static File getFileName(String str, String str2) {
		if (!SavingHelper.isCardMounted()) {
			return null;
		}
		File file = new File(str);
		if (file.exists() || file.mkdirs()) {
			file = new File(str, str2);
			if (file.exists()) {
				file.delete();
			}
			try {
				file.createNewFile();
				if (file.isFile() && file.canWrite()) {
					return file;
				}
				IOException iOException = new IOException(String.valueOf(4353), new Throwable(file.getAbsolutePath()));
				throw iOException;
			} catch (IOException e) {
				IOException iOException2 = new IOException(String.valueOf(4354), new Throwable(str));
				try {
					throw iOException2;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		IOException iOException2 = new IOException(String.valueOf(4354), new Throwable(str));
		try {
			throw iOException2;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	public static Bitmap getBitmapScale(Context context, String str, int i)
			throws Throwable {
		Throwable th;
		Bitmap bitmap = null;
		System.currentTimeMillis();
		try {
			String stringBuilder = new StringBuilder(Environment
					.getExternalStorageDirectory().getAbsolutePath())
					.append("/").append(context.getString(R.string.app_name))
					.append("/").append(fileName).append("/").append(str)
					.toString();
			if (new File(stringBuilder).exists()) {
			}
		} catch (Exception e6) {
			Throwable excep2222 = null;
			excep2222.printStackTrace();
			return bitmap;
		} catch (Throwable th3) {
			th = th3;
			throw th;
		}
		return bitmap;
	}

}
*/
