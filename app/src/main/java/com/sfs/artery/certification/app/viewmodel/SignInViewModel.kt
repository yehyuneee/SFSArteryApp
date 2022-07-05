package com.sfs.artery.certification.app.viewmodel

import androidx.lifecycle.MutableLiveData
import com.sfs.artery.certification.app.roomdb.ArteryDatabase
import com.sfs.artery.certification.app.roomdb.dao.UserDao
import com.sfs.artery.certification.app.roomdb.entity.User
import com.sfs.artery.certification.app.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
) : BaseViewModel() {
    private var userDao: UserDao

    // User 데이터에 변화일어나는지 감지 가능
    private val signUserData: MutableLiveData<User> by lazy { MutableLiveData<User>() }

    init {
        userDao = ArteryDatabase.getInstance(resourceProvider.getContext())!!.userDao()
    }

    /**
     * 가입하기
     */
    fun doSignIn(user: User) {
        userDao.insertUser(user)
    }

    /**
     * 아이디 중복체크
     * true : 중복된 아이디
     */
    fun isEffectiveId(id: String): Boolean {
        // TODO db 조회, 수정할때는 백그라운드에서 작업한다. 수정 필요
        val checkUser = userDao.searchId(id)
        checkUser.let {
            if (checkUser.userId.equals(id)) {
                return true
            }
        }

        return false
    }
}