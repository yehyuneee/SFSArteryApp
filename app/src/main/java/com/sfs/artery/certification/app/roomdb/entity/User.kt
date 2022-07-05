package com.sfs.artery.certification.app.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userInfo")
data class User(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "userPw") val userPw: String,
    @ColumnInfo(name = "userName") val userName: String,
    @ColumnInfo(name = "userPhone") val userPhoneNum: String,
    @ColumnInfo(name = "userCompany_code") val userCompanyCode: String,
    @ColumnInfo(name = "userArtery_hand_code") val userArteryHandCode: String,
    @ColumnInfo(name = "userArtery_code") val userArteryCode: String,
)
