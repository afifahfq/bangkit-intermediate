package com.example.storyapp.Views

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.Models.User
import com.example.storyapp.Preferences.UserPreference
import com.example.storyapp.R
import com.example.storyapp.UI.SubmitButton
import com.example.storyapp.UI.NormalEditText
import com.example.storyapp.UI.PasswordEditText
import com.example.storyapp.ViewModels.UserViewModel
import com.example.storyapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
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
    }

    private fun subscribe() {
        val userObserver = Observer<User> { aUser ->
            saveUser(aUser)
        }
        mLiveDataUser.getUser()?.observe(this, userObserver)

        val statusObserver = Observer<Boolean> { aStatus ->
            showLoading(aStatus)
        }
        mLiveDataUser.getStatus().observe(this, statusObserver)
    }

    private fun saveUser(user: User) {
        val userPreference = UserPreference(this)
        userPreference.setUser(user)
        Toast.makeText(this, userPreference.getUser().name, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setPreference() {

    }

    private fun setMyButtonEnable() {
        val resultEmail = emailEditText.text
        val resultPass = passEditText.text
        submitButton.isEnabled = resultEmail != null && resultEmail.toString().isNotEmpty() && resultPass != null && resultPass.toString().isNotEmpty()
    }
}