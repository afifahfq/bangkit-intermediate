package com.example.storyapp.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storyapp.R

class DetailStoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_story)
    }

    companion object {
        private const val TAG = "DetailStoryActivity"
        const val EXTRA_STORY = "extra_story"
    }
}