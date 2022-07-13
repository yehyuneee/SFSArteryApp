package com.sfs.artery.certification.app.activity

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.sfs.artery.certification.app.BR
import com.sfs.artery.certification.app.R
import com.sfs.artery.certification.app.activity.ArteryActivity.Companion.KEY_NRPALMACTIVITY_RESPONS
import com.sfs.artery.certification.app.common.AlertDialogBtnType
import com.sfs.artery.certification.app.common.ArteryType
import com.sfs.artery.certification.app.databinding.ActivitySignInBinding
import com.sfs.artery.certification.app.extention.moveArteryActivity
import com.sfs.artery.certification.app.util.ArteryActivityResponse
import com.sfs.artery.certification.app.util.CommonDialogListener
import com.sfs.artery.certification.app.view.HeaderView
import com.sfs.artery.certification.app.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.co.normee.palmvein.NRPalmViewDesc
import jp.co.normee.palmvein.NRPalmViewMsg

@AndroidEntryPoint
class SignInActivity : BaseActivity<ActivitySignInBinding, SignInViewModel>(),
    View.OnClickListener {
    override val layoutId: Int = R.layout.activity_sign_in
    override val variable: Int = BR.viewmodel
    override val viewModel: SignInViewModel by viewModels()

    private var mUserId = ""
    private val mActivity = this@SignInActivity

    val LEFT_HAND_CODE = 0
    val RIGHT_HAND_CODE = 1

    private lateinit var mResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()

        with(viewModel) {
            signEssentialChk.observe(this@SignInActivity, { errMsg ->
                // 필수값 체크
                // 에러사항들 알림팝업 노출
                showCommonDialog(AlertDialogBtnType.ONE,
                    this@SignInActivity.resources.getString(errMsg.msg))
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
                val ableBackground = R.drawable.artery_enroll_box_layout

                if (!mUserId.equals(id)) {
                    idOverlapId = false
                    arteryEnroll = false
                    if (arteryType.value == ArteryType.RIGHT) {
                        viewBinding!!.rightHandArteryLayout.setBackgroundResource(
                            ableBackground)
                        viewBinding!!.rightHandArteryLayout.isClickable = true
                    } else {
                        viewBinding!!.rightHandArteryLayout.setBackgroundResource(
                            ableBackground)
                        viewBinding!!.leftHandArteryLayout.isClickable = true
                    }
                }
                mUserId = id
            })
        }
    }

    fun initView() {
        mResult = activityForResult()

        with(viewBinding!!) {
            headerView.setHeader(HeaderView.HEADER_BACK,
                applicationContext.getString(R.string.login_sign_in),
                this@SignInActivity)

            signInNumberEdit.addTextChangedListener(PhoneNumberFormattingTextWatcher())
            signInOverlapBtn.setOnClickListener(mActivity)
            leftHandArteryLayout.setOnClickListener(mActivity)
            rightHandArteryLayout.setOnClickListener(mActivity)
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            viewBinding!!.signInOverlapBtn.id -> {
                // 아이디 중복확인
                val id = viewBinding!!.signInIdEdit.text
                if (id?.isEmpty() == false) {
                    viewModel.isEffectiveId()
                } else {
                    showCommonDialog(AlertDialogBtnType.ONE, "아이디를 입력해주세요.")
                }
            }
            viewBinding!!.leftHandArteryLayout.id -> {
                // 왼쪽 정맥
                moveArteryEnrollActivity(LEFT_HAND_CODE)
            }
            viewBinding!!.rightHandArteryLayout.id -> {
                // 오른쪽 정맥
                moveArteryEnrollActivity(RIGHT_HAND_CODE)
            }
        }
    }

    /**
     * 정맥 등록 페이지 이동
     */
    fun moveArteryEnrollActivity(handCode: Int) {
        val requestCode = ArteryActivity.KEY_NRPALMACTIVITY_REQUEST_INIT
        val setting = NRPalmViewDesc.InitDescs()

        if (viewBinding!!.signInIdEdit.text.isNullOrEmpty() && !viewModel.idOverlapId) {
            showCommonDialog(AlertDialogBtnType.ONE, "아이디 입력 및 중복 체크해주세요.")
            return
        }

        if (handCode == LEFT_HAND_CODE) {
            viewModel.arteryType.postValue(ArteryType.LEFT)
        } else {
            viewModel.arteryType.postValue(ArteryType.RIGHT)
        }

        setting.Setting.UserID = viewBinding!!.signInIdEdit.text.toString().toInt()
        setting.Setting.SubID = handCode
        setting.Call.CMode = NRPalmViewDesc.CallDesc.CallMode.REGISTER
        setting.Setting.IsOutputAuditLog = false

        moveArteryActivity<ArteryActivity>(setting, requestCode, mResult)
    }

    fun activityForResult(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            var alertText: String? = "N/A"
            if (result.resultCode == 0) {
                alertText = getString(R.string.artery_error)
            } else {
                val res: ArteryActivityResponse =
                    result.data?.getSerializableExtra(KEY_NRPALMACTIVITY_RESPONS) as ArteryActivityResponse
                when (res.Value) {
//                    NRPalmViewMsg.MSGVALUE_CANCEL -> alertText =
//                        getString(R.string.artery_error)
                    NRPalmViewMsg.MSGVALUE_SUCCESS -> {
                        alertText = getString(R.string.artery_success)
                        if (res.ValueStr != null) {
                            alertText += res.ValueStr
                        }
                        with(viewModel) {
                            var handType: String
                            val unableBackground = R.drawable.artery_enroll_click_box_layout
                            if (arteryType.value == ArteryType.RIGHT) {
                                handType = getString(R.string.artery_right_hand)
                                viewBinding!!.rightHandArteryLayout.setBackgroundResource(
                                    unableBackground)
                                viewBinding!!.rightHandArteryLayout.isClickable = false
                            } else {
                                handType = getString(R.string.artery_left_hand)
                                viewBinding!!.leftHandArteryLayout.setBackgroundResource(
                                    unableBackground)
                                viewBinding!!.leftHandArteryLayout.isClickable = false
                            }
                            viewBinding!!.arteryConfirmText.text =
                                String.format(getString(R.string.sign_in_artery_enroll_success),
                                    handType)
                            arteryEnroll = true
                        }
                    }
                    NRPalmViewMsg.MSGVALUE_FAILURE -> {
                        alertText = getString(R.string.artery_fail)
                        if (res.ValueStr != null) {
                            alertText += res.ValueStr
                        }
                    }
                    else -> alertText =
                        java.lang.String.format(getString(R.string.artery_error) + "[%d]",
                            res.Value)
                }
            }
            showCommonDialog(AlertDialogBtnType.ONE, alertText.toString())
        }
    }
}