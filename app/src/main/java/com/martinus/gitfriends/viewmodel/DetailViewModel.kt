package com.martinus.gitfriends.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martinus.gitfriends.repository.LocalRepository
import com.martinus.gitfriends.repository.local.entity.FavoriteUser
import com.martinus.gitfriends.repository.remote.response.GithubUser
import com.martinus.gitfriends.repository.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {

    val _listFollowing = MutableLiveData<List<GithubUser>>()
    val listFollowing: LiveData<List<GithubUser>> = _listFollowing

    val _listFollowers = MutableLiveData<List<GithubUser>>()
    val listFollowers: LiveData<List<GithubUser>> = _listFollowers

    private val _name = MutableLiveData<GithubUser>()
    val name: LiveData<GithubUser> = _name

    private val _username = MutableLiveData<GithubUser>()
    val username: LiveData<GithubUser> = _username

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mLocalRepository: LocalRepository = LocalRepository(application)

    fun getAll(): LiveData<List<FavoriteUser>> = mLocalRepository.getAll()

    fun insertUser(login: FavoriteUser) {
        mLocalRepository.insertUser(login)
    }

    fun delete(login: FavoriteUser) {
        mLocalRepository.delete(login)
    }

    fun getFavoriteUser(login: String): LiveData<FavoriteUser> {
        return mLocalRepository.getFavoriteUser(login)
    }

    fun getDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<GithubUser> {
            override fun onResponse(
                call: Call<GithubUser>,
                response: Response<GithubUser>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _name.value = response.body()
                    _username.value = response.body()
                } else {
                    Log.e("DetailViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubUser>, t: Throwable) {
                _isLoading.value = false
                Log.e("DetailViewModel", "onFailure : ${t.message.toString()}")
            }
        })
    }

    fun getUserFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<GithubUser>> {
            override fun onResponse(
                call: Call<List<GithubUser>>,
                response: Response<List<GithubUser>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowing.value = response.body()

                }
            }

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun getUserFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<GithubUser>> {
            override fun onResponse(
                call: Call<List<GithubUser>>,
                response: Response<List<GithubUser>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowers.value = response.body()
                } else {
                    Log.e("FollowersViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}