package com.martinus.gitfriends.repository.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.martinus.gitfriends.repository.local.entity.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserDatabase {
            if (INSTANCE == null) {
                synchronized(UserDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java, "user_database"
                    )
                        .build()
                }
            }
            return INSTANCE as UserDatabase
        }
    }
}