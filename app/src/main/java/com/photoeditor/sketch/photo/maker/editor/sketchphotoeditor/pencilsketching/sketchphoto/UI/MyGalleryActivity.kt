package com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.R
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.databinding.ActivityMygalleryBinding
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.databinding.ItemGalleryBinding
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.UI.MyGalleryActivity.RecyclerAdapter.MyHolderView
import com.photoeditor.sketch.photo.maker.editor.sketchphotoeditor.pencilsketching.sketchphoto.Utils.AppUtils
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.io.File

class MyGalleryActivity : Activity() {
    private lateinit var mainAdapter: RecyclerAdapter
    var name_list = ArrayList<String>()
    var name_path_list = ArrayList<String>()
    var extention = ".jpg"
    var extention_png = ".png"
    var context: Context? = null
    var tvNoItem: TextView? = null

    private lateinit var binding: ActivityMygalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMygalleryBinding.inflate(layoutInflater)

        setContentView(binding.root)


        context = this
        tvNoItem = findViewById(R.id.tvNoItem)
        mainAdapter = RecyclerAdapter()
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 2)

        binding.apply {
            recyclerView.layoutManager = mLayoutManager
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.adapter = mainAdapter
        }

        setClicks()

    }

    fun setClicks() {
        binding.apply {
            ivBack.setOnClickListener { finish() }
        }
    }

    override fun onResume() {
        loadingImages()
        super.onResume()
    }

    //***********************************Mohib: getting list of saved files************************************
    private fun loadingImages() {
        val rootPath = AppUtils.getAppFolderPath(context)
        val directory = File(rootPath)
        val listFile = directory.listFiles()
        if (listFile != null && listFile.size > 0) {
            //Mohib: if saved files are then show dialog of all files to choose
            name_list.clear()
            name_path_list.clear()
            for (i in listFile.indices) {
                if (listFile[i].name.endsWith(extention) || listFile[i].name.endsWith(
                        extention_png
                    )
                ) {
                    name_list.add(listFile[i].name)
                    name_path_list.add(listFile[i].absolutePath)
                }
            }
            mainAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(this, "no file found", Toast.LENGTH_SHORT).show()
        }
        if (name_path_list.size > 0) {
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

            myHolderView.tvName.text = name_list[pos]

            Picasso.get().load(File(name_path_list[pos])).into(myHolderView.iv1, object : Callback {
                override fun onSuccess() {}
                override fun onError(e: Exception) {}
            })

            myHolderView.itemView.setOnClickListener {
                val intent = Intent(this@MyGalleryActivity, ViewImageActivity::class.java)
                intent.putExtra("ImgUrl", name_path_list[pos])
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return name_list.size
        }

        inner class MyHolderView(itemView: ItemGalleryBinding) :
            RecyclerView.ViewHolder(itemView.root) {
            var tvName: TextView = itemView.tvName
            var iv1: ImageView = itemView.ivMain


        }
    }
}