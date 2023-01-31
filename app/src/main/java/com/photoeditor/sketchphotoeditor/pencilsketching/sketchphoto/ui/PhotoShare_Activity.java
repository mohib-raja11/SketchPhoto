package com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.exifinterface.media.ExifInterface;

import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R;
import com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.constant.SaveToStorageUtil;
import com.photoeditor.sketchphotoeditor.pencilsketching.sketchphoto.ui.pencil.GPUImageFilterTools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

public class PhotoShare_Activity extends BaseActivity {
    public Boolean savedok;
    public GPUImageFilter mFilter;
    public GPUImage mGPUImage;
    private Uri shareuri;
    private int MaxResolution;
    private Bitmap share_bitmap;
    private int screenwidth, imagewidth, imageheight, currentapiVersion;
    private ImageView pic_imageview;
    private LinearLayout color_layout;
    private ImageButton effect_Button, red_Button, blue_Button, green_Button, doneButton, reset_Button;

    public PhotoShare_Activity() {
        savedok = false;
        shareuri = null;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoshare_activity);


        initViews();

        DisplayMetrics displaymetrics;
        displaymetrics = new DisplayMetrics();
        mGPUImage = new GPUImage(this);
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenwidth = displaymetrics.widthPixels;
        MaxResolution = screenwidth;
        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        pic_imageview = findViewById(R.id.finalimg);

        loadImageAsycTask();

