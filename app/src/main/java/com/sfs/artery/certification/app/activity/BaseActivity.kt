package com.sfs.artery.certification.app.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleObserver
import com.sfs.artery.certification.app.common.AlertDialogBtnType
import com.sfs.artery.certification.app.common.LoadingType
import com.sfs.artery.certification.app.util.CommonDialogListener
import com.sfs.artery.certification.app.util.PermissionListener
import com.sfs.artery.certification.app.dialog.CommonDialog
import com.sfs.artery.certification.app.dialog.LoadingDialog
import com.sfs.artery.certification.app.viewmodel.BaseViewModel

abstract class BaseActivity<DataBiding : ViewDataBinding, model : BaseViewModel>
    : AppCompatActivity(), LifecycleObserver {

    private var _binding: DataBiding? = null
    val viewBinding get() = _binding

    abstract val layoutId: Int
    abstract val variable: Int
    abstract val viewModel: model

    lateinit var _permissionListener: PermissionListener
    var mLoadingDialog: LoadingDialog? = null

    val requestPermissionLancher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // 권한 획득 성공
            _permissionListener.onGranted()
        } else {
            _permissionListener.onRefused()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<DataBiding>(this, layoutId).apply {
            lifecycleOwner = this@BaseActivity
            setVariable(variable, viewModel)
            _binding = this
        }

        with(viewModel) {
            loadingDialogState.observe(this@BaseActivity, { type ->
                if (type == LoadingType.SHOW) {
                    showLoadingDialog()
                } else {
                    dismissLoadingDialog()
                }
            })
        }
    }

    protected fun showLoadingDialog() {
        mLoadingDialog?.let { dialog ->
            if (dialog.isShowing) {
                dialog.dismissDialog()
            }
        }
        mLoadingDialog = LoadingDialog(context = this)
        mLoadingDialog?.showDialog()
    }

    protected fun dismissLoadingDialog() {
        mLoadingDialog?.let { dialog ->
            if (dialog.isShowing) {
                dialog.dismissDialog()
            }
        }
        mLoadingDialog?.dismissDialog()
        mLoadingDialog = null
    }

    /**
     * One Or Two 버튼 공통 팝업 정의
     * 버튼별 리스너는 리턴되는 CommonDialog 내에서 각각 정의되도록 한다.
     */
    protected fun showCommonDialog(
        type: AlertDialogBtnType,
        msg: String,
    ): CommonDialog {
        return CommonDialog(
            context = this
        ).apply {
            if (type == AlertDialogBtnType.ONE) {
                setOneButtonType()

                dialogClick(object : CommonDialogListener {
                    override fun onConfirm() {
                        dismiss()
                    }

                    override fun onCancle() {
                        dismiss()
                    }
                })
            }
            setContentMsg(msg)
            show()
        }
    }

    fun requestCameraPermission(permissionListener: PermissionListener) {
        _permissionListener = permissionListener
        requestPermissionLancher.launch(Manifest.permission.CAMERA)
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.run {
            viewModel.clearDisposable()
        }
    }
}