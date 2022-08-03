package com.sfs.artery.certification.app.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.sfs.artery.certification.app.BR
import com.sfs.artery.certification.app.R
import com.sfs.artery.certification.app.common.AlertDialogBtnType
import com.sfs.artery.certification.app.common.ArteryType
import com.sfs.artery.certification.app.databinding.ActivityNormalUserInfoBinding
import com.sfs.artery.certification.app.dialog.ChangeArteryDialogFragment
import com.sfs.artery.certification.app.dialog.ChangePwDialogFragment
import com.sfs.artery.certification.app.extention.moveArteryActivity
import com.sfs.artery.certification.app.roomdb.dao.UserDao
import com.sfs.artery.certification.app.util.ArteryActivityResponse
import com.sfs.artery.certification.app.util.ChangeArteryListener
import com.sfs.artery.certification.app.util.PermissionListener
import com.sfs.artery.certification.app.view.HeaderView
import com.sfs.artery.certification.app.viewmodel.NormalUserInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.co.normee.palmvein.NRPalmViewDesc
import jp.co.normee.palmvein.NRPalmViewMsg
import kotlin.random.Random

@AndroidEntryPoint
class NormalUserInfoActivity :
    BaseActivity<ActivityNormalUserInfoBinding, NormalUserInfoViewModel>() {

    override val layoutId: Int = R.layout.activity_normal_user_info
    override val variable: Int = BR.viewmodel
    override val viewModel: NormalUserInfoViewModel by viewModels()

    private lateinit var mResult: ActivityResultLauncher<Intent>

    private var mArteryHandCode: Int = ArteryType.LEFT.code

    lateinit var mArteryUserId: String
    lateinit var mUserDao: UserDao
    lateinit var mArteryChangeListener: ChangeArteryListener


    companion object {
        val USER_INFO_ID = "USER_INFO_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mResult = activityForResult()

        with(viewModel) {
            mUserDao = userDao
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

            changePwState.observe(this@NormalUserInfoActivity, {
                val changePwDialogFragment =
                    ChangePwDialogFragment(this@NormalUserInfoActivity, mUserDao)
                changePwDialogFragment.show(supportFragmentManager, changePwDialogFragment.tag)
            })
        }

        mArteryChangeListener = object : ChangeArteryListener {
            override fun onChangeArtery(handcode: Int) {
                mArteryHandCode = handcode
                if (mArteryHandCode == ArteryType.RIGHT.code) {
                    mArteryUserId = viewModel.userRightArteryId
                } else {
                    mArteryUserId = viewModel.userLeftArteryId
                }
                moveArteryEnrollActivity(handcode)
            }
        }
        with(viewBinding!!) {
            headerView.setHeader(
                HeaderView.HEADER_BASIC,
                applicationContext.getString(R.string.normal_user_info_text),
                this@NormalUserInfoActivity
            )

            changeArteryCode.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    val changeArteryDialogFragment =
                        ChangeArteryDialogFragment(this@NormalUserInfoActivity,
                            mArteryChangeListener)
                    changeArteryDialogFragment.show(supportFragmentManager,
                        changeArteryDialogFragment.tag)
                }
            })
        }
    }

    /**
     * 정맥 등록 페이지 이동
     */
    fun moveArteryEnrollActivity(handCode: Int) {
        val requestCode = ArteryActivity.KEY_NRPALMACTIVITY_REQUEST_INIT
        val setting = NRPalmViewDesc.InitDescs()

        Log.d("등록 2: ", mArteryUserId)
        setting.Setting.UserID = mArteryUserId.toInt()
        setting.Setting.SubID = handCode
        setting.Call.CMode = NRPalmViewDesc.CallDesc.CallMode.REGISTER
        setting.Setting.IsOutputAuditLog = false

        requestCameraPermission(object : PermissionListener {
            override fun onGranted() {
                moveArteryActivity<ArteryActivity>(setting, requestCode, mResult)
            }

            override fun onRefused() {
                showCommonDialog(AlertDialogBtnType.ONE,
                    getString(R.string.camera_permission_denied))
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
                        if (res.ValueStr != null) {
                            alertText += res.ValueStr
                        }
                        var handType: String
                        if (mArteryHandCode == ArteryType.RIGHT.code) {
                            handType = getString(R.string.artery_right_hand)
                        } else {
                            handType = getString(R.string.artery_left_hand)
                        }
                        alertText = String.format(
                            getString(R.string.sign_in_artery_enroll_success),
                            handType
                        )
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
}