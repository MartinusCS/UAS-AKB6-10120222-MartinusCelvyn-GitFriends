package com.martinus.gitfriends.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.martinus.gitfriends.R
import com.martinus.gitfriends.adapter.SectionsPagerAdapter
import com.martinus.gitfriends.databinding.ActivityDetailBinding
import com.martinus.gitfriends.repository.local.entity.FavoriteUser
import com.martinus.gitfriends.viewmodel.DetailViewModel
import com.martinus.gitfriends.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels() {
        ViewModelFactory.getInstance(application)
    }

    private var userState: Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        login = intent.getStringExtra(data).toString()
        val imgUrl = intent.getStringExtra(img)

        viewModel.getDetailUser(login)
        viewModel.username.observe(this) {
            binding.apply {
                tvUsernameDetail.text = it.login
                tvNameDetail.text = it.name
                tvFollowersDetail.text = "${it.followers} Followers"
                tvFollowingDetail.text = "${it.following} Following"
                tvLocationDetail.text = it.location
            }
            Glide.with(this)
                .load(it.avatarUrl)
                .into(binding.ivPhotoDetail)
        }


        val sectionsPagerAdapter = SectionsPagerAdapter(this, login)
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val callback = supportActionBar
        callback?.title = "Detail Github User"
        callback?.setDisplayHomeAsUpEnabled(true)

        setStateFab(login)
        binding.fabFav.setOnClickListener {
            val favorite = FavoriteUser(login, imgUrl.toString())
            if (userState) {
                viewModel.delete(favorite)
            } else {
                viewModel.insertUser(favorite)
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setStateFab(login: String) {
        viewModel.getFavoriteUser(login).observe(this) {
            if (it != null) {
                userState = true
                binding.fabFav.setImageResource(R.drawable.ic_favorited_24)
            } else {
                userState = false
                binding.fabFav.setImageResource(R.drawable.ic_favorite_24)
            }
        }
    }

    companion object {
        var login = ""
        const val data = "DATA"
        const val img = "IMG"
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}