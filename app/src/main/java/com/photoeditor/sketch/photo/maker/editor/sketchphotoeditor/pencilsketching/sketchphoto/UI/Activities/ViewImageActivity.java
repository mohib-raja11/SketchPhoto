package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ViewImageActivity extends Activity {
    private final int three_seconds = 3000;
    private Bitmap bitmapForShare;
    private String imgUrl = null;
    private ImageView mainImageView;
    private boolean tapped = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_view_the_image);

        mainImageView = findViewById(R.id.mainImageView);

        imgUrl = getIntent().getStringExtra("ImgUrl");
        Picasso.get().load(new File(imgUrl)).into(mainImageView);
        bitmapForShare = BitmapFactory.decodeFile(imgUrl);

        delay3Seconds();

    }

    public void delay3Seconds() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tapped = false;
            }
        }, three_seconds);
    }

    public void onClick(View arg0) {

        if (tapped) {
            return;
        }

        tapped = true;
        delay3Seconds();

        switch (arg0.getId()) {
            case R.id.mainImageView:
                showFullImage();
                return;
            case R.id.shareBtn:
                shareImage();
                return;

            case R.id.deleteBtn:
                deleteImage();
                return;

            case R.id.btn_set_wallpaper:
                setWallpaper();
                break;
            case R.id.iv_close:
                finish();
                break;
        }
    }

    private void showFullImage() {
        Intent intent = new Intent(ViewImageActivity.this, FullScreenImageActivity.class);
        intent.putExtra("path", imgUrl);
        startActivityForResult(intent, 2);
    }

    private void shareImage() {
        Uri uri = getImageUri(this, bitmapForShare);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpg");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_img_via)));
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void setWallpaper() {
        if (bitmapForShare == null) {
            return;
        }
        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            myWallpaperManager.setBitmap(bitmapForShare);

            Toast toast = Toast.makeText(getApplicationContext(), R.string.wallpaper_succesfully_set, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishAffinity();
                }
            }, 1000);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleted() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.removeOpt));
        builder.setMessage(getResources().getString(R.string.removeTxt));
        builder.setPositiveButton(getResources().getString(R.string.yestxt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

                File file = new File(imgUrl);
                if (file.delete()) {
                    setResult(-1);
                    finish();
                    return;
                }
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorImg), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.canceltxt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void deleteImage() {
        deleted();

    }
}
