package com.dicoding.alfistoryapp.view.detail

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.alfistoryapp.data.ViewModelFactory
import com.dicoding.alfistoryapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityDetailBinding
    private var id : String?= null
    private var token : String?= null

    companion object{
        private const val TAG = "DetailViewModel"
        const val EXTRA_ID="extra_id"
        const val EXTRA_TOKEN="extra_token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra(EXTRA_ID)
        token = intent.getStringExtra(EXTRA_TOKEN)

        viewModel.getDetailStory2(token.toString(),id.toString())

        viewModel.result.observe(this){users->
            binding.tvName.text="${users.name}"
            binding.tvDescription.text = "${users.description}"
            Glide.with(binding.root)
                .load(users.photoUrl) // URL Gambar
                .into(binding.ivStory)
        }

        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}