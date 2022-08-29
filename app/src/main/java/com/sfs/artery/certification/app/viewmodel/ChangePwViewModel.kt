package com.sfs.artery.certification.app.viewmodel

import androidx.lifecycle.MutableLiveData
import com.sfs.artery.certification.app.common.ChangePwErrorType
import com.sfs.artery.certification.app.roomdb.dao.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ChangePwViewModel @Inject constructor(
) : BaseViewModel() {
    lateinit var userDao: UserDao

    val originPw: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val newPw: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val newPwConfirm: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val changePwState: MutableLiveData<ChangePwErrorType> by lazy { MutableLiveData<ChangePwErrorType>() }

    fun doChangePw() {
        if (originPw.value.isNullOrEmpty()) {
            changePwState.postValue(ChangePwErrorType.ORIGIN_PW_EMPTY)
        } else if (newPw.value.isNullOrEmpty()) {
            changePwState.postValue(ChangePwErrorType.NEW_PW_EMPTY)
        } else if (newPwConfirm.value.isNullOrEmpty()) {
            changePwState.postValue(ChangePwErrorType.NEW_PW_COFIRM_EMPTY)
        } else if (newPw.value != newPwConfirm.value) {
            changePwState.postValue(ChangePwErrorType.NEW_PW_COFIRM_NOT_CORRECT)
        } else {
            addDisposable(
                userDao.searchPw(originPw.value.toString())
                    .startLoading()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doComplete({ user ->
                        if (originPw.value == user.userPw) {
                            updatePw()
                        } else {
                            changePwState.postValue(ChangePwErrorType.NEW_PW_COFIRM)
                        }
                    }, {
                        changePwState.postValue(ChangePwErrorType.NEW_PW_COFIRM)
                    })
            )
        }


    }

    fun updatePw() {
        addDisposable(
            userDao.updatePw(originPw.value.toString(), newPw.value.toString())
                .startLoading()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doComplete({ user ->
                    changePwState.postValue(ChangePwErrorType.CHANGE_PW)
                }, {
                    changePwState.postValue(ChangePwErrorType.CHANGE_PW_ERROR)
                })
        )
    }
}