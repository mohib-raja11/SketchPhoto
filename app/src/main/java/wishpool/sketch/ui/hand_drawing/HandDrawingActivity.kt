package wishpool.sketch.ui.hand_drawing

import android.graphics.Color
import android.os.Bundle
import wishpool.sketch.databinding.ActivityHandDrawingBinding
import wishpool.sketch.ui.BaseActivity

class HandDrawingActivity : BaseActivity() {

    lateinit var binding: ActivityHandDrawingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHandDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            drawingView.setBrushAlpha(120)// values from 0-255
            drawingView.setBrushColor(Color.RED)
            drawingView.setSizeForBrush(12) // takes value from 0-200
            drawingView.undo()
            drawingView.redo()
            drawingView.erase(Color.BLACK) // give the color same as the background color
            drawingView.clearDrawingBoard()

            //getter methods
            val alpha = drawingView.getBrushAlpha() // returns INT
            val brushSize = drawingView.getBrushSize() // returns INT
            val brushColor = drawingView.getBrushColor() // returns INT

            val drawing =
                drawingView.getDrawing() // returns ArrayList<CustomPath>(); where CustomPath(var color:Int , var brushThickness:Int, var alpha:Int)

            drawingView.getDrawingCache(false)

            ivDownload.setOnClickListener {

                drawingView.saveImage()


            }
        }
    }
}