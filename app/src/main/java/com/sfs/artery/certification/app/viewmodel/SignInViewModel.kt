package com.sfs.artery.certification.app.viewmodel

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sfs.artery.certification.app.common.ArteryType
import com.sfs.artery.certification.app.common.SignFormErrorType
import com.sfs.artery.certification.app.roomdb.ArteryDatabase
import com.sfs.artery.certification.app.roomdb.dao.UserDao
import com.sfs.artery.certification.app.roomdb.entity.User
import com.sfs.artery.certification.app.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
) : BaseViewModel() {
    var userDao: UserDao

    val user_id: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val user_pw: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val user_pw_confirm: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val user_name: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val user_phonenum: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val user_company_code: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    // 아이디 존재 여부
    val idValiableChk = MutableLiveData<Boolean>()
    var idOverlapId: Boolean = false

    // 회사코드 확인 여부
    val isCompanyCodeConfirm = MutableLiveData<Boolean>()

    // 정맥 타입
    val arteryType: MutableLiveData<ArteryType> by lazy { MutableLiveData<ArteryType>() }

    // 가입 필수값 체크
    val signEssentialChk: MutableLiveData<SignFormErrorType> by lazy { MutableLiveData<SignFormErrorType>() }

    // 가입 성공 여부
    val signInStatus = MutableLiveData<Boolean>()

    init {
        userDao = ArteryDatabase.getInstance(resourceProvider.getContext())!!.userDao()
    }

    /**
     * 가입하기
     */
    fun doSignIn() {
        val userData = User(0, user_id.value.toString(), user_pw.value.toString(),
            user_name.value.toString(), user_phonenum.value.toString(),
            user_company_code.value.toString(), ArteryType.LEFT.name, "0")
        addDisposable(
            userDao.insertUser(userData).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    signInStatus.postValue(true)
                    Log.e("성공??", userData.userId)
                }, {
                    signInStatus.postValue(false)
                    Log.e("실패??", it.cause.toString())
                })
        )
    }

    /**
     * 아이디 중복체크
     * true : 중복된 아이디
     */
    fun isEffectiveId() {
        // 내부 DB상에 존재하는지 체크
        // TODO 차후 서버에서 체크하도록 수정 필요
        addDisposable(
            userDao.searchId(user_id.value.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ user ->
                    if (user.userId.equals(user_id.value.toString())) {
                        // 중복된 아이디가 존재한다.
                        // 아이디 재입력 필요
                        idValiableChk.postValue(false)
                    } else {
                        idValiableChk.postValue(true)
                        idOverlapId = true
                    }
                }, {
                    idValiableChk.postValue(true)
                    idOverlapId = true
                })
        )
    }

    /**
     * 회원가입 Form 입력 여부 체크
     * edittext 참고 : https://steemit.com/kr/@jeonghamin/andoird-5-mvvm-edittext
     * 모든 edittext에 넣고, 회원가입 폼 체크하 면 될 것 같음
     */
    fun signInTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            TODO("Not yet implemented")
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            signEssentialFormCheck()
        }

        override fun afterTextChanged(p0: Editable?) {
            TODO("Not yet implemented")
        }
    }

    /**
     * 가입하기 클릭 시
     * 필수란 체크
     */
    fun signEssentialFormCheck() {
        if (user_id.value.isNullOrEmpty()) {
            signEssentialChk.postValue(SignFormErrorType.ID_EMPTY)
        } else if (!idOverlapId) {
            signEssentialChk.postValue(SignFormErrorType.ID_OVERLAP_CHK_EMPTY)
        } else if (user_pw.value.isNullOrEmpty()) {
            signEssentialChk.postValue(SignFormErrorType.PW_EMPTY)
        } else if (user_pw_confirm.value.isNullOrEmpty()) {
            signEssentialChk.postValue(SignFormErrorType.PW_COMFIRM_CHK_EMPTY)
        } else if (user_pw.value.toString() != user_pw_confirm.value.toString()) {
            signEssentialChk.postValue(SignFormErrorType.PW_NOT_CORRECT)
        } else if (user_name.value.isNullOrEmpty()) {
            signEssentialChk.postValue(SignFormErrorType.NAME_EMPTY)
        } else if (user_phonenum.value.isNullOrEmpty()) {
            signEssentialChk.postValue(SignFormErrorType.PHONE_NUM_EMPTY)
        } else if (Pattern.matches("^01(?:0|1|[6-9]) - (?:\\d{3}|\\d{4}) - \\d{4}\$",
                user_phonenum.value.toString())
        ) {
            signEssentialChk.postValue(SignFormErrorType.PHONE_NUM_NOT_CORRECT)
        } else if (user_company_code.value.isNullOrEmpty()) {
            signEssentialChk.postValue(SignFormErrorType.COMPANY_CODE_EMPTY)
        } else if (isCompanyCodeConfirm.value == false) {
            signEssentialChk.postValue(SignFormErrorType.COMPANY_CODE_CHK_EMPTY)
        } else {
            // 필수값이 모두 체크되었을 경우 가입 진행
            doSignIn()
        }
    }
}