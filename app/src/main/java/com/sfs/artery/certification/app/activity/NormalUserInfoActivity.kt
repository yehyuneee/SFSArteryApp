package com.sfs.artery.certification.app.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.sfs.artery.certification.app.BR
import com.sfs.artery.certification.app.R
import com.sfs.artery.certification.app.databinding.ActivityNormalUserInfoBinding
import com.sfs.artery.certification.app.view.HeaderView
import com.sfs.artery.certification.app.viewmodel.NormalUserInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NormalUserInfoActivity :
    BaseActivity<ActivityNormalUserInfoBinding, NormalUserInfoViewModel>() {

    override val layoutId: Int = R.layout.activity_normal_user_info
    override val variable: Int = BR.viewmodel
    override val viewModel: NormalUserInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding!!.headerView.setHeader(
            HeaderView.HEADER_BACK,
            applicationContext.getString(R.string.normal_user_info_text),
            this@NormalUserInfoActivity
        )
    }
}