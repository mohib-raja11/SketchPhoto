package com.sketch.ui;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.sketch.R;
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
