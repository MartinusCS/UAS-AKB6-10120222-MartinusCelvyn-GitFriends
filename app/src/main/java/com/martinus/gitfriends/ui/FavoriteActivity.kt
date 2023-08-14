package com.martinus.gitfriends.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.martinus.gitfriends.adapter.MainAdapter
import com.martinus.gitfriends.databinding.ActivityFavoriteBinding
import com.martinus.gitfriends.repository.remote.response.GithubUser
import com.martinus.gitfriends.viewmodel.DetailViewModel
import com.martinus.gitfriends.viewmodel.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val viewModel: DetailViewModel by viewModels() {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavorite.addItemDecoration(itemDecoration)

        viewModel.getAll().observe(this) {
            val listGithub = ArrayList<GithubUser>()
            if (it != null) {
                for (github in it) {
                    val user = GithubUser(github.avatarUrl, github.login, 0, 0, "", "")
                    listGithub.add(user)
                }
            }
            binding.rvFavorite.adapter = MainAdapter(listGithub)
            checkData(listGithub)

        }

        val callback = supportActionBar
        callback?.title = "Favorite Github User"
        callback?.setDisplayHomeAsUpEnabled(true)
    }

    private fun checkData(list: ArrayList<GithubUser>) {
        if (list.isEmpty()) {
            binding.apply {
                ivNotFound.visibility = View.VISIBLE
                tvNotFound.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}