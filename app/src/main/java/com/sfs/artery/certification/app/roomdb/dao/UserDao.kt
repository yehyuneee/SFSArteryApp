package com.sfs.artery.certification.app.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sfs.artery.certification.app.roomdb.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query("SELECT userInfo.* FROM userinfo JOIN userSearch ON (userSearch.userId == userInfo.userId) WHERE userSearch MATCH :id")
    fun searchId(id: String): User

    @Query("SELECT userInfo.* FROM userinfo JOIN userSearch ON (userSearch.userPw == userInfo.userPw) WHERE userSearch MATCH :pw")
    fun searchPw(pw: String): User
}