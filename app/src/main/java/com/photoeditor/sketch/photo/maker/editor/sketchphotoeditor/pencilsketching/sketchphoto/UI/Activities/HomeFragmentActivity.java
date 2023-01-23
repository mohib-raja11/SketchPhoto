package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
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
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketch.gallery.ImageFragment;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketch.gallery.MediaChooserConstants;

public class HomeFragmentActivity extends FragmentActivity implements
        ImageFragment.OnImageSelectedListener {

    private static Uri fileUri;
    private final Handler handler = new Handler();
    private FragmentTabHost mTabHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_fragment);

        mTabHost = findViewById(android.R.id.tabhost);

        mTabHost.setup(this, getSupportFragmentManager(), R.id.realTabcontent);

        if (getIntent() != null && (getIntent().getBooleanExtra("isFromBucket", false))) {

            if (getIntent().getBooleanExtra("image", false)) {

                Bundle bundle = new Bundle();
                bundle.putString("name", getIntent().getStringExtra("name"));
                mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(getResources().getString(R.string.images_tab) + "     "), ImageFragment.class, bundle);

            } else {

                Bundle bundle = new Bundle();
                bundle.putString("name", getIntent().getStringExtra("name"));
            }
        } else {

            if (MediaChooserConstants.showImage) {

                mTabHost.addTab(
                        mTabHost.newTabSpec("tab1").setIndicator(getResources().getString(R.string.images_tab) + "      "), ImageFragment.class, null);
            }

        }

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {

            TextView textView = mTabHost.getTabWidget()
                    .getChildAt(i).findViewById(android.R.id.title);
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
            textView.setTextColor(getResources().getColor(
                    R.color.tabs_title_color));
            textView.setTextSize(convertDipToPixels(10));

        }

        if ((mTabHost.getTabWidget().getChildAt(0) != null)) {
            ((TextView) (mTabHost.getTabWidget().getChildAt(0)
                    .findViewById(android.R.id.title)))
                    .setTextColor(Color.WHITE);
        }

        if ((mTabHost.getTabWidget().getChildAt(1) != null)) {
            ((TextView) (mTabHost.getTabWidget().getChildAt(1)
                    .findViewById(android.R.id.title)))
                    .setTextColor(getResources().getColor(
                            R.color.headerbar_selected_tab_color));
        }

        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                ImageFragment imageFragment = (ImageFragment) fragmentManager
                        .findFragmentByTag("tab1");

                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();

                if (tabId.equalsIgnoreCase("tab1")) {

                    if (imageFragment != null) {

                        fragmentTransaction.show(imageFragment);
                    }
                    ((TextView) (mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title)))
                            .setTextColor(getResources().getColor(
                                    R.color.headerbar_selected_tab_color));
                    ((TextView) (mTabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title)))
                            .setTextColor(Color.WHITE);

                } else {

                    ((TextView) (mTabHost.getTabWidget().getChildAt(0)
                            .findViewById(android.R.id.title)))
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MediaChooserConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        fileUri));

                final AlertDialog alertDialog = MediaChooserConstants
                        .getDialog(HomeFragmentActivity.this).create();
                alertDialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5000ms
                        String fileUriString = fileUri.toString().replaceFirst("file:///", "/").trim();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        ImageFragment imageFragment = (ImageFragment) fragmentManager.findFragmentByTag("tab1");
                        if (imageFragment == null) {
                            ImageFragment newImageFragment = new ImageFragment();
                            newImageFragment.addItem(fileUriString);

                        } else {
                            imageFragment.addItem(fileUriString);
                        }
                        alertDialog.cancel();
                    }
                }, 5000);
            }
        }
    }

    @Override
    public void onImageSelected(int count) {
        if (mTabHost.getTabWidget().getChildAt(1) != null) {
            if (count != 0) {
                ((TextView) mTabHost.getTabWidget().getChildAt(1)
                        .findViewById(android.R.id.title))
                        .setText(getResources().getString(R.string.images_tab)
                                + "  " + count);

            } else {
                ((TextView) mTabHost.getTabWidget().getChildAt(1)
                        .findViewById(android.R.id.title))
                        .setText(getResources().getString(R.string.image));
            }
        } else {
            if (count != 0) {
                ((TextView) mTabHost.getTabWidget().getChildAt(0)
                        .findViewById(android.R.id.title))
                        .setText(getResources().getString(R.string.images_tab)
                                + "  " + count);

            } else {
                ((TextView) mTabHost.getTabWidget().getChildAt(0)
                        .findViewById(android.R.id.title))
                        .setText(getResources().getString(R.string.image));
            }
        }
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private void setHeaderBarCameraBackground(Drawable drawable) {
        int sdk = android.os.Build.VERSION.SDK_INT;

    }

    public int convertDipToPixels(float dips) {
        return (int) (dips
                * HomeFragmentActivity.this.getResources().getDisplayMetrics().density + 0.5f);
    }

}
