package com.sfs.artery.certification.app.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.sfs.artery.certification.app.BR
import com.sfs.artery.certification.app.R
import com.sfs.artery.certification.app.common.AlertDialogBtnType
import com.sfs.artery.certification.app.common.ArteryType
import com.sfs.artery.certification.app.common.LoginProcessType
import com.sfs.artery.certification.app.databinding.ActivityLoginBinding
import com.sfs.artery.certification.app.extention.moveActivity
import com.sfs.artery.certification.app.extention.moveArteryActivity
import com.sfs.artery.certification.app.extention.moveNomalUserInfoActivity
import com.sfs.artery.certification.app.extention.startActivity
import com.sfs.artery.certification.app.util.ArteryActivityResponse
import com.sfs.artery.certification.app.util.CommonDialogListener
import com.sfs.artery.certification.app.util.PermissionListener
import com.sfs.artery.certification.app.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.co.normee.palmvein.NRPalmViewDesc
import jp.co.normee.palmvein.NRPalmViewMsg

// AndroidEntryPoint : Hilt가 해당 클래스에 Dependency를 제공할 수 있는 Component를 생성해준다.
// Hilt 참고 : https://developer88.tistory.com/349
@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(), View.OnClickListener {
    override val layoutId: Int = R.layout.activity_login
    override val variable: Int = BR.viewmodel
    override val viewModel: LoginViewModel by viewModels()

    private lateinit var mResult: ActivityResultLauncher<Intent>
    var mArteryUserId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mResult = activityForResult()

        with(viewModel) {
            loginStatus.observe(this@LoginActivity, { status ->
                if (status == LoginProcessType.ARTERY_LOGIN) {
                    mArteryUserId = arteryUserId.toInt()
                    moveArteryEnrollActivity()
                } else if (status == LoginProcessType.ID_LOGIN) {
                    moveNomalUserInfoActivity<NormalUserInfoActivity>(viewModel.userId.value.toString())
                }
            })

            loginFailType.observe(this@LoginActivity, { type ->
                showCommonDialog(
                    AlertDialogBtnType.ONE,
                    this@LoginActivity.resources.getString(type.msg)
                )
            })
        }

        viewBinding!!.loginSignInLayout.setOnClickListener(this)
    }

    /**
     * 정맥 인증 페이지 이동
     */
    fun moveArteryEnrollActivity() {
        val requestCode = ArteryActivity.KEY_NRPALMACTIVITY_REQUEST_INIT
        val setting = NRPalmViewDesc.InitDescs()

        setting.Setting.UserID = mArteryUserId
        setting.Setting.SubID = ArteryType.LEFT.code
        setting.Call.CMode = NRPalmViewDesc.CallDesc.CallMode.AUTHENTICATE
        setting.Setting.IsOutputAuditLog = false

        requestCameraPermission(object : PermissionListener {
            override fun onGranted() {
                moveArteryActivity<ArteryActivity>(setting, requestCode, mResult)
            }

            override fun onRefused() {
                showCommonDialog(AlertDialogBtnType.ONE, "카메라 권한 미허용으로 정맥 인증이 불가능합니다.")
            }
        })

    }


    fun activityForResult(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            var alertText: String? = ""
            if (result.resultCode == 0) {
                alertText = getString(R.string.artery_error)
            } else {
                val res: ArteryActivityResponse =
                    result.data?.getSerializableExtra(ArteryActivity.KEY_NRPALMACTIVITY_RESPONS) as ArteryActivityResponse
                val value = res.Value
                when (value) {
                    NRPalmViewMsg.MSGVALUE_CANCEL -> alertText =
                        getString(R.string.cancel_text)
                    NRPalmViewMsg.MSGVALUE_SUCCESS -> {
                        alertText = getString(R.string.artery_success_text_authenticate)
                        if (res.ValueStr != null) {
                            alertText += res.ValueStr
                        }
                        showCommonDialog(AlertDialogBtnType.ONE, alertText.toString()).apply {
                            dialogClick(object : CommonDialogListener {
                                override fun onConfirm() {
                                    dismiss()
                                    finish()
                                    moveNomalUserInfoActivity<NormalUserInfoActivity>(viewModel.userId.value.toString())
                                }

                                override fun onCancle() {
                                    dismiss()
                                }

                            })
                        }
                        return@registerForActivityResult
                    }
                    NRPalmViewMsg.MSGVALUE_FAILURE -> {
                        alertText = getString(R.string.artery_fail)
                        if (res.ValueStr != null) {
                            alertText += res.ValueStr
                        }
                    }
                    else -> {
                        alertText =
                            java.lang.String.format(
                                getString(R.string.artery_error) + "[%d]",
                                value
                            )
                    }
                }
            }
            showCommonDialog(AlertDialogBtnType.ONE, alertText.toString())
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            viewBinding?.loginSignInLayout?.id -> startActivity<SignInActivity>()
        }
    }
}