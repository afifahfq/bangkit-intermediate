package com.example.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.storyapp.UI.SubmitButton
import com.example.storyapp.UI.NormalEditText
import com.example.storyapp.UI.PasswordEditText

class LoginActivity : AppCompatActivity() {
    private lateinit var submitButton: SubmitButton
    private lateinit var emailEditText: NormalEditText
    private lateinit var passEditText: PasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        submitButton = findViewById(R.id.my_button)
        emailEditText = findViewById(R.id.et_email)
        passEditText = findViewById(R.id.et_pass)
        setMyButtonEnable()

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
        submitButton.setOnClickListener { Toast.makeText(this@LoginActivity, emailEditText.text, Toast.LENGTH_SHORT).show() }

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
        submitButton.setOnClickListener { Toast.makeText(this@LoginActivity, passEditText.text, Toast.LENGTH_SHORT).show() }
    }

    private fun setMyButtonEnable() {
        val resultEmail = emailEditText.text
        val resultPass = passEditText.text
        submitButton.isEnabled = resultEmail != null && resultEmail.toString().isNotEmpty() && resultPass != null && resultPass.toString().isNotEmpty()
    }
}