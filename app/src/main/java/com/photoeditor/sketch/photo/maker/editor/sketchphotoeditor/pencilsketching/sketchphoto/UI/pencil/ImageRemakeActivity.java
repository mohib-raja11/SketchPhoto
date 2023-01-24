package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI.pencil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.exifinterface.media.ExifInterface;

import com.edmodo.cropper.CropImageView;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.CSketchFilter;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.Ragnarok.BitmapFilter;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.SketchColorFilter;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.SketchColorFilter2;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.SketchFilter;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.SketchFilter2;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI.BaseActivity;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI.PhotoShare_Activity;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI.pencil.others.ActivityHandler;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.Utils.AppUtils;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.sketches.SecondSketchFilter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.UUID;

public class ImageRemakeActivity extends BaseActivity implements OnClickListener {

    public static int overlayid = -1;
    private int MaxResolution, imageheight, imagewidth, screenwidth;

    public static Bitmap pic_result, pic_forSketch, pic_forDraw;
    private Bitmap colorPencilBitmap = null, colorPencil2Bitmap, pencilsketchBitmap = null, pencil2Bitmap = null;
    private Bitmap simpleSketchbitmap1 = null, simpleSketchbitmap2 = null, comicBitmap = null;

    private String Imagepath, sendimagepath;

    private String[] tool_array;
    private final String[] as = {"Color", "Pencil 1", "Color 2", "Pencil 2", "Pencil 3", "Pencil 4", "Pencil 5", "Sepia"};


    private Float Orientation;

    private Animation anim_bottom_show, anim_btnapply, animhidebtn, animsgallerybtn, animshowbtndown, animshowbtnup;

    private LinearLayout crop_gallery, effect_gallery, pic_apply_layout, pic_btn_gallery, pic_donelayout;
    private RelativeLayout gallery_layout;
    private Uri imageuri;

    private CropImageView pic_cropImageView;
    private ImageView pic_imageview;
    private TextView txt_editor;
    private final ActivityHandler activityHelper;
    private FrameLayout viewContainer;
    private Dialog exit_dialog;
    private final Integer[] effectImages = {R.drawable.pic_eff_0, R.drawable.pic_eff_1, R.drawable.pic_eff_2, R.drawable.pic_eff_3, R.drawable.pic_eff_4, R.drawable.pic_eff_5, R.drawable.pic_eff_6, R.drawable.pic_eff_7};

    private boolean MoveBack = false, moveforword = true, lineOne = true, sketchDone = false;


