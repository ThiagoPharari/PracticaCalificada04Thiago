package com.pharari.thiago.lab13phararithiago

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pharari.thiago.lab13phararithiago.databinding.ListItemImgBinding
import java.io.File

class GalleryAdapter(
    private val context: Context,
    private val imageFiles: List<File>
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            GalleryViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.list_item_img, parent, false)
        return GalleryViewHolder(view)
    }
    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int)
    {
        val imageFile = imageFiles[position]
        Glide.with(context)
            .load(imageFile)
            .centerCrop()
            .into(holder.imageView)
    }
    override fun getItemCount(): Int = imageFiles.size
    class GalleryViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.local_img)
    }
}
