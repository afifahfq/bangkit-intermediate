package com.example.storyapp.Views

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.Helper.createCustomTempFile
import com.example.storyapp.Helper.uriToFile
import com.example.storyapp.MainActivity
import com.example.storyapp.Preferences.UserPreference
import com.example.storyapp.R
import com.example.storyapp.UI.NormalEditText
import com.example.storyapp.UI.PasswordEditText
import com.example.storyapp.UI.SubmitButton
import com.example.storyapp.ViewModels.StoryViewModel
import com.example.storyapp.databinding.ActivityCreateStoryBinding
import com.example.storyapp.databinding.ActivityLoginBinding
import com.google.android.material.button.MaterialButton
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CreateStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateStoryBinding
    private lateinit var currentPhotoPath: String
    private lateinit var mLiveDataStory: StoryViewModel
    private var imageFile: File? = null
    private lateinit var submitButton: MaterialButton
    private lateinit var editDescriptionText: NormalEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_story)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        submitButton = findViewById(R.id.uploadButton)
        editDescriptionText = findViewById(R.id.editDescriptionText)
        setMyButtonEnable()

        binding.cameraButton.setOnClickListener { startTakePhoto() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener { uploadImage() }

        binding.editDescriptionText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length != 0) {
                    setMyButtonEnable()
                }
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        mLiveDataStory = ViewModelProvider(this)[StoryViewModel::class.java]
        subscribe()
    }

    private fun subscribe() {
        val statusObserver = Observer<Boolean> { aStatus ->
            showResult(aStatus)
        }
        mLiveDataStory.getStatus().observe(this, statusObserver)

        val loadingObserver = Observer<Boolean> { aLoading ->
            showLoading(aLoading)
        }
        mLiveDataStory.getLoading().observe(this, loadingObserver)
    }

    private fun showResult(aStatus: Boolean) {
        if (aStatus == true) {
            Toast.makeText(this, "Story uploaded!",Toast.LENGTH_SHORT).show()
            val detailStoryIntent = Intent(this@CreateStoryActivity, MainActivity::class.java)
            startActivity(detailStoryIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@CreateStoryActivity as Activity).toBundle())
            finish()
        } else {
            Toast.makeText(this, "Upload failed, please retry!",Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setMyButtonEnable() {
        val resultDesc = editDescriptionText.text
        submitButton.isEnabled = resultDesc != null && resultDesc.toString().isNotEmpty()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@CreateStoryActivity,
                "com.example.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        if (imageFile != null) {
            onAlertDialog(binding.root)
        } else {
            Toast.makeText(this@CreateStoryActivity, "Please choose your image.", Toast.LENGTH_SHORT).show()
        }
    }

    fun onAlertDialog(view: View) {
        val userPreference = UserPreference(this)
        val file = imageFile as File
        val desc: String = binding.editDescriptionText.text.toString()
        val description = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        val builder = androidx.appcompat.app.AlertDialog.Builder(view.context)
        builder.setTitle("Upload Story")
        builder.setMessage("How do you want to upload your story?")

        builder.setPositiveButton(
            "As Myself") { dialog, id ->
//            Toast.makeText(this, "As Myself",Toast.LENGTH_SHORT).show()
            mLiveDataStory.addNewStory(userPreference.getToken(), description, imageMultipart)
        }
        builder.setNegativeButton(
            "As Guest") { dialog, id ->
//            Toast.makeText(this, "As Guest",Toast.LENGTH_SHORT).show()
            mLiveDataStory.addNewStoryGuest(description, imageMultipart)
        }
        builder.setNeutralButton(
            "Cancel") { dialog, id ->
        }
        builder.show()
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            val result =  BitmapFactory.decodeFile(myFile.path)
//            Silakan gunakan kode ini jika mengalami perubahan rotasi
//            val result = rotateBitmap(
//                BitmapFactory.decodeFile(myFile.path),
//                true
//            )

            imageFile = myFile
            binding.previewImageView.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@CreateStoryActivity)

            imageFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}