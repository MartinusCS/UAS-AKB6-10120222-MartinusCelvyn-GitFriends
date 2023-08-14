package com.martinus.gitfriends.repository.remote.retrofit

import com.martinus.gitfriends.repository.remote.response.GithubUser
import com.martinus.gitfriends.repository.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")

    fun getSearchGithubUser(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<GithubUser>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<GithubUser>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<GithubUser>>

}