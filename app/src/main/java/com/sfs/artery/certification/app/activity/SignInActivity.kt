package com.sfs.artery.certification.app.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.sfs.artery.certification.app.BR
import com.sfs.artery.certification.app.R
import com.sfs.artery.certification.app.databinding.ActivitySignInBinding
import com.sfs.artery.certification.app.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : BaseActivity<ActivitySignInBinding, SignInViewModel>() {
    override val layoutId: Int = R.layout.activity_sign_in
    override val variable: Int = BR.viewmodel
    override val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}