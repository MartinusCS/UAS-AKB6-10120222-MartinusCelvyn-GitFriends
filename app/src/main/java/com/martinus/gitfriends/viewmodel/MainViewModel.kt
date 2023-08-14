package com.martinus.gitfriends.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martinus.gitfriends.repository.remote.response.GithubUser
import com.martinus.gitfriends.repository.remote.response.UserResponse
import com.martinus.gitfriends.repository.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    internal val _listUsers = MutableLiveData<List<GithubUser>>()
    val listUsers: LiveData<List<GithubUser>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchUser(name: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchGithubUser(name)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUsers.value = response.body()?.items
                    Log.d("ViewModel", "${response.body()?.items}")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure : ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }

}