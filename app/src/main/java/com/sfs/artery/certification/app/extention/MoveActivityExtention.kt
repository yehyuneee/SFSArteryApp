package com.sfs.artery.certification.app.extention

import android.app.Activity
import android.content.Intent

inline fun <reified T : Activity> Activity.moveActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
    finish()
}


inline fun <reified T : Activity> Activity.startActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}