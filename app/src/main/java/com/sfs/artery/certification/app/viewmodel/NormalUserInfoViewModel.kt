package com.sfs.artery.certification.app.viewmodel

import androidx.lifecycle.MutableLiveData
import com.sfs.artery.certification.app.roomdb.ArteryDatabase
import com.sfs.artery.certification.app.roomdb.dao.UserDao
import com.sfs.artery.certification.app.roomdb.entity.User
import com.sfs.artery.certification.app.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class NormalUserInfoViewModel
@Inject constructor(resourceProvider: ResourceProvider) : BaseViewModel() {
    var userDao: UserDao

    var userPassword: String = ""
    var userArteryId: String = ""

    val userInfo: MutableLiveData<User> by lazy { MutableLiveData<User>() }

    init {
        userDao = ArteryDatabase.getInstance(resourceProvider.getContext())!!.userDao()
    }

    /**
     * 일반 회원정보
     */
    fun setUserInfo(id: String) {
        addDisposable(
            userDao.searchId(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ user ->
                    userInfo.value = user
                }, {

                })
        )
    }
}