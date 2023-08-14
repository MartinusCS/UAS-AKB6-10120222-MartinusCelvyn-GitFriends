package com.martinus.gitfriends.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.martinus.gitfriends.R
import com.martinus.gitfriends.adapter.MainAdapter
import com.martinus.gitfriends.databinding.ActivityMainBinding
import com.martinus.gitfriends.repository.local.SettingPreference
import com.martinus.gitfriends.repository.remote.response.GithubUser
import com.martinus.gitfriends.viewmodel.MainViewModel
import com.martinus.gitfriends.viewmodel.SettingViewModel
import com.martinus.gitfriends.viewmodel.SettingViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreference.getInstance(dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this)
        { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        viewModel.searchUser("risha")

        viewModel.listUsers.observe(this) { lisUsers ->
            setRecycleView(lisUsers)

            checkData(lisUsers)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        findViewById<RecyclerView>(R.id.rv_user).apply {
            viewModel._listUsers
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                moveTaskToBack(true)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
            }
            R.id.setting -> {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setRecycleView(list: List<GithubUser>) {
        val adapter = MainAdapter(list)
        val recycleView = binding.rvUser
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(this)

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun checkData(list: List<GithubUser>) {
        if (list.isEmpty()) {
            binding.apply {
                ivNotFound.visibility = View.VISIBLE
                tvNotFound.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                ivNotFound.visibility = View.GONE
                tvNotFound.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}