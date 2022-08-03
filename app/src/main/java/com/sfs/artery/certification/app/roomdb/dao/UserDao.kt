package com.sfs.artery.certification.app.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sfs.artery.certification.app.roomdb.entity.User
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Completable

    @Query("SELECT userInfo.* FROM userinfo JOIN userSearch ON (userSearch.userId == userInfo.userId) WHERE userSearch MATCH :id")
    fun searchId(id: String): Single<User>

    @Query("SELECT userInfo.* FROM userinfo JOIN userSearch ON (userSearch.userPw == userInfo.userPw) WHERE userSearch MATCH :pw")
    fun searchPw(pw: String): Single<User>

    @Query("UPDATE userInfo SET userPw =:changePw WHERE userPw =:originPw")
    fun updatePw(originPw: String, changePw: String): Single<Unit>
}