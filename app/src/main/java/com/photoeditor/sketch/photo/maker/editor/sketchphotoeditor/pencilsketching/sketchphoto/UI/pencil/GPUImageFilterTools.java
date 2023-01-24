package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI.pencil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;

import java.util.LinkedList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter;

public class GPUImageFilterTools {

    static Bitmap overlayBmp = null;

    public static void showDialog(final Context context,
                                  final OnGpuImageFilterChosenListener listener) {
        final FilterList filters = new FilterList();
        filters.addFilter("Posterize", FilterType.POSTERIZE);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose a filter");
        builder.setItems(
                filters.names.toArray(new String[filters.names.size()]),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                                        final int item) {
                        listener.onGpuImageFilterChosenListener(createFilterForType(
                                context, filters.filters.get(item)));
                    }
                });
        builder.create().show();
    }

    private static GPUImageFilter createFilterForType(final Context context,
                                                      final FilterType type) {
        switch (type) {

            case POSTERIZE:
                return new GPUImagePosterizeFilter();
            case RED:
                return new GPUImageRGBFilter(1.5F, 1.0F, 1.0F);
            case GREEN:
                return new GPUImageRGBFilter(1.0F, 1.5F, 1.0F);
            case BLUE:
                return new GPUImageRGBFilter(1.0F, 1.0F, 1.5F);
            default:
                throw new IllegalStateException("No filter of that type!");
        }

    }

    public interface OnGpuImageFilterChosenListener {
        void onGpuImageFilterChosenListener(GPUImageFilter filter);
    }

    private enum FilterType {
        POSTERIZE, RED, GREEN, BLUE
    }

    private static class FilterList {
        public List<String> names = new LinkedList<String>();
        public List<FilterType> filters = new LinkedList<FilterType>();

        public void addFilter(final String name, final FilterType filter) {
            names.add(name);
            filters.add(filter);
        }
    }

    public static void Applyeffects(int i, Context context,
                                    OnGpuImageFilterChosenListener ongpuimagefilterchosenlistener) {
        switch (i) {
            default:
                return;

            case 2:
                //
                ongpuimagefilterchosenlistener
                        .onGpuImageFilterChosenListener(createFilterForType(
                                context, FilterType.RED));
                return;

            case 1:
                //
                ongpuimagefilterchosenlistener
                        .onGpuImageFilterChosenListener(createFilterForType(
                                context, FilterType.BLUE));
                return;

            case 0:
                //
                ongpuimagefilterchosenlistener
                        .onGpuImageFilterChosenListener(createFilterForType(
                                context, FilterType.GREEN));

                return;

            case 3:
                ongpuimagefilterchosenlistener
                        .onGpuImageFilterChosenListener(createFilterForType(
                                context, FilterType.POSTERIZE));
                return;

        }
    }

}
