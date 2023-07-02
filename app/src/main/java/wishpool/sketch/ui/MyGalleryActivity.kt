package wishpool.sketch.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import wishpool.sketch.GlobalActivity
import wishpool.sketch.R
import wishpool.sketch.databinding.ActivityMygalleryBinding
import wishpool.sketch.databinding.ItemGalleryBinding
import wishpool.sketch.ui.MyGalleryActivity.RecyclerAdapter.MyHolderView
import java.io.File
import java.util.*

class MyGalleryActivity : BaseActivity() {

    private lateinit var mainAdapter: RecyclerAdapter

    val filesList = arrayListOf<ImageModel>()

    private var extention = ".jpg"
    private var extentionPng = ".png"
    private var tvNoItem: TextView? = null

    var baseFolderPath = ""
    var title = "My Work"

    private lateinit var binding: ActivityMygalleryBinding

    companion object {
        fun start(context: Context, baseFolderPath: String, title: String) {
            val intent = Intent(context, MyGalleryActivity::class.java)
            intent.putExtra("baseFolderPath", baseFolderPath)
            intent.putExtra("title", title)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMygalleryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        baseFolderPath = intent.getStringExtra("baseFolderPath")!!
        title = intent.getStringExtra("title")!!

        tvNoItem = findViewById(R.id.tvNoItem)
        mainAdapter = RecyclerAdapter()
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 2)

        binding.apply {

            tvTitle.text = title

            recyclerView.layoutManager = mLayoutManager
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.adapter = mainAdapter
        }

        setClicks()
    }

    private fun setClicks() {
        binding.apply {
            ivBack.setOnClickListener { finish() }
        }
    }

    override fun onResume() {
        loadingImages()
        super.onResume()
    }

    data class ImageModel(val imageName: String, val imagePath: String)

    //********Mohib: getting list of saved files*****************//
    @SuppressLint("NotifyDataSetChanged")
    private fun loadingImages() {
        val rootPath = baseFolderPath
        val directory = File(rootPath)
        val listFile = directory.listFiles()

        val tempList = arrayListOf<ImageModel>()

        if (listFile != null && listFile.isNotEmpty()) {
            //Mohib: if saved files are then show dialog of all files to choose

            for (i in listFile.indices) {
                if (listFile[i].name.endsWith(extention) || listFile[i].name.endsWith(
                        extentionPng
                    )
                ) {

                    tempList.add(ImageModel(listFile[i].name, listFile[i].absolutePath))
                }
            }
        } else {
            Toast.makeText(this, "no file found", Toast.LENGTH_SHORT).show()
        }

        //MR: now loading hand drawing photos

        /* val handDrawing = File(getAppDrawingFolderPath())
         val handDrawingList = handDrawing.listFiles()

         if (handDrawingList != null && handDrawingList.isNotEmpty()) {
             //Mohib: if saved files are then show dialog of all files to choose

             for (i in handDrawingList.indices) {
                 if (handDrawingList[i].name.endsWith(extention) || handDrawingList[i].name.endsWith(
                         extentionPng
                     )
                 ) {

                     tempList.add(
                         ImageModel(
                             handDrawingList[i].name,
                             handDrawingList[i].absolutePath
                         )
                     )
                 }
             }
         }*/


        //MR: to show last as first
        val reversedList = tempList.asReversed()
        filesList.clear()
        filesList.addAll(reversedList)

        mainAdapter.notifyDataSetChanged()

        if (filesList.size > 0) {
            tvNoItem!!.visibility = View.GONE
        } else {
            tvNoItem!!.visibility = View.VISIBLE
        }
    }

    inner class RecyclerAdapter : RecyclerView.Adapter<MyHolderView>() {
        override fun onCreateViewHolder(parent: ViewGroup, i: Int): MyHolderView {
            val itemView = ItemGalleryBinding.inflate(layoutInflater, parent, false)
            return MyHolderView(itemView)
        }

        override fun onBindViewHolder(myHolderView: MyHolderView, pos: Int) {

            myHolderView.tvName.text = filesList[pos].imageName

            Picasso.get().load(File(filesList[pos].imagePath))
                .into(myHolderView.iv1, object : Callback {
                    override fun onSuccess() {}
                    override fun onError(e: Exception) {}
                })

            myHolderView.itemView.setOnClickListener {

                val bound = 5 + pos
                val number = Random().nextInt(bound)

                Log.d(TAG, "onBindViewHolder: bound = $bound number = $number")

                val showAd = number % 3 == 0
                if (!showAd) {
                    gotoNextScreen(pos)
                    return@setOnClickListener
                }

                GlobalActivity.showAdmobInterstitialAd(this@MyGalleryActivity) {
                    gotoNextScreen(pos)
                }
            }
        }

        override fun getItemCount(): Int {
            return filesList.size
        }

        inner class MyHolderView(itemView: ItemGalleryBinding) :
            RecyclerView.ViewHolder(itemView.root) {
            var tvName: TextView = itemView.tvName
            var iv1: ImageView = itemView.ivMain
        }
    }

    fun gotoNextScreen(pos: Int) {
        val intent = Intent(this@MyGalleryActivity, ViewImageActivity::class.java)
        intent.putExtra("ImgUrl", filesList[pos].imagePath)
        startActivity(intent)
    }
}