        green_Button.setOnClickListener(v -> {
            new EffectAsnyTask().execute(0);
            doneButton.setVisibility(View.VISIBLE);
            reset_Button.setVisibility(View.VISIBLE);
        });
        blue_Button.setOnClickListener(v -> {
            new EffectAsnyTask().execute(1);
            doneButton.setVisibility(View.VISIBLE);
            reset_Button.setVisibility(View.VISIBLE);
        });
        red_Button.setOnClickListener(v -> {
            new EffectAsnyTask().execute(2);
            doneButton.setVisibility(View.VISIBLE);
            reset_Button.setVisibility(View.VISIBLE);
        });
        reset_Button.setOnClickListener(v -> {

            loadImageAsycTask();

            color_layout.setVisibility(View.INVISIBLE);
        });
        doneButton.setOnClickListener(v -> color_layout.setVisibility(View.INVISIBLE));
        effect_Button.setOnClickListener(v -> color_layout.setVisibility(View.VISIBLE));

    }

    private void initViews() {
        color_layout = findViewById(R.id.top_color_button);
        effect_Button = findViewById(R.id.effect_button);
        red_Button = findViewById(R.id.red_button);
        blue_Button = findViewById(R.id.blue_button);
        green_Button = findViewById(R.id.green_button);
        doneButton = findViewById(R.id.done_button);
        reset_Button = findViewById(R.id.reset_button);


        findViewById(R.id.btnDelete).setOnClickListener(v -> deleteCurrentImage());
        findViewById(R.id.save_btn).setOnClickListener(v -> {
            Bitmap bitmap = mGPUImage.getBitmapWithFilterApplied();
            saveImage(bitmap);
        });
        findViewById(R.id.share_btn).setOnClickListener(v -> {
            Bitmap bitmap = mGPUImage.getBitmapWithFilterApplied();
            Uri uri = getImageUri(PhotoShare_Activity.this, bitmap);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpg");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_img_via)));
        });


    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        shareuri = intent.getData();
        MaxResolution = intent.getIntExtra("picresolution", screenwidth);

    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {
            cursor.moveToFirst();
            @SuppressLint("Range") String s = cursor.getString(cursor.getColumnIndex("_data"));
            cursor.close();
            return s;
        }
    }

    private float getImageOrientation(String s) {
        int i;
        float f;
        try {
            i = (new ExifInterface(s)).getAttributeInt("Orientation", 1);
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            return 0.0F;
        }
        if (i == 6) {
            f = 90F;
        } else {
            if (i == 3) {
                return 180F;
            }
            f = 0.0F;
            if (i == 8) {
                return 270F;
            }
        }
        return f;
    }

    private void getAspectRatio(String s, float f) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(s, options);
        float f1 = (float) options.outWidth / (float) options.outHeight;
        float f2;
        float f3;
        if (f1 > 1.0F) {
            f3 = f;
            f2 = f3 / f1;
        } else {
            f2 = f;
            f3 = f2 * f1;
        }
        imagewidth = (int) f3;
        imageheight = (int) f2;
    }

    public Bitmap getResizedOriginalBitmap(String s, float f2) {
        BitmapFactory.Options options;
        int i;
        int j;
        int k;
        int l;
        int i1;
        float f;
        float f1;
        Bitmap bitmap;
        Matrix matrix;
        try {
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(s), null, options);
            i = options.outWidth;
            j = options.outHeight;
            k = imagewidth;
            l = imageheight;

        } catch (FileNotFoundException filenotfoundexception) {
            return null;
        }
        i1 = 1;
        do {
            if (i / 2 <= k) {
                f = (float) k / (float) i;
                f1 = (float) l / (float) j;
                options.inJustDecodeBounds = false;
                options.inDither = false;
                options.inSampleSize = i1;
                options.inScaled = false;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(s), null, options);
                    matrix = new Matrix();
                    matrix.postScale(f, f1);
                    matrix.postRotate(f2);
                    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            i /= 2;
            j /= 2;
            i1 *= 2;
        } while (true);

    }

    public void saveImage(Bitmap bitmap) {

        SaveToStorageUtil.saveReal(bitmap, PhotoShare_Activity.this);

        Toast.makeText(PhotoShare_Activity.this, getString(R.string.img_saved_successfully), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> {

            Intent intent = new Intent(PhotoShare_Activity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }, 1000);

    }

    protected void onDestroy() {
        if (savedok) {
            if (share_bitmap != null && !share_bitmap.isRecycled()) {
                share_bitmap.recycle();
                share_bitmap = null;
                System.gc();
            }
            savedok = false;
            shareuri = null;
        } else {
            if (shareuri == null) {
                if (share_bitmap != null && !share_bitmap.isRecycled()) {
                    share_bitmap.recycle();
                    share_bitmap = null;
                    System.gc();
                }
                savedok = false;
                shareuri = null;
            } else {
                File file = new File(shareuri.getPath());
                if (file.exists()) {
                    file.delete();
                }
                if (currentapiVersion > 18) {
                    scanFile(shareuri.getPath(), true);
                }
                if (currentapiVersion <= 18) {
                    try {
                        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                        intent.setData(Uri.fromFile(new File(shareuri.getPath())));
                        sendBroadcast(intent);
                    } catch (NullPointerException nullpointerexception) {
                        nullpointerexception.printStackTrace();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }
        super.onDestroy();
    }

    private void scanFile(String s, final boolean isDelete) {
        try {
            MediaScannerConnection.scanFile(getApplicationContext(), new String[]{s}, null, new MediaScannerConnection.OnScanCompletedListener() {

                public void onScanCompleted(String s1, Uri uri) {
                    if (isDelete && uri != null) {
                        getApplicationContext().getContentResolver().delete(uri, null, null);
                    }
                }

            });
            return;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    protected void Image_effect(int i) {
        GPUImageFilterTools.Applyeffects(i, this, gpuimagefilter -> {
            switchFilterTo(gpuimagefilter);
            mGPUImage.requestRender();
        });
    }

    public void switchFilterTo(GPUImageFilter gpuimagefilter) {
        if (mFilter == null || gpuimagefilter != null) {
            mFilter = gpuimagefilter;
            mGPUImage.setFilter(mFilter);

        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void deleteCurrentImage() {
        Intent intent = new Intent(PhotoShare_Activity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    public class EffectAsnyTask extends AsyncTask<Integer, Void, Void> {

        Bitmap eff;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            Image_effect(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            Bitmap bitmap = eff;
            try {
                eff = mGPUImage.getBitmapWithFilterApplied(share_bitmap);
                pic_imageview.setImageBitmap(eff);
            } catch (NullPointerException nullpointerexception) {
                pic_imageview.setImageBitmap(share_bitmap);
                mGPUImage.setImage(share_bitmap);
            } catch (OutOfMemoryError outofmemoryerror) {
                pic_imageview.setImageBitmap(share_bitmap);
                mGPUImage.setImage(share_bitmap);
                System.gc();
            } catch (Exception ignored) {
            }
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                System.gc();
            }

            super.onPostExecute(result);
        }

    }

    boolean getimage;

    public void loadImageAsycTask() {

        getimage = false;

        ProgressDialog dialog = ProgressDialog.show(PhotoShare_Activity.this, "", "Loading...");
        dialog.setCancelable(false);
        getIntentExtra();

        getMExecutor().runWorker(() -> {

            float Orientation;


            String imagepath = getRealPathFromURI(shareuri);
            if (imagepath != null && imagepath.endsWith(".png") || imagepath.endsWith(".jpg") || imagepath.endsWith(".jpeg") || imagepath.endsWith(".bmp")) {
                Orientation = Float.valueOf(getImageOrientation(imagepath));
                getAspectRatio(imagepath, MaxResolution);
                share_bitmap = getResizedOriginalBitmap(imagepath, Orientation);
                getimage = true;
            }

            runOnUiThread(() -> {

                if (getimage) {
                    if (share_bitmap == null || share_bitmap.getHeight() <= 5 || share_bitmap.getWidth() <= 5) {
                        Toast.makeText(getApplicationContext(), "Image Format not supported .", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        pic_imageview.setImageBitmap(share_bitmap);
                        mGPUImage.setImage(share_bitmap);

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Unsupported media file.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                dialog.dismiss();

            });

            return null;
        });
    }

}
