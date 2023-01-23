package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.gallery;

import java.util.ArrayList;

import android.graphics.Bitmap;
import androidx.fragment.app.Fragment;
import androidx.collection.LruCache;
import android.widget.ImageView;

import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R;



public class GalleryCache {
	private LruCache<String, Bitmap> mBitmapCache;
	private ArrayList<String> mCurrentTasks;
	public GalleryCache(int size, int maxWidth, int maxHeight) {
		mBitmapCache = new LruCache<String, Bitmap>(size) {
			@Override
			protected int sizeOf(String key, Bitmap b) {
				// Assuming that one pixel contains four bytes.
				return b.getHeight() * b.getWidth() * 4;
			}
		};

		mCurrentTasks = new ArrayList<String>();
	}

	private Bitmap getBitmapFromCache(String key) {
		return mBitmapCache.get(key);
	}

	
	public void loadBitmap(Fragment mainActivity, String imageKey,
			ImageView imageView, boolean isScrolling) {
		final Bitmap bitmap = getBitmapFromCache(imageKey);
		

		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.drawable.ic_loading);
			//			imageView.setImageResource(R.drawable.transprent_drawable);
//			if (!isScrolling && !mCurrentTasks.contains(imageKey)) {
//				if(mainActivity instanceof VideoFragment){
//					BitmapLoaderTask task = new BitmapLoaderTask(imageKey, ((VideoFragment)mainActivity).getAdapter());
//					task.execute();
//
//				}else if(mainActivity instanceof BucketVideoFragment){
//					BitmapLoaderTask task = new BitmapLoaderTask(imageKey, ((BucketVideoFragment)mainActivity).getAdapter());
//					task.execute();
//				}
//			}
		}
	}

	public void clear() {
		mBitmapCache.evictAll();
	}



}
