package com.sfs.artery.certification.app.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import com.sfs.artery.certification.app.R
import com.sfs.artery.certification.app.util.CommonDialogListener

class CommonDialog(context: Context) : AppCompatDialog(context) {
    val mContext = context
    private var mDialogContent: AppCompatTextView? = null
    private var mDialogConfirmBtn: LinearLayoutCompat? = null
    private var mDialogCancleBtn: LinearLayoutCompat? = null

    private var mDialogClickListener: CommonDialogListener? = null

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.common_alert_dialog)

        mDialogContent = findViewById(R.id.common_content_text)
        mDialogConfirmBtn = findViewById(R.id.common_confirm_btn)
        mDialogCancleBtn = findViewById(R.id.common_cancel_btn)

        mDialogConfirmBtn?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                mDialogClickListener.let {
                    it?.onConfirm()
                }
            }
        })

        mDialogCancleBtn?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                mDialogClickListener.let {
                    it?.onCancle()
                }
            }
        })
    }

    fun setOneButtonType() {
        mDialogCancleBtn?.isVisible = false
    }

    fun setContentMsg(msg: String) {
        mDialogContent?.text = msg
    }

    fun dialogClick(listener: CommonDialogListener?) {
        mDialogClickListener = listener
    }
}