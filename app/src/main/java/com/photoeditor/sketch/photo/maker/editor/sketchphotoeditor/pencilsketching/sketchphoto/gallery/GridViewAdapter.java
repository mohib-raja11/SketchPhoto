

package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.gallery;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R;

public class GridViewAdapter extends ArrayAdapter<MediaModel> {
	

	private Context mContext;
	private List<MediaModel> mGalleryModelList;
	private int mWidth;
	private boolean mIsFromVideo;
	LayoutInflater viewInflater;
	

	public GridViewAdapter(Context context, int resource, List<MediaModel> categories, boolean isFromVideo) {
		super(context, resource, categories);
		mGalleryModelList = categories;
		mContext          = context;
		mIsFromVideo      = isFromVideo;
		viewInflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return mGalleryModelList.size();
	}

	@Override
	public MediaModel getItem(int position) {
		return mGalleryModelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {

			mWidth = mContext.getResources().getDisplayMetrics().widthPixels;  
			
			convertView = viewInflater.inflate(R.layout.view_grid_item_media_chooser, parent, false);

			holder = new ViewHolder();
			holder.checkBoxTextView   = (CheckedTextView) convertView.findViewById(R.id.checkTextViewFromMediaChooserGridItemRowView);
			holder.imageView          = (ImageView) convertView.findViewById(R.id.imageViewFromMediaChooserGridItemRowView);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LayoutParams imageParams = (LayoutParams) holder.imageView.getLayoutParams();
		imageParams.width  = mWidth/2;
		imageParams.height = mWidth/2;

		holder.imageView.setLayoutParams(imageParams);

		// set the status according to this Category item

		if(mIsFromVideo){
		//	new VideoLoadAsync(videoFragment, holder.imageView, false, mWidth/2).executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, mGalleryModelList.get(position).url.toString());

		}else{
			ImageLoadAsync loadAsync = new ImageLoadAsync(mContext, holder.imageView, mWidth/2);
			loadAsync.executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, mGalleryModelList.get(position).url);
		}

		holder.checkBoxTextView.setChecked(mGalleryModelList.get(position).status);
		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		CheckedTextView checkBoxTextView;
	}

}
