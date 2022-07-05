package com.sfs.artery.certification.app.roomdb.entity

import androidx.room.Entity
import androidx.room.Fts4

@Entity(tableName = "userSearch")
@Fts4(contentEntity = User::class)
data class UserSearchTemp(
    val userId: String,
    val userPw: String,
)
