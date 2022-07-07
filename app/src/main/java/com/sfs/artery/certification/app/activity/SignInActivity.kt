package com.sfs.artery.certification.app.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import com.sfs.artery.certification.app.BR
import com.sfs.artery.certification.app.R
import com.sfs.artery.certification.app.common.AlertDialogBtnType
import com.sfs.artery.certification.app.databinding.ActivitySignInBinding
import com.sfs.artery.certification.app.view.HeaderView
import com.sfs.artery.certification.app.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : BaseActivity<ActivitySignInBinding, SignInViewModel>(),
    View.OnClickListener {
    override val layoutId: Int = R.layout.activity_sign_in
    override val variable: Int = BR.viewmodel
    override val viewModel: SignInViewModel by viewModels()

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
                if (chkAll) {
                    // 필수값 체크 모두 되었을 경우 회원가입 진행
                    // TODO 현재는 DB상에 넣지만, 추후 서버에 저장
                    doSignIn()
                }
            })
        }
    }

    fun initView() {
        viewBinding!!.headerView.setHeader(HeaderView.HEADER_BACK,
            applicationContext.getString(R.string.login_sign_in),
            this@SignInActivity)

        viewBinding!!.signInOverlapBtn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            viewBinding!!.signInOverlapBtn.id -> {
                val id = viewBinding!!.signInIdEdit.text
                if (id?.isEmpty() == false) {
                    if (viewModel.isEffectiveId(id.toString())) {
                        showCommonDialog(AlertDialogBtnType.ONE, "중복된 아이디입니다.")
                    } else {
                        showCommonDialog(AlertDialogBtnType.ONE, "사용가능한 아이디입니다.")
                    }
                } else {

                }
            }
        }
    }
}