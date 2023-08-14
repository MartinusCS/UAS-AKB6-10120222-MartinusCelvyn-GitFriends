package com.martinus.gitfriends.repository.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.martinus.gitfriends.repository.local.entity.FavoriteUser

@Dao
interface UserDao {

    @Query("SELECT * FROM FavoriteUser WHERE login = :login ")
    fun getFavoriteUser(login: String): LiveData<FavoriteUser>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(login: FavoriteUser)

    @Delete
    fun delete(login: FavoriteUser)

    @Query("SELECT * FROM FavoriteUser")
    fun getAll(): LiveData<List<FavoriteUser>>
}