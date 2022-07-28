package com.sfs.artery.certification.app.activity

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

    companion object {
        val USER_INFO_ID = "USER_INFO_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewModel) {
            setUserInfo(intent.extras?.get(USER_INFO_ID).toString())

            userInfo.observe(this@NormalUserInfoActivity, { user ->
                viewBinding!!.apply {
                    infoUserIdText.text = String.format(
                        getString(R.string.normal_user_info_artery_id_title),
                        user.userId)

                    userInfoCompanyName.text = user.userCompanyCode
                    userInfoName.text = user.userName
                    userInfoPhonenum.text = user.userPhoneNum
                }
            })
        }

        viewBinding!!.headerView.setHeader(
            HeaderView.HEADER_BASIC,
            applicationContext.getString(R.string.normal_user_info_text),
            this@NormalUserInfoActivity
        )
    }
}