package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R;
import com.squareup.picasso.Picasso;

import java.io.File;


public class FullScreenImageActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screenimage);

        String path = getIntent().getStringExtra("path");
        ImageView photoView = findViewById(R.id.photo_view);

        Picasso.get().load(new File(path)).into(photoView);

    }
}
