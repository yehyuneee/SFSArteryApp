package com.sfs.artery.certification.app.viewmodel

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import com.sfs.artery.certification.app.common.ArteryType
import com.sfs.artery.certification.app.roomdb.ArteryDatabase
import com.sfs.artery.certification.app.roomdb.dao.UserDao
import com.sfs.artery.certification.app.roomdb.entity.User
import com.sfs.artery.certification.app.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
) : BaseViewModel() {
    private var userDao: UserDao

    private val user_id: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val user_pw: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val user_pw_confirm: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val user_phonenum: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val user_company_code: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    // 아이디 중복 여부
    private val isOverlapChk = MutableLiveData<Boolean>()

    // 회사코드 확인 여부
    private val isCompanyCodeConfirm = MutableLiveData<Boolean>()

    // 정맥 타입
    private val arteryType: MutableLiveData<ArteryType> by lazy { MutableLiveData<ArteryType>() }

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
        // 내부 DB상에 존재하는지 체크
        // TODO 차후 서버에서 체크하도록 수정 필요
        var result = false
        addDisposable(
            userDao.searchId(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ user ->
                    user.let {
                        if (it.userId.equals(id)) {
                            result = true
                        }
                    }
                }, {
                    result = false
                })
        )
        return result
    }

    /**
     * 회원가입 Form 입력 여부 체크
     * edittext 참고 : https://steemit.com/kr/@jeonghamin/andoird-5-mvvm-edittext
     * 모든 edittext에 넣고, 회원가입 폼 체크하면 될 것 같음
     */
    fun signInTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            TODO("Not yet implemented")
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            TODO("Not yet implemented")
        }

        override fun afterTextChanged(p0: Editable?) {
            TODO("Not yet implemented")
        }
    }

    fun signFormCheck() {
        if(user_id.value.isNullOrEmpty()){

        }
    }
}