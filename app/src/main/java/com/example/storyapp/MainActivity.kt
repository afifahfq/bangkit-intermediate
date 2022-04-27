package com.example.storyapp

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.Adapter.ListStoryAdapter
import com.example.storyapp.Models.Story
import com.example.storyapp.Models.User
import com.example.storyapp.Preferences.UserPreference
import com.example.storyapp.ViewModels.StoryViewModel
import com.example.storyapp.Views.CreateStoryActivity
import com.example.storyapp.Views.DetailStoryActivity
import com.example.storyapp.Views.LandingActivity
import com.example.storyapp.Views.LoginActivity
import com.example.storyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mLiveDataList: StoryViewModel
    private lateinit var rvStories: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvStories = findViewById(R.id.rv_stories)
        rvStories.setHasFixedSize(true)

        val userPreference = UserPreference(this)
        Log.i("CEKPREFERENCE", userPreference.getUser().name!!)

        mLiveDataList = ViewModelProvider(this)[StoryViewModel::class.java]
        subscribe()
//        mLiveDataList.getAllStories(userPreference.getToken(), null, 100, 0)

        binding.fabAdd.setOnClickListener {
            val myIntent = Intent(this, CreateStoryActivity::class.java)
//            this.startActivity(myIntent)
            this.startActivity(myIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity).toBundle())
        }
    }

    private fun subscribe() {
        val listObserver = Observer<ArrayList<Story>?> { aList ->
            showRecyclerList(aList)
        }
        mLiveDataList.getList().observe(this, listObserver)

        val loadingObserver = Observer<Boolean> { aStatus ->
            showLoading(aStatus)
        }
        mLiveDataList.getLoading().observe(this, loadingObserver)
    }

    private fun showRecyclerList(aList: ArrayList<Story>) {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvStories.layoutManager = GridLayoutManager(this, 2)
        } else {
            rvStories.layoutManager = LinearLayoutManager(this)
        }

        val listUserAdapter = ListStoryAdapter(aList)
        rvStories.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Story) {

                val detailStoryIntent = Intent(this@MainActivity, DetailStoryActivity::class.java)
                detailStoryIntent.putExtra(DetailStoryActivity.EXTRA_STORY, data)
                startActivity(detailStoryIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity).toBundle())
//                startActivity(detailStoryIntent, optionsCompat.toBundle())
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
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
//            Toast.makeText(this, "Updating your device",Toast.LENGTH_SHORT).show()

            val userPreference = UserPreference(this)
            userPreference.clear()

            Log.i("CEKPREFERENCE", userPreference.getUser().name!!)

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