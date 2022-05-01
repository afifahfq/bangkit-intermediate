package com.example.storyapp.Views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.Api.ApiService
import com.example.storyapp.MainActivity
import com.example.storyapp.Models.User
import com.example.storyapp.Preferences.UserPreference
import com.example.storyapp.R
import com.example.storyapp.UI.SubmitButton
import com.example.storyapp.UI.NormalEditText
import com.example.storyapp.UI.PasswordEditText
import com.example.storyapp.ViewModels.UserViewModel
import com.example.storyapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mLiveDataUser: UserViewModel
    private lateinit var submitButton: SubmitButton
    private lateinit var emailEditText: NormalEditText
    private lateinit var passEditText: PasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Login")

        checkLoggedIn()

        submitButton = findViewById(R.id.my_button)
        emailEditText = findViewById(R.id.et_email)
        passEditText = findViewById(R.id.et_pass)
        setMyButtonEnable()

        mLiveDataUser = ViewModelProvider(this)[UserViewModel::class.java]
        subscribe()

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        passEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length >= 6) {
                    setMyButtonEnable()
                }
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        submitButton.setOnClickListener {
            //Toast.makeText(this@LoginActivity, passEditText.text, Toast.LENGTH_SHORT).show()
            mLiveDataUser.login(emailEditText.text.toString(), passEditText.text.toString())
        }

        val gotoRegister: TextView = findViewById(R.id.goto_register)
        gotoRegister.setOnClickListener(this)
    }

    private fun checkLoggedIn() {
        val userPreference = UserPreference(this)

        if (userPreference.getUser().userId != "") {
            val moveIntent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(moveIntent)
            finish()
        }
    }

    private fun subscribe() {
        val userObserver = Observer<User> { aUser ->
//            Toast.makeText(this@LoginActivity, aUser.userId, Toast.LENGTH_SHORT).show()
            saveUser(aUser)
        }
        mLiveDataUser.getUser()?.observe(this, userObserver)

        val loadingObserver = Observer<Boolean> { aLoading ->
            showLoading(aLoading)
        }
        mLiveDataUser.getLoading().observe(this, loadingObserver)

        val loginObserver = Observer<Boolean> { aStatus ->
            checkLoginStatus(aStatus)
        }
        mLiveDataUser.getStatus().observe(this, loginObserver)
    }

    private fun saveUser(user: User) {
        val userPreference = UserPreference(this)
        userPreference.setUser(user)
        Log.i("CEKPREFERENCE", userPreference.getUser().toString())
//        Toast.makeText(this, userPreference.getUser().name, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun checkLoginStatus(aStatus: Boolean) {
        val userPreference = UserPreference(this)

        if (aStatus) {
            val moveIntent = Intent(this@LoginActivity, MainActivity::class.java)
            ApiService.TOKEN = userPreference.getToken().toString()
            Log.i("CEKTOKEN", ApiService.TOKEN)
            startActivity(moveIntent)
            finish()
        } else {
            Toast.makeText(this@LoginActivity, "Login gagal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setMyButtonEnable() {
        val resultEmail = emailEditText.text
        val resultPass = passEditText.text
        submitButton.isEnabled = resultEmail != null && resultEmail.toString().isNotEmpty() && resultPass != null && resultPass.toString().isNotEmpty()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.goto_register -> {
                val moveIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
//                startActivity(moveIntent)
                startActivity(moveIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity as Activity).toBundle())
                finish()
            }
        }
    }
}