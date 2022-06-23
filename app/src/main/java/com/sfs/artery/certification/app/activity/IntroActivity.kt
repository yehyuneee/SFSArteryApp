package com.sfs.artery.certification.app.activity

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.sfs.artery.certification.app.R
import com.sfs.artery.certification.app.extention.startActivity

class IntroActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                startActivity<LoginActivity>()
            }
        }, 1000)
    }
}