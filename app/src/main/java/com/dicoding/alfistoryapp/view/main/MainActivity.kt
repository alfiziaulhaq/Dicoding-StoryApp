package com.dicoding.alfistoryapp.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.alfistoryapp.R
import com.dicoding.alfistoryapp.data.ViewModelFactory
import com.dicoding.alfistoryapp.data.response.ListStoryItem
import com.dicoding.alfistoryapp.databinding.ActivityMainBinding
import com.dicoding.alfistoryapp.view.detail.DetailActivity
import com.dicoding.alfistoryapp.view.maps.MapsActivity
import com.dicoding.alfistoryapp.view.posting.PostingActivity
import com.dicoding.alfistoryapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding
    private var id :String?=null
    private var token :String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                id = user.id
                token = user.token
                binding.tvWelcome.text = "Hi, ${user.name}"
                setUserData(user.token)
            }
            else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        //create recyclerview
        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)

        binding.bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.setting -> {
                    viewModel.logout()
                    true
                }

                R.id.posting -> {
                    Intent(this@MainActivity, PostingActivity::class.java).also {
                        it.putExtra(PostingActivity.EXTRA_TOKEN, token)
                        startActivity(it)
                    }
                    false
                }
                R.id.maps -> {
                    Intent(this@MainActivity, MapsActivity::class.java).also {
                        it.putExtra(MapsActivity.EXTRA_TOKEN, token)
                        startActivity(it)
                    }
                    false
                }
                else -> false
            }
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
        binding.bottomNavView.selectedItemId = R.id.home
    }

    private fun setUserData(token:String) {
        val adapter = StoryAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.storyPagingData(token).observe(this) {
            adapter.submitData(lifecycle, it)
        }

        adapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_TOKEN, token)
                    startActivity(it)
                }
            }
        }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

}