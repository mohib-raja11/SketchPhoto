package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketch.gallery;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R;

import java.io.File;
import java.util.ArrayList;


public class ImageFragment extends Fragment
{
    private ArrayList<String> mSelectedItems = new ArrayList<String>();
    private ArrayList<MediaModel> mGalleryModelList;
    private GridView mImageGridView;
    private View mView;
    private OnImageSelectedListener mCallback;
    private GridViewAdapter mImageAdapter;
    private Cursor mImageCursor;


    public ImageFragment()
    {
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try
        {
            mCallback = (OnImageSelectedListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement OnImageSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (mView == null)
        {
            mView = inflater.inflate(R.layout.view_grid_layout_media_chooser, container, false);

            mImageGridView = mView.findViewById(R.id.gridViewFromMediaChooser);


            if (getArguments() != null)
            {
                initPhoneImages(getArguments().getString("name"));
            }
            else
            {
                initPhoneImages();
            }

        }
        else
        {
            ((ViewGroup) mView.getParent()).removeView(mView);
            if (mImageAdapter == null || mImageAdapter.getCount() == 0)
            {
                Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), Toast.LENGTH_SHORT).show();
            }
        }

        return mView;
    }

    private void initPhoneImages(String bucketName)
    {
        try
        {
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            String searchParams;
            searchParams = "bucket_display_name = \"" + bucketName + "\"";

            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            mImageCursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, searchParams, null, orderBy + " DESC");

            setAdapter(mImageCursor);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initPhoneImages()
    {
        try
        {
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            mImageCursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");

            setAdapter(mImageCursor);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setAdapter(Cursor imagecursor)
    {

        if (imagecursor.getCount() > 0)
        {

            mGalleryModelList = new ArrayList<>();

            for (int i = 0; i < imagecursor.getCount(); i++)
            {
                imagecursor.moveToPosition(i);
                int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                MediaModel galleryModel = new MediaModel(imagecursor.getString(dataColumnIndex), false);
                mGalleryModelList.add(galleryModel);
            }


            mImageAdapter = new GridViewAdapter(getActivity(), 0, mGalleryModelList, false);
            mImageGridView.setAdapter(mImageAdapter);
        }
        else
        {
            Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), Toast.LENGTH_SHORT).show();
        }

        mImageGridView.setOnItemLongClickListener(new OnItemLongClickListener()
        {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {

                GridViewAdapter adapter = (GridViewAdapter) parent.getAdapter();
                MediaModel galleryModel = adapter.getItem(position);
                File file = new File(galleryModel.url);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri exportUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                {
                    exportUri = FileProvider.getUriForFile(getContext(), getActivity().getPackageName() + ".fileprovider", file);
                }

                else
                {
                    exportUri = Uri.fromFile(file);
                }


                intent.setDataAndType(exportUri, "image/*");
                startActivity(intent);
                return true;
            }
        });

        mImageGridView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id)
            {
                // update the mStatus of each category in the adapter
                GridViewAdapter adapter = (GridViewAdapter) parent.getAdapter();
                MediaModel galleryModel = adapter.getItem(position);


                if (!galleryModel.status)
                {
                    long size = MediaChooserConstants.ChekcMediaFileSize(new File(galleryModel.url), false);
                    if (size != 0)
                    {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.file_size_exeeded) + "  " + MediaChooserConstants.SELECTED_IMAGE_SIZE_IN_MB + " " + getActivity().getResources().getString(R.string.mb), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if ((MediaChooserConstants.MAX_MEDIA_LIMIT == MediaChooserConstants.SELECTED_MEDIA_COUNT))
                    {
                        if (MediaChooserConstants.SELECTED_MEDIA_COUNT < 2)
                        {
                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.max_limit_file) + "  " + MediaChooserConstants.SELECTED_MEDIA_COUNT + " " + getActivity().getResources().getString(R.string.file), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else
                        {
                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.max_limit_file) + "  " + MediaChooserConstants.SELECTED_MEDIA_COUNT + " " + getActivity().getResources().getString(R.string.files), Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                }

                // inverse the status
                galleryModel.status = !galleryModel.status;

                adapter.notifyDataSetChanged();

                if (galleryModel.status)
                {
                    mSelectedItems.add(galleryModel.url);
                    MediaChooserConstants.SELECTED_MEDIA_COUNT++;
                    Intent imageIntent = new Intent();
                    imageIntent.setAction(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
                    imageIntent.putStringArrayListExtra("list", mSelectedItems);
                    getActivity().sendBroadcast(imageIntent);
                    getActivity().finish();

                }
                else
                {
                    mSelectedItems.remove(galleryModel.url.trim());
                    MediaChooserConstants.SELECTED_MEDIA_COUNT--;
                }

                if (mCallback != null)
                {
                    mCallback.onImageSelected(mSelectedItems.size());
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("list", mSelectedItems);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                }

            }
        });
    }

    public ArrayList<String> getSelectedImageList()
    {
        return mSelectedItems;
    }

    public void addItem(String item)
    {
        if (mImageAdapter != null)
        {
            MediaModel model = new MediaModel(item, false);
            mGalleryModelList.add(0, model);
            mImageAdapter.notifyDataSetChanged();
        }
        else
        {
            initPhoneImages();
        }
    }

    public interface OnImageSelectedListener
    {
        void onImageSelected(int count);
    }
}
