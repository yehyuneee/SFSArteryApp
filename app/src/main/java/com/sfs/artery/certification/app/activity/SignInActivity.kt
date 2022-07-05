package com.sfs.artery.certification.app.activity

import android.os.Bundle
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
                    showCommonDialog(AlertDialogBtnType.ONE, "아이디를 입력해주세요.")
                }
            }
        }
    }
}