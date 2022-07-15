package com.sfs.artery.certification.app.viewmodel

import androidx.lifecycle.MutableLiveData
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
    val companyCode: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val saveIdFlag: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val loginStatus: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    init {
        userDao = ArteryDatabase.getInstance(resourceProvider.getContext())!!.userDao()
    }

    /**
     * Id로 로그인 진행
     */
    fun doIdLogin() {
        addDisposable(
            userDao.searchId(userId.value.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ user ->
                    var flag = true
                    val loginUser = user

                    if (loginUser.userPw != userPw.value) {
                        // 패스워드 체크
                        flag = false
                    } else if (loginUser.userCompanyCode != companyCode.value) {
                        // 회사코드 체크
                        flag = false
                    }
                    arteryUserId = loginUser.id.toString()
                    loginStatus.postValue(flag)
                }, {
                    loginStatus.postValue(false)
                })
        )
    }
}