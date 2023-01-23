package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R;
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.Utils.AppUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class MyGalleryActivity extends Activity {

    RecyclerView recyclerView;
    RecyclerAdapter mainAdapter;
    ArrayList<String> name_list = new ArrayList<>();
    ArrayList<String> name_path_list = new ArrayList<>();
    String extention = ".jpg";
    String extention_png = ".png";
    Context context;
    TextView tvNoItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mygallery);

        context = this;

        tvNoItem = findViewById(R.id.tvNoItem);
        recyclerView = findViewById(R.id.recyclerView);

        mainAdapter = new RecyclerAdapter();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mainAdapter);

    }

    @Override
    protected void onResume() {
        loadingImages();
        super.onResume();
    }

    //***********************************Mohib: getting list of saved files************************************
    private void loadingImages() {

        String rootPath = AppUtils.getAppFolderPath(context);

        File directory = new File(rootPath);
        File[] listFile = directory.listFiles();

        if (listFile != null && listFile.length > 0) {
            //Mohib: if saved files are then show dialog of all files to choose
            if (listFile != null) {
                name_list.clear();
                name_path_list.clear();
                for (int i = 0; i < listFile.length; i++) {

                    if (listFile[i].getName().endsWith(extention) || listFile[i].getName().endsWith(extention_png)) {
                        name_list.add(listFile[i].getName());
                        name_path_list.add(listFile[i].getAbsolutePath());

                    }

                }
                mainAdapter.notifyDataSetChanged();
            }

        } else {
            Toast.makeText(this, "no file found", Toast.LENGTH_SHORT).show();
        }
        if (name_path_list.size() > 0) {
            tvNoItem.setVisibility(View.GONE);
        } else {
            tvNoItem.setVisibility(View.VISIBLE);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolderView> {

        public RecyclerAdapter() {

        }

        @NonNull
        @Override
        public MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_gallery, parent, false);
            return new MyHolderView(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolderView myHolderView, int pos) {

            final int adapterPos = pos;
            myHolderView.tvName.setText(name_list.get(adapterPos));

            Picasso.get().load(new File(name_path_list.get(adapterPos))).into(myHolderView.iv1, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {

                }
            });
            myHolderView.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MyGalleryActivity.this, ViewImageActivity.class);
                    intent.putExtra("ImgUrl", name_path_list.get(adapterPos));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return name_list.size();
        }

        public class MyHolderView extends RecyclerView.ViewHolder {
            TextView tvName;
            ImageView iv1;

            public MyHolderView(@NonNull View itemView) {
                super(itemView);

                tvName = itemView.findViewById(R.id.tvName);
                iv1 = itemView.findViewById(R.id.ivMain);
            }
        }
    }
}
