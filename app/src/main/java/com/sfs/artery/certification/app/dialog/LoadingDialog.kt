package com.sfs.artery.certification.app.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView
import com.sfs.artery.certification.app.R
import com.sfs.artery.certification.app.extention.CommonFunction

class LoadingDialog(context: Context) : Dialog(context) {
    lateinit var loadingImg: AppCompatImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setCancelable(false)
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.loading_dialog)

        loadingImg = findViewById(R.id.loading_img)
        CommonFunction.setLoadingImage(context, R.drawable.loading_dialog, loadingImg)
    }

    fun showDialog() {
        if (!this.isShowing) {
            this.show()
        }
    }

    fun dismissDialog() {
        if (this.isShowing) {
            this.dismiss()
        }
    }
}