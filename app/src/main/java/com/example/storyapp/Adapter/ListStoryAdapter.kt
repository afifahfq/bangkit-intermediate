package com.example.storyapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.Models.Story
import com.example.storyapp.R

class ListStoryAdapter(private val listStories: ArrayList<Story>): RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListStoryAdapter.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_story, parent, false)
        return ListViewHolder(view)
    }

    class ListViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvUsername: TextView = itemView.findViewById(R.id.tv_item_name)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_description)
    }

    override fun onBindViewHolder(holder: ListStoryAdapter.ListViewHolder, position: Int) {
        val (id, name, description, photoUrl, createdAt, lat, lon) = listStories[position]
        Glide.with(holder.itemView.context)
            .load(photoUrl)
            .into(holder.imgPhoto)

        holder.tvUsername.text = name
        holder.tvName.text = "description"
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listStories[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return listStories.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Story)
    }
}