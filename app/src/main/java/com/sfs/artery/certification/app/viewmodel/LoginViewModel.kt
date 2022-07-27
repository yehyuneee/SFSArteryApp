package com.sfs.artery.certification.app.viewmodel

import androidx.lifecycle.MutableLiveData
import com.sfs.artery.certification.app.common.LoginProcessType
import com.sfs.artery.certification.app.common.LoginType
import com.sfs.artery.certification.app.roomdb.ArteryDatabase
import com.sfs.artery.certification.app.roomdb.dao.UserDao
import com.sfs.artery.certification.app.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
) : BaseViewModel() {

    var userDao: UserDao
    var arteryUserId: String = ""

    val userId: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val userPw: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val saveIdFlag: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val loginStatus: MutableLiveData<LoginProcessType> by lazy { MutableLiveData<LoginProcessType>() }
    val loginFailType: MutableLiveData<LoginType> by lazy { MutableLiveData<LoginType>() }

    init {
        userDao = ArteryDatabase.getInstance(resourceProvider.getContext())!!.userDao()
    }

    /**
     * 정맥 인증 로그인 진행
     */
    fun doArteryLogin() {
        if (userId.value.isNullOrEmpty()) {
            loginFailType.postValue(LoginType.ID_EMPTY)
        } else {
            doLogin(false)
        }
    }

    /**
     * Id로 로그인 진행
     */
    fun doIdLogin() {
        if (userId.value.isNullOrEmpty()) {
            loginFailType.postValue(LoginType.ID_EMPTY)
        } else if (userPw.value.isNullOrEmpty()) {
            loginFailType.postValue(LoginType.PW_EMPTY)
        } else {
            doLogin(true)
        }
    }


    fun doLogin(isIdLogin: Boolean) {
        addDisposable(
            userDao.searchId(userId.value.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ user ->
                    val loginUser = user

                    if (isIdLogin) {
                        // id로 로그인
                        if (loginUser.userPw != userPw.value) {
                            // 패스워드 체크
                            loginFailType.postValue(LoginType.PW_COMFIRM_CHK_EMPTY)
                        }
                        loginStatus.postValue(LoginProcessType.ID_LOGIN)
                    } else {
                        arteryUserId = loginUser.id.toString()
                        loginStatus.postValue(LoginProcessType.ARTERY_LOGIN)
                    }
                }, {
                    loginFailType.postValue(LoginType.ID_COMFIRM_CHK_EMPTY)
                    loginStatus.postValue(LoginProcessType.NONE)
                })
        )
    }
}