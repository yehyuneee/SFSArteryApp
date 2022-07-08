package com.sfs.artery.certification.app.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sfs.artery.certification.app.roomdb.dao.UserDao
import com.sfs.artery.certification.app.roomdb.entity.User
import com.sfs.artery.certification.app.roomdb.entity.UserSearchTemp

@Database(entities = [User::class, UserSearchTemp::class], version = 1)
abstract class ArteryDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var instance: ArteryDatabase? = null
        fun getInstance(context: Context): ArteryDatabase? {
            if (instance == null) {
                synchronized(ArteryDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ArteryDatabase::class.java,
                        "arteryDatabase"
                    ).fallbackToDestructiveMigration().build()
                }
            }

            return instance
        }
    }
}