package com.sfs.artery.certification.app.dialog

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.sfs.artery.certification.app.R
import com.sfs.artery.certification.app.common.ChangePwErrorType
import com.sfs.artery.certification.app.databinding.ChangePwLayoutBinding
import com.sfs.artery.certification.app.roomdb.dao.UserDao
import com.sfs.artery.certification.app.util.CommonDialogListener
import com.sfs.artery.certification.app.viewmodel.ChangePwViewModel

class ChangePwDialogFragment(activity: Activity, userDao: UserDao) : DialogFragment() {
    var _databinding: ChangePwLayoutBinding? = null
    val mActivity = activity
    val mUserDao = userDao

    private val changePwViewModel: ChangePwViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _databinding = ChangePwLayoutBinding.inflate(LayoutInflater.from(context)).apply {
            viewmodel = changePwViewModel
        }
        initView()
        return _databinding!!.root
    }

    fun initView() {
        changePwViewModel.userDao = mUserDao
        with(changePwViewModel) {
            changePwState.observe(this@ChangePwDialogFragment, { type ->
                dismissDialog()

                val commondialog = CommonDialog(
                    context = mActivity
                )
                commondialog.apply {
                    setOneButtonType()
                    dialogClick(object : CommonDialogListener {
                        override fun onConfirm() {
                            dismiss()
                            if (type == ChangePwErrorType.CHANGE_PW) {
                                if (this@ChangePwDialogFragment.isVisible) {
                                    this@ChangePwDialogFragment.dismiss()
                                }
                            }
                        }

                        override fun onCancle() {
                            dismiss()
                        }
                    })
                    setContentMsg(getString(type.msg))
                    show()
                }
            })
        }

        _databinding!!.changePwCancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (this@ChangePwDialogFragment.isVisible) {
                    this@ChangePwDialogFragment.dismiss()
                }
            }
        })
    }
}