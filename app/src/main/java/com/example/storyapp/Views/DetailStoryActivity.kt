package com.example.storyapp.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.storyapp.Models.Story
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_story)

        val story = intent.getParcelableExtra<Story>(EXTRA_STORY) as Story
        setView(story)
    }

    private fun setView(detailStory: Story) {
        val avatarObject: ImageView = findViewById(R.id.img_item_photo)
        val nameObject: TextView = findViewById(R.id.tv_item_name)
        val dateObject: TextView = findViewById(R.id.tv_item_date)
        val descriptionObject: TextView = findViewById(R.id.tv_item_description)

        Glide.with(avatarObject.context)
            .load(detailStory.photoUrl)
            .into(avatarObject)

//        val createdAt = DateFormat.getDateInstance(DateFormat.FULL).format(detailStory.createdAt)
//        val createdAt = detailStory.createdAt?.format(DateTimeFormatter.ISO_DATE_TIME)
        val createdAt = detailStory.createdAt?.withDateFormat()
        val date = "Created at ${createdAt?.dropLast(4)}"
        dateObject.text = date

        val name = "${detailStory.name}"
        nameObject.text = name

        val description = "Description : ${detailStory.description}"
        descriptionObject.text = description
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.tvItemName.visibility = View.GONE
            binding.tvItemDescription.visibility = View.GONE
            binding.tvItemDate.visibility = View.GONE
            binding.imgItemPhoto.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.tvItemName.visibility = View.VISIBLE
            binding.tvItemDescription.visibility = View.VISIBLE
            binding.tvItemDate.visibility = View.VISIBLE
            binding.imgItemPhoto.visibility = View.VISIBLE
        }
    }

    fun String.withDateFormat(): String {
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.CHINESE)
        val date = format.parse(this) as Date

//        val stringDate = date.format(DateTimeFormatter.ISO_DATE_TIME)

        return DateFormat.getDateInstance(DateFormat.FULL).format(date)
    }

    companion object {
        private const val TAG = "DetailStoryActivity"
        const val EXTRA_STORY = "extra_story"
    }
}