package com.sfs.artery.certification.app.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import com.sfs.artery.certification.app.R

class HeaderView : LinearLayoutCompat {

    lateinit var mHeaderBack: AppCompatImageView
    lateinit var mHeaderTitle: AppCompatTextView
    lateinit var mBackActivity: Activity

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        initView()
    }

    companion object {
        val HEADER_BACK = "back"
        val HEADER_BASIC = "basic"
    }

    fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.header_view_layout, this, false)
        addView(view)

        mHeaderBack = view.findViewById(R.id.header_back_btn)
        mHeaderTitle = view.findViewById(R.id.header_title_text)
        mHeaderBack.setOnClickListener(object : OnClickListener {
            override fun onClick(p0: View?) {
                mBackActivity.finish()
            }
        })
    }

    // 헤더 타입에 따라 Visible 처리
    fun setHeader(type: String, title: String, activity: Activity) {
        when (type) {
            HEADER_BASIC -> mHeaderBack.isVisible = false
            HEADER_BACK -> mHeaderBack.isVisible = true
        }
        mHeaderTitle.text = title
        mBackActivity = activity
    }
}