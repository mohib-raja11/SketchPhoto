package wishpool.sketch.ui.pencil;

import android.graphics.Bitmap;
import android.util.Log;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter;

public class GPUImageFilterTools {

    static Bitmap overlayBmp = null;
    public  static float redValue = 1.5f;
    public  static float greenValue = 1.0f;
    public  static float blueValue = 1.0f;

    private static GPUImageFilter createFilterForType(final FilterType type) {
        switch (type) {

            case CUSTOM:
                return new GPUImageRGBFilter(redValue, greenValue, blueValue);

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
        CUSTOM, POSTERIZE, RED, GREEN, BLUE
    }

    public static void Applyeffects(int pos, OnGpuImageFilterChosenListener ongpuimagefilterchosenlistener) {

        Log.d("mLogs", "Applyeffects: pos = "+pos);
        switch (pos) {
            default:
                return;

            case -1:
                ongpuimagefilterchosenlistener.onGpuImageFilterChosenListener(createFilterForType(FilterType.CUSTOM));
                return;
            case 2:
                //
                ongpuimagefilterchosenlistener.onGpuImageFilterChosenListener(createFilterForType(FilterType.RED));
                return;

            case 1:
                //
                ongpuimagefilterchosenlistener.onGpuImageFilterChosenListener(createFilterForType(FilterType.BLUE));
                return;

            case 0:
                //
                ongpuimagefilterchosenlistener.onGpuImageFilterChosenListener(createFilterForType(FilterType.GREEN));

                return;

            case 3:
                ongpuimagefilterchosenlistener.onGpuImageFilterChosenListener(createFilterForType(FilterType.POSTERIZE));
                return;

        }
    }

}
