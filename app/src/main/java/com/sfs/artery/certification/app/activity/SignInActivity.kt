package com.sfs.artery.certification.app.activity

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import com.sfs.artery.certification.app.BR
import com.sfs.artery.certification.app.R
import com.sfs.artery.certification.app.common.AlertDialogBtnType
import com.sfs.artery.certification.app.databinding.ActivitySignInBinding
import com.sfs.artery.certification.app.util.CommonDialogListener
import com.sfs.artery.certification.app.view.HeaderView
import com.sfs.artery.certification.app.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : BaseActivity<ActivitySignInBinding, SignInViewModel>(),
    View.OnClickListener {
    override val layoutId: Int = R.layout.activity_sign_in
    override val variable: Int = BR.viewmodel
    override val viewModel: SignInViewModel by viewModels()

    private var mUserId = ""

    private val mActivity = this@SignInActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()

        with(viewModel) {
            signEssentialChk.observe(this@SignInActivity, { errMsg ->
                // 필수값 체크
                // 에러사항들 알림팝업 노출
                showCommonDialog(AlertDialogBtnType.ONE,
                    this@SignInActivity.resources.getString(errMsg.msg))

                signInStatus.postValue(false)
            })

            signInStatus.observe(this@SignInActivity, { chkAll ->
                // 회원가입 여부
                if (chkAll) {
                    showCommonDialog(
                        AlertDialogBtnType.ONE,
                        "회원가입이 완료되었습니다.")
                        .let {
                            it.dialogClick(object : CommonDialogListener {
                                override fun onConfirm() {
                                    finish()
                                }

                                override fun onCancle() {
                                    it.dismiss()
                                }
                            })
                        }
                } else {
                    showCommonDialog(AlertDialogBtnType.ONE, "회원가입에 실패하였습니다.")
                }
            })

            idValiableChk.observe(this@SignInActivity, { isOverlap ->
                // 중복여부 체크
                if (!isOverlap) {
                    showCommonDialog(AlertDialogBtnType.ONE, "중복된 아이디입니다.")
                } else {
                    showCommonDialog(AlertDialogBtnType.ONE, "사용가능한 아이디입니다.")
                }
            })

            user_id.observe(this@SignInActivity, { id ->
                if (!mUserId.equals(id)) {
                    idOverlapId = false
                }
                mUserId = id
            })
        }
    }

    fun initView() {
        with(viewBinding!!) {
            headerView.setHeader(HeaderView.HEADER_BACK,
                applicationContext.getString(R.string.login_sign_in),
                this@SignInActivity)

            signInNumberEdit.addTextChangedListener(PhoneNumberFormattingTextWatcher())
            signInOverlapBtn.setOnClickListener(mActivity)
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            viewBinding!!.signInOverlapBtn.id -> {
                val id = viewBinding!!.signInIdEdit.text
                if (id?.isEmpty() == false) {
                    viewModel.isEffectiveId()
                } else {
                    showCommonDialog(AlertDialogBtnType.ONE, "아이디를 입력해주세요.")
                }
            }
        }
    }
}