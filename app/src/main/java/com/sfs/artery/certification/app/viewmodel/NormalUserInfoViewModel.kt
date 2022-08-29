package com.sfs.artery.certification.app.viewmodel

import androidx.lifecycle.MutableLiveData
import com.sfs.artery.certification.app.common.SingleLiveEvent
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

    var userLeftArteryId: String = ""
    var userRightArteryId: String = ""

    val userInfo: MutableLiveData<User> by lazy { MutableLiveData<User>() }
    val changePwState: SingleLiveEvent<Unit> by lazy { SingleLiveEvent() }


    init {
        userDao = ArteryDatabase.getInstance(resourceProvider.getContext())!!.userDao()
    }

    /**
     * 일반 회원정보
     */
    fun setUserInfo(id: String) {
        addDisposable(
            userDao.searchId(id)
                .startLoading()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doComplete({ user ->
                    userInfo.value = user
                    userLeftArteryId = user.userLeftArteryCode
                    userRightArteryId = user.userRightArteryCode
                }, {

                })
        )
    }

    fun changePw() {
        changePwState.call()
    }
}