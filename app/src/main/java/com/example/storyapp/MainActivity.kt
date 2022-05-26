package com.example.storyapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.Adapter.LoadingStateAdapter
import com.example.storyapp.Adapter.StoryListAdapter
import com.example.storyapp.Api.ApiService
import com.example.storyapp.Models.Story
import com.example.storyapp.Preferences.UserPreference
import com.example.storyapp.ViewModels.StoryViewModel
import com.example.storyapp.ViewModels.ViewModelFactory
import com.example.storyapp.Views.*
import com.example.storyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvStories: RecyclerView
    private val storyViewModel: StoryViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvStories = findViewById(R.id.rv_stories)
        rvStories.setHasFixedSize(true)

        binding.fabAdd.setOnClickListener {
            val myIntent = Intent(this, CreateStoryActivity::class.java)
            this.startActivity(myIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity).toBundle())
        }

        binding.mapbutton.setOnClickListener {
            val myIntent = Intent(this, MapsActivity::class.java)
            this.startActivity(myIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity).toBundle())
        }

//        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            rvStories.layoutManager = GridLayoutManager(this, 2)
//        } else {
//            rvStories.layoutManager = LinearLayoutManager(this)
//        }

        binding.rvStories.layoutManager = LinearLayoutManager(this)


        getData()
    }

    private fun getData() {
        val adapter = StoryListAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        storyViewModel.story.observe(this, {
            adapter.submitData(lifecycle, it)
        })

        adapter.setOnItemClickCallback(object : StoryListAdapter.MyViewHolder.OnItemClickCallback {
            override fun onItemClicked(data: Story) {
                val detailStoryIntent = Intent(this@MainActivity, DetailStoryActivity::class.java)
                detailStoryIntent.putExtra(DetailStoryActivity.EXTRA_STORY, data)
                startActivity(detailStoryIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity).toBundle())
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu1 -> {
                onAlertDialog(binding.root)
                return true
            }
            else -> return true
        }
    }

    fun onAlertDialog(view: View) {
        val builder = AlertDialog.Builder(view.context)
        builder.setTitle("Log Out")
        builder.setMessage("Are you sure you want to log out?")

        builder.setPositiveButton(
            "Yes") { dialog, id ->

            val userPreference = UserPreference(this)
            userPreference.clear()

            ApiService.TOKEN = ""

            val i = Intent(this, LandingActivity::class.java)
            startActivity(i)
            finish()
        }
        builder.setNegativeButton(
            "Cancel") { dialog, id ->
        }
        builder.show()
    }
}