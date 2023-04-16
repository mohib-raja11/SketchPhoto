package wishpool.sketch.sketch_util

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import wishpool.sketch.sketch_util.GPUImageFilterTools.OnGpuImageFilterChosenListener
import wishpool.sketch.sketch_util.GPUImageFilterTools.FilterList
import wishpool.sketch.sketch_util.GPUImageFilterTools.FilterType
import android.content.DialogInterface
import wishpool.sketch.sketch_util.GPUImageFilterTools
import jp.co.cyberagent.android.gpuimage.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter
import jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter
import java.lang.IllegalStateException
import java.util.*

object GPUImageFilterTools {
    var overlayBmp: Bitmap? = null
    fun showDialog(
        context: Context,
        listener: OnGpuImageFilterChosenListener
    ) {
        val filters = FilterList()
        filters.addFilter("Posterize", FilterType.POSTERIZE)
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose a filter")
        builder.setItems(
            filters.names.toTypedArray()
        ) { dialog: DialogInterface?, item: Int ->
            listener.onGpuImageFilterChosenListener(
                createFilterForType(
                    context, filters.filters[item]
                )
            )
        }
        builder.create().show()
    }

    private fun createFilterForType(
        context: Context,
        type: FilterType
    ): GPUImageFilter {
        return when (type) {
            FilterType.POSTERIZE -> GPUImagePosterizeFilter()
            FilterType.RED -> GPUImageRGBFilter(1.5f, 1.0f, 1.0f)
            FilterType.GREEN -> GPUImageRGBFilter(1.0f, 1.5f, 1.0f)
            FilterType.BLUE -> GPUImageRGBFilter(1.0f, 1.0f, 1.5f)
            else -> throw IllegalStateException("No filter of that type!")
        }
    }

    @JvmStatic
    fun Applyeffects(
        i: Int, context: Context,
        ongpuimagefilterchosenlistener: OnGpuImageFilterChosenListener
    ) {
        when (i) {
            2 -> {
                //
                ongpuimagefilterchosenlistener
                    .onGpuImageFilterChosenListener(
                        createFilterForType(
                            context, FilterType.RED
                        )
                    )
                return
            }
            1 -> {
                //
                ongpuimagefilterchosenlistener
                    .onGpuImageFilterChosenListener(
                        createFilterForType(
                            context, FilterType.BLUE
                        )
                    )
                return
            }
            0 -> {
                //
                ongpuimagefilterchosenlistener
                    .onGpuImageFilterChosenListener(
                        createFilterForType(
                            context, FilterType.GREEN
                        )
                    )
                return
            }
            3 -> {
                ongpuimagefilterchosenlistener
                    .onGpuImageFilterChosenListener(
                        createFilterForType(
                            context, FilterType.POSTERIZE
                        )
                    )
                return
            }
            else -> return
        }
    }

    interface OnGpuImageFilterChosenListener {
        fun onGpuImageFilterChosenListener(filter: GPUImageFilter?)
    }

    private enum class FilterType {
        POSTERIZE, RED, GREEN, BLUE
    }

    private class FilterList {
        var names: MutableList<String> = LinkedList()
        var filters: MutableList<FilterType> = LinkedList()
        fun addFilter(name: String, filter: FilterType) {
            names.add(name)
            filters.add(filter)
        }
    }
}