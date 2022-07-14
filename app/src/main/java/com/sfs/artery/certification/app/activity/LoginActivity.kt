package com.sfs.artery.certification.app.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.sfs.artery.certification.app.BR
import com.sfs.artery.certification.app.R
import com.sfs.artery.certification.app.databinding.ActivityLoginBinding
import com.sfs.artery.certification.app.extention.startActivity
import com.sfs.artery.certification.app.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

// AndroidEntryPoint : Hilt가 해당 클래스에 Dependency를 제공할 수 있는 Component를 생성해준다.
// Hilt 참고 : https://developer88.tistory.com/349
@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(), View.OnClickListener {
    override val layoutId: Int = R.layout.activity_login
    override val variable: Int = BR.viewmodel
    override val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding!!.loginSignInLayout.setOnClickListener(this)
        viewBinding!!.loginBtnLayout.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            viewBinding?.loginSignInLayout?.id -> startActivity<SignInActivity>()
            viewBinding?.loginBtnLayout?.id -> startActivity<NormalUserInfoActivity>()
        }
    }
}