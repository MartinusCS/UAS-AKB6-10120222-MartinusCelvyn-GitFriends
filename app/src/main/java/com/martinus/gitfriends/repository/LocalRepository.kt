package com.martinus.gitfriends.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.martinus.gitfriends.repository.local.entity.FavoriteUser
import com.martinus.gitfriends.repository.local.room.UserDao
import com.martinus.gitfriends.repository.local.room.UserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LocalRepository(application: Application) {
    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getAll(): LiveData<List<FavoriteUser>> = mUserDao.getAll()

    fun insertUser(login: FavoriteUser) {
        executorService.execute { mUserDao.insertUser(login) }
    }

    fun delete(login: FavoriteUser) {
        executorService.execute { mUserDao.delete(login) }
    }

    fun getFavoriteUser(login: String): LiveData<FavoriteUser> {
        return mUserDao.getFavoriteUser(login)
    }

}