package com.example.storyapp.Views

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.Api.ApiService
import com.example.storyapp.MainActivity
import com.example.storyapp.Models.User
import com.example.storyapp.Preferences.UserPreference
import com.example.storyapp.R
import com.example.storyapp.ViewModels.UserViewModel
import com.example.storyapp.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLandingBinding
    private lateinit var mLiveDataUser: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        binding = ActivityLandingBinding.inflate(layoutInflater)

        checkLoggedIn()

        mLiveDataUser = ViewModelProvider(this)[UserViewModel::class.java]
        subscribe()

        val gotoLogin: Button = findViewById(R.id.goto_login)
        val gotoRegister: Button = findViewById(R.id.goto_register)

        gotoLogin.setOnClickListener(this)
        gotoRegister.setOnClickListener(this)

        mLiveDataUser.loginStatus.postValue(false)

        val userPreference = UserPreference(this)
    }

    private fun checkLoggedIn() {
        val userPreference = UserPreference(this)

        if (userPreference.getUser().userId != "") {
            ApiService.TOKEN = userPreference.getToken().toString()

            val moveIntent = Intent(this@LandingActivity, MainActivity::class.java)
            startActivity(moveIntent)
            finish()
        }
    }

    private fun subscribe() {
        val loginObserver = Observer<Boolean> { aStatus ->
            checkLoginStatus(aStatus)
        }
        mLiveDataUser.getStatus().observe(this, loginObserver)
    }

    private fun checkLoginStatus(aStatus: Boolean) {
        if (aStatus) {
            val moveIntent = Intent(this@LandingActivity, MainActivity::class.java)
//            startActivity(moveIntent)
            startActivity(moveIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@LandingActivity as Activity).toBundle())
            finish()
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.goto_login -> {
                val moveIntent = Intent(this@LandingActivity, LoginActivity::class.java)
//                startActivity(moveIntent)
                startActivity(moveIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@LandingActivity as Activity).toBundle())
                finish()
            }
            R.id.goto_register -> {
                val moveIntent = Intent(this@LandingActivity, RegisterActivity::class.java)
//                startActivity(moveIntent)
                startActivity(moveIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@LandingActivity as Activity).toBundle())
                finish()
            }
        }
    }
}