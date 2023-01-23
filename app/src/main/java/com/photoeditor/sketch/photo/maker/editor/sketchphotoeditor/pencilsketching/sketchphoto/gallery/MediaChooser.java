

package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.gallery;


public class MediaChooser {

	public static final String VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER = "lNc_videoSelectedAction"; 

	public static final String IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER = "lNc_imageSelectedAction"; 

	public static void setFolderName(String folderName) {
		if(folderName != null){
			MediaChooserConstants.folderName = folderName;
		}
	}

	public static void showCameraVideoView(boolean showCameraVideo) {
		MediaChooserConstants.showCameraVideo = showCameraVideo;
	}

	public static void setImageSize(int size) {
		MediaChooserConstants.SELECTED_IMAGE_SIZE_IN_MB = size;
	}

	public static void setVideoSize(int size) {
		MediaChooserConstants.SELECTED_VIDEO_SIZE_IN_MB = size;
	}

	public static void setSelectionLimit(int limit) {
		MediaChooserConstants.MAX_MEDIA_LIMIT = limit;
	}

	public static void setSelectedMediaCount(int count) {
		MediaChooserConstants.SELECTED_MEDIA_COUNT = count;
	}

	/**
	 *  Get selected media file count.
	 *  
	 * @return
	 * 			count.
	 */
	public static int getSelectedMediaCount() {
		return MediaChooserConstants.SELECTED_MEDIA_COUNT ;
	}

	/**
	 * To display images only.
	 */
	public static void showOnlyImageTab() {
		MediaChooserConstants.showImage = true;
		MediaChooserConstants.showVideo = false;
	}

	/**
	 * To display video and images.
	 */
	public static void showImageVideoTab() {
		MediaChooserConstants.showImage = true;
		MediaChooserConstants.showVideo = true;
	}

	/**
	 * To display videos only.
	 */
	public static void showOnlyVideoTab() {
		MediaChooserConstants.showImage = false;
		MediaChooserConstants.showVideo = true;
	}

}
