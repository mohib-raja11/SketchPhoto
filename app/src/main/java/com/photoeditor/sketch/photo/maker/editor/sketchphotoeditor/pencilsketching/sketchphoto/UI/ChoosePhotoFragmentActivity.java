package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI;/*
package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTabHost;
import androidx.fragment.app.FragmentTransaction;

import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketch.gallery.BucketImageFragment;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketch.gallery.MediaChooserConstants;

import java.util.ArrayList;
//import java.util.Date;

public class ChoosePhotoFragmentActivity extends FragmentActivity {

    //    private static Uri fileUri;
//    private final Handler handler = new Handler();
    private FragmentTabHost mTabHost;

    private final ArrayList<String> mSelectedImage = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_media_chooser_photo);

        initViews();

        mTabHost.setup(this, getSupportFragmentManager(), R.id.realTabcontent);

        if (MediaChooserConstants.showImage) {
            mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Images" + " "), BucketImageFragment.class, null);
        }

        mTabHost.getTabWidget().setBackgroundColor(Color.GRAY);

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {

            View childView = mTabHost.getTabWidget().getChildAt(i);
            TextView textView = childView.findViewById(android.R.id.title);

            if (textView.getLayoutParams() instanceof RelativeLayout.LayoutParams) {

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) textView
                        .getLayoutParams();
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                textView.setLayoutParams(params);

            } else if (textView.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView
                        .getLayoutParams();
                params.gravity = Gravity.CENTER;
                textView.setLayoutParams(params);
            }
            textView.setTextColor(getResources().getColor(R.color.tabs_title_color));
            textView.setTextSize(convertDipToPixels(10));
        }

        ((TextView) (mTabHost.getTabWidget().getChildAt(0)
                .findViewById(android.R.id.title))).setTextColor(Color.WHITE);

        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                BucketImageFragment imageFragment = (BucketImageFragment) fragmentManager
                        .findFragmentByTag("tab1");
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();

                if (tabId.equalsIgnoreCase("tab1")) {

                    if (imageFragment == null) {
                        BucketImageFragment newImageFragment = new BucketImageFragment();
                        fragmentTransaction.add(R.id.realTabcontent,
                                newImageFragment, "tab1");

                    } else {

                        fragmentTransaction.show(imageFragment);

                    }
                    ((TextView) (mTabHost.getTabWidget().getChildAt(0)
                            .findViewById(android.R.id.title)))
                            .setTextColor(getResources().getColor(
                                    R.color.headerbar_selected_tab_color));

                } else {

                    ((TextView) (mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title)))
                            .setTextColor(Color.WHITE);
                    ((TextView) (mTabHost.getTabWidget().getChildAt(1)
                            .findViewById(android.R.id.title)))
                            .setTextColor(getResources().getColor(
                                    R.color.headerbar_selected_tab_color));
                }

                fragmentTransaction.commit();
            }
        });

    }

    private void initViews() {
        mTabHost = findViewById(android.R.id.tabhost);
    }

    */
/*private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }*//*


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == MediaChooserConstants.BUCKET_SELECT_IMAGE_CODE) {
                addMedia(mSelectedImage, data.getStringArrayListExtra("list"));

            } */
/*else if (requestCode == MediaChooserConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));
                final AlertDialog alertDialog = MediaChooserConstants
                        .getDialog(ChoosePhotoFragmentActivity.this).create();
                alertDialog.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 2000ms
                        String fileUriString = fileUri.toString()
                                .replaceFirst("file:///", "/").trim();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        BucketImageFragment bucketImageFragment = (BucketImageFragment) fragmentManager
                                .findFragmentByTag("tab1");
                        if (bucketImageFragment != null) {
                            bucketImageFragment.getAdapter().addLatestEntry(fileUriString);
                            bucketImageFragment.getAdapter().notifyDataSetChanged();
                        }
                        alertDialog.dismiss();
                    }
                }, 5000);

            }*//*

        }
    }

    private void addMedia(ArrayList<String> list, ArrayList<String> input) {
        list.addAll(input);
    }

    public int convertDipToPixels(float dips) {
        return (int) (dips * ChoosePhotoFragmentActivity.this.getResources().getDisplayMetrics().density + 0.5f);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                finish();
                break;
        }
    }
    */
/*private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                MediaChooserConstants.folderName);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm ss").format(new Date());
        File mediaFile;
        if (type == MediaChooserConstants.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MediaChooserConstants.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }*//*


}
*/
