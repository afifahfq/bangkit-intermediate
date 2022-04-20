package com.example.storyapp.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.MainActivity
import com.example.storyapp.Models.User
import com.example.storyapp.Preferences.UserPreference
import com.example.storyapp.R
import com.example.storyapp.UI.NormalEditText
import com.example.storyapp.UI.PasswordEditText
import com.example.storyapp.UI.SubmitButton
import com.example.storyapp.ViewModels.UserViewModel
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mLiveDataUser: UserViewModel
    private lateinit var submitButton: SubmitButton
    private lateinit var nameEditText: NormalEditText
    private lateinit var emailEditText: NormalEditText
    private lateinit var passEditText: PasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Register")

        submitButton = findViewById(R.id.my_button)
        nameEditText = findViewById(R.id.et_name)
        emailEditText = findViewById(R.id.et_email)
        passEditText = findViewById(R.id.et_pass)
        setMyButtonEnable()

        mLiveDataUser = ViewModelProvider(this)[UserViewModel::class.java]
        subscribe()

        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

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
            //Toast.makeText(this@RegisterActivity, passEditText.text, Toast.LENGTH_SHORT).show()
            mLiveDataUser.register(nameEditText.text.toString(), emailEditText.text.toString(), passEditText.text.toString())
        }
    }

    private fun subscribe() {
        val userObserver = Observer<User> { aUser ->
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
        Toast.makeText(this, userPreference.getUser().name, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun checkLoginStatus(aStatus: Boolean) {
        if (aStatus) {
            val moveIntent = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(moveIntent)
        } else {
            Toast.makeText(this@RegisterActivity, "Register gagal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setMyButtonEnable() {
        val resultName = nameEditText.text
        val resultEmail = emailEditText.text
        val resultPass = passEditText.text
        submitButton.isEnabled =
            resultName != null && resultName.toString().isNotEmpty()
            && resultEmail != null && resultEmail.toString().isNotEmpty()
            && resultPass != null && resultPass.toString().isNotEmpty()
    }
}