package com.martinus.gitfriends.repository.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteUser(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "login")
    var login: String,

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String

)