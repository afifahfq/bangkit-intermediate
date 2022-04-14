package com.example.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.ViewModels.StoryViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var mLiveDataList: StoryViewModel
    private lateinit var rvUsers: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}