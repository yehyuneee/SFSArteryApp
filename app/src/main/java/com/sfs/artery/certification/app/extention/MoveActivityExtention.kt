package com.sfs.artery.certification.app.extention

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.sfs.artery.certification.app.activity.NormalUserInfoActivity.Companion.USER_INFO_ID
import jp.co.normee.palmvein.NRPalmViewDesc

/**
 * Activity 이동
 */
inline fun <reified T : Activity> Activity.startActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

/**
 * Activity 이동 후 현재 Activity 종료
 */
inline fun <reified T : Activity> Activity.moveActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
    finish()
}

/**
 * 정맥 인증/등록 Activity 이동
 */
inline fun <reified T : Activity> Activity.moveArteryActivity(
    setting: NRPalmViewDesc.InitDescs,
    requestKey: String,
    result: ActivityResultLauncher<Intent>,
) {
    val intent = Intent(this, T::class.java)
    intent.putExtra(requestKey, setting)
    result.launch(intent)
}

/**
 * Activity 이동
 */
inline fun <reified T : Activity> Activity.moveNomalUserInfoActivity(id: String) {
    val intent = Intent(this, T::class.java)
    intent.putExtra(USER_INFO_ID, id)
    startActivity(intent)
    finish()
}