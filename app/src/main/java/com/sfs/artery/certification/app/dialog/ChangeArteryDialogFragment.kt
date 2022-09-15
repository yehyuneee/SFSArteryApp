package com.sfs.artery.certification.app.dialog

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sfs.artery.certification.app.R
import com.sfs.artery.certification.app.common.ArteryType
import com.sfs.artery.certification.app.databinding.ChangeArteryLayoutBinding
import com.sfs.artery.certification.app.util.ChangeArteryListener

class ChangeArteryDialogFragment(activity: Activity, changeArteryListener: ChangeArteryListener) :
    DialogFragment() {
    var _databinding: ChangeArteryLayoutBinding? = null
    val mActivity = activity
    val mChangeArteryListener = changeArteryListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _databinding = ChangeArteryLayoutBinding.inflate(LayoutInflater.from(context))
        initView()
        return _databinding!!.root
    }

    fun initView() {
        with(_databinding!!) {
            leftHandArteryLayout.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    mChangeArteryListener.onChangeArtery(ArteryType.LEFT.code)
                    dismiss()
                }
            })
            rightHandArteryLayout.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    mChangeArteryListener.onChangeArtery(ArteryType.RIGHT.code)
                    dismiss()
                }
            })
        }
    }
}