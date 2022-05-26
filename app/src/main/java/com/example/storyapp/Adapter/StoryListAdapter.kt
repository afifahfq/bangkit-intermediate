package com.example.storyapp.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.Models.ListStoryItem
import com.example.storyapp.Models.Story
import com.example.storyapp.databinding.ItemRowStoryBinding

class StoryListAdapter: PagingDataAdapter<ListStoryItem, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: MyViewHolder.OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: MyViewHolder.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)

            val curr = Story(
                data.id!!,
                data.name,
                data.description,
                data.photoUrl,
                data.createdAt,
                data.lat,
                data.lon
            )
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(curr)
            }
        }
    }

    class MyViewHolder(private val binding: ItemRowStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            binding.tvItemName.text = data.name
            binding.tvItemDescription.text = data.description
            Glide.with(binding.cardView)
                .load(data.photoUrl)
                .into(binding.imgItemPhoto)
        }

        interface OnItemClickCallback {
            fun onItemClicked(data: Story)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}