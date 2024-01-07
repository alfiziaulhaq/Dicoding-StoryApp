package com.dicoding.alfistoryapp.view.posting

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.alfistoryapp.data.ViewModelFactory
import com.dicoding.alfistoryapp.data.helper.getImageUri
import com.dicoding.alfistoryapp.data.helper.reduceFileImage
import com.dicoding.alfistoryapp.data.helper.uriToFile
import com.dicoding.alfistoryapp.databinding.ActivityPostingBinding
import com.dicoding.alfistoryapp.view.detail.DetailActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class PostingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostingBinding
    private val viewModel by viewModels<PostingViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var token : String?=""
    private var currentImageUri: Uri? = null

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        const val EXTRA_ID="extra_id"
        const val EXTRA_TOKEN="extra_token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = intent.getStringExtra(DetailActivity.EXTRA_TOKEN).toString()

        viewModel.call.observe(this) {
            if (it.error) {
                showToast(it.message)
                showLoading(false)
            } else {
                showToast("story posted :)")
                finish()
            }
        }

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnPosting.setOnClickListener { uploadImage() }

    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivImage.setImageURI(it)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.textCaption.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImage = imageFile.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImage
            )

            if(binding.textCaption.text.toString()=="" || binding.textCaption.text==null){
                showToast("fill description first !")
            }
            viewModel.postStory2(token.toString(),imageMultipart,description)
        } ?: showToast("upload image first !!!")
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = (if (isLoading) View.VISIBLE else View.GONE)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}