    public ImageRemakeActivity() {

        pic_result = null;
        imageuri = null;
        Imagepath = null;
        tool_array = null;
        this.activityHelper = new ActivityHandler(this, this);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_remake);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        screenwidth = displaymetrics.widthPixels;
        MaxResolution = screenwidth;
        initializeViews();
    }

    private void initializeViews() {
        viewContainer = findViewById(R.id.viewContainer);
        pic_btn_gallery = findViewById(R.id.btn_gallery);
        pic_btn_gallery.setVisibility(View.VISIBLE);
        pic_donelayout = findViewById(R.id.pic_done_layout);
        pic_apply_layout = findViewById(R.id.pic_apply_layout);
        gallery_layout = findViewById(R.id.gallery_layout);
        gallery_layout.setVisibility(View.GONE);
        effect_gallery = findViewById(R.id.effect_gallery);
        crop_gallery = findViewById(R.id.crop_gallery);
        pic_cropImageView = findViewById(R.id.pic_CropImageView);
        pic_cropImageView.setGuidelines(1);
        pic_cropImageView.setImageResource(0);
        pic_cropImageView.setVisibility(View.GONE);
        effect_gallery.setVisibility(View.GONE);
        crop_gallery.setVisibility(View.GONE);
        txt_editor = findViewById(R.id.pic_txteditor);
        txt_editor.setText(R.string.editor);
        pic_imageview = findViewById(R.id.iv_imagemaker);


        setClicks();
    }

    private void setClicks() {
        pic_donelayout.setOnClickListener(this);
        pic_apply_layout.setOnClickListener(this);

        startAnimations();

    }

    private void startAnimations() {
        new LoadImageAsycTask().execute();

        animhidebtn = AnimationUtils.loadAnimation(this, R.anim.hide_button_anims);
        animsgallerybtn = AnimationUtils.loadAnimation(this, R.anim.rightleft_gallery_anims);
        animshowbtnup = AnimationUtils.loadAnimation(this, R.anim.show_button_anims_up);
        anim_btnapply = AnimationUtils.loadAnimation(this, R.anim.show_button_anims_up);
        animshowbtndown = AnimationUtils.loadAnimation(this, R.anim.show_button_anims_down);
        anim_bottom_show = AnimationUtils.loadAnimation(this, R.anim.hide_button_anims_up);

    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        imageuri = intent.getData();
        tool_array = intent.getStringArrayExtra("tool_title");
        MaxResolution = intent.getIntExtra("picresolution", screenwidth);

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
                options.inPreferredConfig = Config.ARGB_8888;
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

    @SuppressLint("ResourceType")
    private void stringMatching() {
        String[] as = tool_array;
        int i = as.length;
        int j = 0;
        do {
            if (j >= i) {
                return;
            }
            String s = as[j];
            @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.pic_btn_layout, null);
            ImageButton imagebutton = view.findViewById(R.id.btn_image);
            TextView textview = view.findViewById(R.id.btn_txt);
            imagebutton.setOnClickListener(this);
            textview.setOnClickListener(this);
            if ("CROP".equalsIgnoreCase(s)) {
                textview.setText(getString(R.string.edt_crop));
                imagebutton.setImageResource(R.drawable.pic_crop);
                imagebutton.setId(2);
                textview.setId(2);
                setIcon_Crop();
            }

            pic_btn_gallery.addView(view);
            j++;
        } while (true);
    }

    private void setIcon_Crop() {

        pic_imageview.setVisibility(View.GONE);
        pic_cropImageView.setImageBitmap(pic_result);
        pic_cropImageView.setVisibility(View.VISIBLE);
        Animationview(pic_btn_gallery, crop_gallery);
        AnimationviewTop(pic_apply_layout, pic_donelayout, 2);
        String[] as = {"custom", "1:1", "2:1", "1:2", "3:2", "2:3", "4:3", "4:6", "4:5", "5:6", "5:7", "9:16", "16:9"};
        int i = 0;
        do {
            if (i >= as.length) {
                return;
            }
            @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.pic_crop_layout, null);
            final Button btn_crop = view.findViewById(R.id.crop_btn);
            btn_crop.setId(i);
            btn_crop.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            btn_crop.setText(as[i]);
            crop_gallery.addView(view);

            btn_crop.setOnClickListener(view1 -> {
                switch (btn_crop.getId()) {
                    default:
                        return;

                    case 0: // '\0'
                        pic_cropImageView.setFixedAspectRatio(false);
                        return;

                    case 1: // '\001'
                        pic_cropImageView.setFixedAspectRatio(true);
                        pic_cropImageView.setAspectRatio(1, 1);
                        return;

                    case 2: // '\002'
                        pic_cropImageView.setFixedAspectRatio(true);
                        pic_cropImageView.setAspectRatio(2, 1);
                        return;

                    case 3: // '\003'
                        pic_cropImageView.setFixedAspectRatio(true);
                        pic_cropImageView.setAspectRatio(1, 2);
                        return;

                    case 4: // '\004'
                        pic_cropImageView.setFixedAspectRatio(true);
                        pic_cropImageView.setAspectRatio(3, 2);
                        return;

                    case 5: // '\005'
                        pic_cropImageView.setFixedAspectRatio(true);
                        pic_cropImageView.setAspectRatio(2, 3);
                        return;

                    case 6: // '\006'
                        pic_cropImageView.setFixedAspectRatio(true);
                        pic_cropImageView.setAspectRatio(4, 3);
                        return;

                    case 7: // '\007'
                        pic_cropImageView.setFixedAspectRatio(true);
                        pic_cropImageView.setAspectRatio(4, 6);
                        return;

                    case 8: // '\b'
                        pic_cropImageView.setFixedAspectRatio(true);
                        pic_cropImageView.setAspectRatio(4, 5);
                        return;

                    case 9: // '\t'
                        pic_cropImageView.setFixedAspectRatio(true);
                        pic_cropImageView.setAspectRatio(5, 6);
                        return;

                    case 10: // '\n'
                        pic_cropImageView.setFixedAspectRatio(true);
                        pic_cropImageView.setAspectRatio(5, 7);
                        return;

                    case 11: // '\013'
                        pic_cropImageView.setFixedAspectRatio(true);
                        pic_cropImageView.setAspectRatio(8, 10);
                        return;

                    case 12: // '\f'
                        pic_cropImageView.setFixedAspectRatio(true);
                        break;
                }
                pic_cropImageView.setAspectRatio(16, 9);
            });
            i++;
        } while (true);
    }

    private void setIcon_Effects() {

        int i = 0;
        do {
            if (i >= as.length) {
                return;
            }
            @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.pic_effect_layout, null);
            final ImageView imageView = view.findViewById(R.id.image);
            TextView textview = view.findViewById(R.id.txt_view);
            imageView.setId(i);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), effectImages[i]);
            textview.setText(as[i]);
            imageView.setImageBitmap(bitmap);
            effect_gallery.addView(view);
            imageView.setOnClickListener(view1 -> {

                Log.d("clickedId", "onClick: " + imageView.getId());
                switch (imageView.getId()) {

                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    default:
                        if (sketchDone) {
                            new SketchAsnyTask().execute(imageView.getId());
                        }
                        break;

                }

            });
            i++;
        } while (true);
    }

    @Override
    public void onBackPressed() {

        ExitDidalog(getString(R.string.pic_exit_txt));

    }

    public void Animationview(final View hideanimview, final View showanimview) {
        hideanimview.startAnimation(animhidebtn);
        animhidebtn.setAnimationListener(new AnimationListener() {

            public void onAnimationEnd(Animation animation) {
                hideanimview.setVisibility(View.GONE);
                showanimview.startAnimation(animsgallerybtn);
                showanimview.setVisibility(View.VISIBLE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                hideanimview.setVisibility(View.VISIBLE);
            }

        });
    }

    public void AnimationviewTop(final View showanimview, final View hideanimview, final int Btnid) {
        hideanimview.startAnimation(animshowbtnup);
        txt_editor.startAnimation(animshowbtnup);
        animshowbtnup.setAnimationListener(new AnimationListener() {

            public void onAnimationEnd(Animation animation) {
                txt_editor.startAnimation(animshowbtndown);
                hideanimview.setVisibility(View.GONE);
                showanimview.setVisibility(View.VISIBLE);
                showanimview.startAnimation(animshowbtndown);
                switch (Btnid) {
                    default:
                        txt_editor.setText(getString(R.string.edt_editor));
                        return;

                    case 1:
                        txt_editor.setText(getString(R.string.edt_effect));
                        return;

                    case 2:
                        txt_editor.setText(getString(R.string.edt_crop));
                        return;

                    case 3:
                        txt_editor.setText(getString(R.string.edt_vintage));
                        return;

                    case 4:
                        txt_editor.setText(getString(R.string.edt_frame));
                        return;

                    case 5:
                        txt_editor.setText(getString(R.string.edt_overlay));
                        return;

                    case 6:
                        txt_editor.setText(getString(R.string.edt_reset));
                        return;

                    case 7:
                        txt_editor.setText(getString(R.string.edt_border));
                        return;

                    case 8:
                        txt_editor.setText(getString(R.string.edt_orientation));
                        return;

                    case 9:
                        txt_editor.setText(getString(R.string.edt_editor));
                        break;
                }
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

        });
    }

    public void ExitDidalog(String s) {
        exit_dialog = new Dialog(this);
        exit_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        exit_dialog.setContentView(R.layout.pic_reset_dialog);
        ((TextView) exit_dialog.findViewById(R.id.pic_reset_txt)).setText(s);
        TextView textview = exit_dialog.findViewById(R.id.pic_dialog_yes);
        TextView textview1 = exit_dialog.findViewById(R.id.pic_dialog_no);
        textview.setText(getString(R.string.leave_edt));
        textview1.setText(getString(R.string.keep_edt));
        textview.setOnClickListener(view -> {
            exit_dialog.dismiss();
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();

        });
        textview1.setOnClickListener(view -> exit_dialog.dismiss());
        exit_dialog.show();
    }

    private void checkcropIV() {
        if (pic_cropImageView.getVisibility() == View.VISIBLE) {
            pic_cropImageView.setVisibility(View.GONE);
            pic_imageview.setVisibility(View.VISIBLE);
        }

        if (crop_gallery.getVisibility() == View.VISIBLE) {
            crop_gallery.setVisibility(View.GONE);
        }
        if (effect_gallery.getVisibility() == View.VISIBLE) {
            effect_gallery.setVisibility(View.GONE);
        }

    }

    public void PicMakerDidalog(String s) {
        final Dialog picmaker_dialog = new Dialog(this);
        picmaker_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        picmaker_dialog.setContentView(R.layout.pic_reset_dialog);
        ((TextView) picmaker_dialog.findViewById(R.id.pic_reset_txt)).setText(s);
        TextView textview = picmaker_dialog.findViewById(R.id.pic_dialog_yes);
        TextView textview1 = picmaker_dialog.findViewById(R.id.pic_dialog_no);
        textview.setText(getString(R.string.reset_edt));
        textview1.setText(getString(R.string.continue_edt));
        textview.setOnClickListener(view -> {
            Bitmap bitmap = pic_result;
            if (Imagepath != null) {
                picmaker_dialog.dismiss();

                Orientation = getImageOrientation(Imagepath);
                getAspectRatio(Imagepath, MaxResolution);
                pic_result = getResizedOriginalBitmap(Imagepath, Orientation);

                pic_imageview.setImageBitmap(pic_result);
                Toast.makeText(getApplicationContext(), "Your original image is back !!!", Toast.LENGTH_SHORT).show();
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    System.gc();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Invalid image path.", Toast.LENGTH_SHORT).show();
            }
        });
        textview1.setOnClickListener(view -> picmaker_dialog.dismiss());
        picmaker_dialog.show();
    }

    @Override
    public void onClick(View view) {
        int viewID;
        viewID = view.getId();
        if (viewID == 2) {
            if (effect_gallery.getVisibility() == View.VISIBLE) {
                effect_gallery.setVisibility(View.GONE);
            }

        }

        if (viewID == 8) {

            checkcropIV();

            AnimationviewTop(pic_apply_layout, pic_donelayout, 8);
        }

        if (viewID == 1) {

            checkcropIV();
            overlayid = -1;
            Animationview(pic_btn_gallery, effect_gallery);
            AnimationviewTop(pic_apply_layout, pic_donelayout, 1);

        }
        if (viewID == 3) {
            checkcropIV();
            overlayid = -1;

            AnimationviewTop(pic_apply_layout, pic_donelayout, 3);
        }

        if (viewID == 4) {

            checkcropIV();
            overlayid = -1;

            AnimationviewTop(pic_apply_layout, pic_donelayout, 4);
        }

        if (viewID == 5) {
            checkcropIV();
            overlayid = -1;

            AnimationviewTop(pic_apply_layout, pic_donelayout, 5);
            return;
        }

        if (viewID == 7) {
            checkcropIV();

        }

        if (viewID == 6) {
            checkcropIV();
            PicMakerDidalog("You are loosing your edited image.Do you want to reset?");

        } else if (viewID == R.id.pic_apply_layout) {

            if (crop_gallery.getVisibility() == View.VISIBLE) {
                Bitmap bitmap = pic_cropImageView.getCroppedImage();
                pic_result = bitmap.copy(Config.ARGB_8888, true);
                pic_forSketch = bitmap.copy(Config.ARGB_8888, true);
                pic_forDraw = bitmap.copy(Config.ARGB_8888, true);
                crop_gallery.setVisibility(View.GONE);
                pic_cropImageView.setVisibility(View.GONE);
                pic_cropImageView.setImageResource(0);
                pic_imageview.setVisibility(View.VISIBLE);
                pic_imageview.setImageBitmap(pic_result);

                new SketchAsnyTaskFirst().execute(6);

            }
            if (effect_gallery.getVisibility() == View.VISIBLE) {
                effect_gallery.setVisibility(View.GONE);
            }

            AnimationviewTop(pic_donelayout, pic_apply_layout, 9);
            pic_btn_gallery.startAnimation(anim_bottom_show);
            pic_btn_gallery.setVisibility(View.VISIBLE);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic_eff_image);
            setIcon_Effects();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                System.gc();
            }
            checkcropIV();
            overlayid = -1;

            Animationview(pic_btn_gallery, effect_gallery);
            AnimationviewTop(pic_donelayout, pic_apply_layout, 1);

        } else if (viewID == R.id.pic_done_layout) {

            saveBitmap(UUID.randomUUID().toString(), 100, pic_result);
            File file = new File(sendimagepath);
            if (file.exists()) {
                Uri uri = Uri.fromFile(file);
                final Intent intent = new Intent(ImageRemakeActivity.this, PhotoShare_Activity.class);
                intent.setData(uri);
                startActivity(intent);

            }
        }

    }

    public void onDestroy() {
        super.onDestroy();

        if (GPUImageFilterTools.overlayBmp != null && !GPUImageFilterTools.overlayBmp.isRecycled()) {
            GPUImageFilterTools.overlayBmp.recycle();
            GPUImageFilterTools.overlayBmp = null;
            System.gc();
        }
        if (pic_result != null && !pic_result.isRecycled()) {
            pic_result.recycle();
            pic_result = null;
            System.gc();
        }

        int i = as.length;
        int j = 0;
        do {
            if (j >= i) {
                animhidebtn.cancel();
                animsgallerybtn.cancel();
                animshowbtnup.cancel();
                animshowbtndown.cancel();
                anim_bottom_show.cancel();
                anim_btnapply.cancel();
                unbindDrawables(findViewById(R.id.mainlayout));
                return;
            }

            j++;
        } while (true);
    }

    private void unbindDrawables(View view) {
        try {
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
                ((ViewGroup) view).removeAllViews();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveBitmap(String s, int i, Bitmap bitmap) {

        String s1 = AppUtils.getAppFolderPath(this);
        new File(s1).mkdirs();

        File file;
        boolean flag;
        (new BitmapFactory.Options()).inSampleSize = 5;
        sendimagepath = (new StringBuilder(s1)).append(File.separator).append(s).append(".jpg").toString();
        file = new File(sendimagepath);
        flag = file.exists();
        String[] as = null;
        try {
            if (flag) {
                file.delete();
                file.createNewFile();
            } else {
                file.createNewFile();
            }

            FileOutputStream fileoutputstream = new FileOutputStream(file);
            BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(fileoutputstream);

            bitmap.compress(Bitmap.CompressFormat.PNG, i, bufferedoutputstream);
            bufferedoutputstream.flush();
            bufferedoutputstream.close();
            as = new String[1];
            as[0] = file.toString();
        } catch (NullPointerException nullpointerexception) {
            Log.e("TAG", "saveBitmap: NullPointerException = " + nullpointerexception.getMessage());
        } catch (IOException e) {

            e.printStackTrace();
        }
        MediaScannerConnection.scanFile(this, as, null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String s1, Uri uri) {

            }
        });
    }

    public Bitmap getSketchBitmap(Bitmap bm1, int type) {
        Bitmap SketchBitmap = bm1;
        switch (type) {
            case 0:
                SketchColorFilter2 sketchColorFilter2 = new SketchColorFilter2(ImageRemakeActivity.this, activityHelper);
                if (colorPencil2Bitmap == null) {
                    try {
                        colorPencil2Bitmap = sketchColorFilter2.getSketchFromBH(bm1);
                        SketchBitmap = colorPencil2Bitmap;
                    } catch (Throwable e) {

                        e.printStackTrace();
                    }
                } else {
                    SketchBitmap = colorPencil2Bitmap;
                }

                break;

            case 1:
                SketchFilter sketchFilter = new SketchFilter(ImageRemakeActivity.this, activityHelper);
                if (pencilsketchBitmap == null) {
                    try {
                        pencilsketchBitmap = sketchFilter.getSketchFromBH(bm1);
                        SketchBitmap = pencilsketchBitmap;
                    } catch (Throwable e) {

                        e.printStackTrace();
                    }
                } else {
                    SketchBitmap = pencilsketchBitmap;
                }

                break;
            case 2:
                SketchColorFilter printFilter = new SketchColorFilter(ImageRemakeActivity.this, activityHelper);

                if (colorPencilBitmap == null) {
                    try {
                        colorPencilBitmap = printFilter.getSketchFromBH(bm1);
                        SketchBitmap = colorPencilBitmap;
                    } catch (Throwable e) {

                        e.printStackTrace();
                    }
                } else {
                    SketchBitmap = colorPencilBitmap;
                }
                break;
            case 3:
                SketchFilter2 sketchFilter2 = new SketchFilter2(ImageRemakeActivity.this, activityHelper);

                if (pencil2Bitmap == null) {
                    try {
                        pencil2Bitmap = sketchFilter2.getSketchFromBH(bm1);
                        SketchBitmap = pencil2Bitmap;
                    } catch (Throwable e) {

                        e.printStackTrace();
                    }
                } else {
                    SketchBitmap = pencil2Bitmap;
                }
                break;
            case 4:
                CSketchFilter cSketchFilter = new CSketchFilter(ImageRemakeActivity.this, activityHelper);

                if (comicBitmap == null) {
                    try {
                        comicBitmap = cSketchFilter.getSketchFromBH(bm1);
                        SketchBitmap = comicBitmap;
                    } catch (Throwable e) {

                        e.printStackTrace();
                    }
                } else {
                    SketchBitmap = comicBitmap;
                }
                break;
            case 5:

                if (simpleSketchbitmap1 == null) {
                    try {
                        Bitmap greyScaleCoppy = toGrayscale(bm1);

                        Bitmap invertCopy = invert(greyScaleCoppy);

                        Bitmap blurImage = fastblur(invertCopy, 7);
                        if (!invertCopy.isRecycled()) {
                            invertCopy.recycle();
                            System.gc();
                        }
                        simpleSketchbitmap1 = ColorDodgeBlend(blurImage, greyScaleCoppy);
                        if (!greyScaleCoppy.isRecycled()) {
                            greyScaleCoppy.recycle();
                            System.gc();
                        }
                        if (!blurImage.isRecycled()) {
                            blurImage.recycle();
                            System.gc();
                        }
                        simpleSketchbitmap1 = toGrayscale(simpleSketchbitmap1);
                        SketchBitmap = simpleSketchbitmap1;
                    } catch (Throwable e) {

                        e.printStackTrace();
                    }
                } else {
                    SketchBitmap = simpleSketchbitmap1;
                }

                break;
            case 6:
                SecondSketchFilter secondSketchFilter = new SecondSketchFilter();

                if (simpleSketchbitmap2 == null) {
                    try {
                        simpleSketchbitmap2 = secondSketchFilter.getSimpleSketch(bm1);

                        SketchBitmap = simpleSketchbitmap2;
                    } catch (Throwable e) {

                        e.printStackTrace();
                    }
                } else {
                    SketchBitmap = simpleSketchbitmap2;
                }

                break;
            case 7:
                if (simpleSketchbitmap2 == null) {
                    try {
                        SecondSketchFilter secondSketchFilter1 = new SecondSketchFilter();
                        simpleSketchbitmap2 = secondSketchFilter1.getSimpleSketch(bm1);

                        SketchBitmap = ConvertToSepia(simpleSketchbitmap2);

                    } catch (Throwable e) {

                        e.printStackTrace();
                    }
                } else {
                    SketchBitmap = ConvertToSepia(simpleSketchbitmap2);
                }
                break;
            case 8:

                SketchBitmap = BitmapFilter.changeStyle(bm1, BitmapFilter.OIL_STYLE);

                break;
            case 9:

                SketchBitmap = BitmapFilter.changeStyle(bm1, BitmapFilter.SKETCH_STYLE);

                break;
        }

        return SketchBitmap;
        // result = Color
    }

    public Bitmap ConvertToSepia(Bitmap sampleBitmap) {
        float[] sepMat = {0.3930000066757202f, 0.7689999938011169f, 0.1889999955892563f, 0, 0, 0.3490000069141388f, 0.6859999895095825f, 0.1679999977350235f, 0, 0, 0.2720000147819519f, 0.5339999794960022f, 0.1309999972581863f, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1};
        ColorMatrix sepiaMatrix = new ColorMatrix();
        sepiaMatrix.set(sepMat);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(sepiaMatrix);
        Bitmap rBitmap = sampleBitmap.copy(Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);

        Canvas myCanvas = new Canvas(rBitmap);
        myCanvas.drawBitmap(rBitmap, 0, 0, paint);
        return rBitmap;
    }

    public Bitmap fastblur(Bitmap sentBitmap, int radius) {

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;
        int[] r = new int[wh];
        int[] g = new int[wh];
        int[] b = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int[] vmin = new int[Math.max(w, h)];
        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int[] dv = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }
        yw = yi = 0;
        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;
        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;
                sir = stack[i + radius];
                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];
                rbs = r1 - Math.abs(i);
                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];
                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    public Bitmap invert(Bitmap original) {
        // Create mutable Bitmap to invert, argument true makes it mutable
        Bitmap inversion = original.copy(Config.ARGB_8888, true);
        final int RGB_MASK = 0x00FFFFFF;
        // Get info about Bitmap
        int width = inversion.getWidth();
        int height = inversion.getHeight();
        int pixels = width * height;
        int[] pixel = new int[pixels];
        inversion.getPixels(pixel, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels; i++)
            pixel[i] ^= RGB_MASK;
        inversion.setPixels(pixel, 0, width, 0, 0, width, height);
        return inversion;
    }

    public Bitmap ColorDodgeBlend(Bitmap source, Bitmap layer) {
        Bitmap base = source.copy(Config.ARGB_8888, true);
        Bitmap blend = layer.copy(Config.ARGB_8888, false);

        IntBuffer buffBase = IntBuffer.allocate(base.getWidth() * base.getHeight());
        base.copyPixelsToBuffer(buffBase);
        buffBase.rewind();

        IntBuffer buffBlend = IntBuffer.allocate(blend.getWidth() * blend.getHeight());
        blend.copyPixelsToBuffer(buffBlend);
        buffBlend.rewind();

        IntBuffer buffOut = IntBuffer.allocate(base.getWidth() * base.getHeight());
        buffOut.rewind();

        while (buffOut.position() < buffOut.limit()) {

            int filterInt = buffBlend.get();
            int srcInt = buffBase.get();
            int redValueFilter = Color.red(filterInt);
            int greenValueFilter = Color.green(filterInt);
            int blueValueFilter = Color.blue(filterInt);
            int redValueSrc = Color.red(srcInt);
            int greenValueSrc = Color.green(srcInt);
            int blueValueSrc = Color.blue(srcInt);
            int redValueFinal = colordodge(redValueFilter, redValueSrc);
            int greenValueFinal = colordodge(greenValueFilter, greenValueSrc);
            int blueValueFinal = colordodge(blueValueFilter, blueValueSrc);
            int pixel = Color.argb(255, redValueFinal, greenValueFinal, blueValueFinal);
            // here
            buffOut.put(pixel);
        }
        buffOut.rewind();
        base.copyPixelsFromBuffer(buffOut);
        blend.recycle();

        return base;
    }

    private int colordodge(int in1, int in2) {
        float image = (float) in2;
        float mask = (float) in1;
        return ((int) ((image == 255) ? image : Math.min(255, (((long) mask << 8) / (255 - image)))));
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    @SuppressLint("StaticFieldLeak")
    public class LoadImageAsycTask extends AsyncTask<Void, Void, Void> {
        Float Orientation;
        ProgressDialog dialog;
        boolean getimage = false;

        @Override
        protected void onPreExecute() {

            dialog = ProgressDialog.show(ImageRemakeActivity.this, "", "Loading...");
            dialog.setCancelable(false);
            getIntentExtra();
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            if (imageuri == null) {
                getimage = false;
            } else {
                Imagepath = getRealPathFromURI(imageuri);
                if (Imagepath != null && (Imagepath.endsWith(".png") || Imagepath.endsWith(".jpg") || Imagepath.endsWith(".jpeg") || Imagepath.endsWith(".bmp"))) {

                    Orientation = getImageOrientation(Imagepath);
                    getAspectRatio(Imagepath, MaxResolution);
                    pic_result = getResizedOriginalBitmap(Imagepath, Orientation);
                    pic_forSketch = getResizedOriginalBitmap(Imagepath, Orientation);
                    pic_forDraw = getResizedOriginalBitmap(Imagepath, Orientation);
                    getimage = true;

                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            gallery_layout.setVisibility(View.VISIBLE);
            if (getimage) {
                if (pic_result == null || pic_result.getHeight() <= 5 || pic_result.getWidth() <= 5) {
                    Toast.makeText(getApplicationContext(), "Image Format not supported .", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    pic_imageview.setImageBitmap(pic_result);
                    stringMatching();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Unsupported media file.", Toast.LENGTH_SHORT).show();
                finish();
            }
            dialog.dismiss();
            super.onPostExecute(result);
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class SketchAsnyTaskFirst extends AsyncTask<Integer, Void, Void> {

        ProgressDialog dialogD;
        Bitmap eff;
        Bitmap bitmap = eff;

        @Override
        protected void onPreExecute() {


            dialogD = ProgressDialog.show(ImageRemakeActivity.this, "", "Sketching...");
            dialogD.setCancelable(false);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {

            eff = getSketchBitmap(pic_forDraw, params[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            pic_result = eff;

            viewContainer.setVisibility(View.VISIBLE);
            viewContainer.addView(new BlurView(ImageRemakeActivity.this));
            pic_result = eff;
            pic_imageview.setVisibility(View.INVISIBLE);

            dialogD.dismiss();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                System.gc();
            }

            super.onPostExecute(result);
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class SketchAsnyTask extends AsyncTask<Integer, Void, Void> {

        ProgressDialog dialogD;
        Bitmap eff;
        Bitmap bitmap = eff;

        @Override
        protected void onPreExecute() {
            dialogD = ProgressDialog.show(ImageRemakeActivity.this, "", "Sketching...");
            dialogD.setCancelable(false);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {

            eff = getSketchBitmap(pic_forSketch, params[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            pic_result = eff;
            pic_imageview.setImageBitmap(pic_result);
            // mGPUImage.setImage(eff);

            dialogD.dismiss();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                System.gc();
            }

            super.onPostExecute(result);
        }

    }

    public class BlurView extends View {

        PointF mImagePos = new PointF();
        PointF mImageSource = new PointF();
        PointF mImageTarget = new PointF();
        long mInterpolateTime;

        Bitmap bmOverlay;
        Bitmap bitmap;
        Canvas pcanvas;
        int x = 0;
        int y = 0;
        int r = 0;
        int mWidth, mHeight;
        Context context;
        int Tilltime = 0;
        private final Paint mPaint;

        public BlurView(Context context) {
            super(context);
            this.context = context;
            setFocusable(true);
            setBackgroundColor(Color.TRANSPARENT);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(Color.TRANSPARENT);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
            mPaint.setAntiAlias(true);

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            bmOverlay = Bitmap.createBitmap(pic_forDraw.getWidth(), pic_forDraw.getHeight(), Config.ARGB_8888);
            pcanvas = new Canvas(bmOverlay);
            pic_forDraw.eraseColor(Color.WHITE);

        }

        @SuppressLint("DrawAllocation")
        @Override
        protected void onDraw(Canvas canvas) {
            // draw a circle that is erasing bitmap
            super.onDraw(canvas);
            canvas.drawBitmap(pic_result, 0, 0, null);
            pcanvas.drawBitmap(pic_forDraw, 0, 0, null);
            pcanvas.drawCircle(mImagePos.x, mImagePos.y, pic_result.getHeight() / 20, mPaint);
            update();
            invalidate();
            canvas.drawBitmap(bmOverlay, 0, 0, null);
            Canvas bitmapCanvas = new Canvas(pic_forDraw);
            bitmapCanvas.drawBitmap(pic_result, 0, 0, null);
            bitmapCanvas.drawBitmap(bmOverlay, 0, 0, null);
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.drawing_hand), mImagePos.x, mImagePos.y, null);

        }

        public void update() {

            if (Tilltime < 400) {
                final long INTERPOLATION_LENGTH = 200;
                long time = SystemClock.uptimeMillis();
                if (time - mInterpolateTime > INTERPOLATION_LENGTH) {
                    mImageSource.set(mImageTarget);
                    mImageTarget.x = (float) (Math.random() * pic_forDraw.getWidth());
                    mImageTarget.y = (float) (Math.random() * pic_forDraw.getHeight());
                    mInterpolateTime = time;
                }

                float t = (float) (time - mInterpolateTime) / INTERPOLATION_LENGTH;
                t = t * t * (3 - 2 * t);
                mImagePos.x = mImageSource.x + (mImageTarget.x - mImageSource.x) * t;
                mImagePos.y = mImageSource.y + (mImageTarget.y - mImageSource.y) * t;
                Tilltime++;
            } else {
                if (lineOne) {
                    mImagePos.y = 0;
                    lineOne = false;
                }
                boolean nextLine = false;

                if (mImagePos.y <= pic_forDraw.getHeight()) {

                    if (mImagePos.x <= 0) {
                        moveforword = true;
                        MoveBack = false;
                        nextLine = true;
                        mImagePos.y += pic_result.getHeight() / 20;
                    } else if (mImagePos.x >= pic_forDraw.getWidth()) {
                        moveforword = false;
                        MoveBack = true;
                        nextLine = true;
                        mImagePos.y += pic_result.getHeight() / 20;
                    }

                    if (moveforword) {
                        mImagePos.x += pic_forDraw.getWidth() / 20;

                    } else if (MoveBack) {

                        mImagePos.x -= pic_result.getWidth() / 20;
                    }

                } else {
                    sketchDone = true;
                    viewContainer.setVisibility(INVISIBLE);
                    pic_imageview.setVisibility(View.VISIBLE);
                    pic_imageview.setImageBitmap(pic_result);
                }

            }
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {

            super.onSizeChanged(w, h, oldw, oldh);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            if (pic_result != null) {
                setMeasuredDimension(pic_result.getWidth(), pic_result.getHeight());
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {

                case MotionEvent.ACTION_DOWN:

                case MotionEvent.ACTION_MOVE: {
                    x = (int) ev.getX();
                    y = (int) ev.getY();
                    invalidate();
                    Log.i("TAG", "onTouchEvent: x= " + x);

                    break;
                }

                case MotionEvent.ACTION_UP:

                    Log.i("TAG", "onTouchEvent: x= " + x);
                    break;

            }
            return true;
        }

    }

}
