package com.sfs.artery.certification.app.activity

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.sfs.artery.certification.app.R
import com.sfs.artery.certification.app.common.ArteryType
import com.sfs.artery.certification.app.util.ArteryActivityResponse
import com.sfs.artery.certification.app.view.ArteryResourceManager
import jp.co.normee.palmvein.NRPalmView
import jp.co.normee.palmvein.NRPalmViewDesc
import jp.co.normee.palmvein.NRPalmViewDesc.InitDescs
import jp.co.normee.palmvein.NRPalmViewMsg
import java.io.Serializable
import java.util.*


class ArteryActivityTest : AppCompatActivity(), Handler.Callback {
    companion object {
        val KEY_NRPALMACTIVITY_REQUEST_INIT =
            "com.sfs.artery.certification.app.activity.arteryactivity.requestinit.intent_key"
        val KEY_NRPALMACTIVITY_REQUEST_DELETE =
            "com.sfs.artery.certification.app.activity.arteryactivity.requestdelete.intent_key"
        val KEY_NRPALMACTIVITY_RESPONS =
            "com.sfs.artery.certification.app.activity.arteryactivity.respons.intent_key"
    }

    lateinit var mView: FrameLayout
    lateinit var mHandImage: AppCompatImageView
    private var URView: NRPalmView? = null
    private val ActivityAuditLog = ArrayList<String>()
    private var UserID = 0
    private var SubID = 0
    private var IsActiveAuditLog = false
    private var mRequestType = NRPalmViewDesc.CallDesc.CallMode.REGISTER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        decorView.systemUiVisibility = uiOptions

        setContentView(R.layout.activity_artery_test)

        var descs: InitDescs? = null

        val intent: Intent?
        intent = getIntent()
        if (intent != null) {
            var obj: Serializable? = null

            if (intent.extras?.get(KEY_NRPALMACTIVITY_REQUEST_INIT) != null) {
                obj =
                    intent.extras?.get(KEY_NRPALMACTIVITY_REQUEST_INIT) as Serializable?

                descs = obj as InitDescs?
            }

            if (intent.extras?.get(KEY_NRPALMACTIVITY_REQUEST_DELETE) != null) {
                obj =
                    intent.extras?.get(KEY_NRPALMACTIVITY_REQUEST_DELETE) as Serializable?

                descs = obj as InitDescs?
                IsActiveAuditLog = descs!!.Setting.IsOutputAuditLog
                deleteUserData(descs.Setting.UserID)
                return
            }
        }

        UserID = descs?.Setting?.UserID ?: 0
        SubID = descs?.Setting?.SubID ?: 0
        IsActiveAuditLog = descs?.Setting?.IsOutputAuditLog ?: false
        mRequestType = descs?.Call?.CMode!!

        mView = findViewById(R.id.normee_view)
        mHandImage = findViewById(R.id.hand_image)

        val display = windowManager.defaultDisplay // in case of Activity
        val size = Point()
        display.getRealSize(size)
        var height = pxToDp(size.y)
        var width = pxToDp(size.x)

        var arteryResourceManager: ArteryResourceManager? = null
        arteryResourceManager = ArteryResourceManager(this@ArteryActivityTest)

        URView = NRPalmView(this@ArteryActivityTest,
            this@ArteryActivityTest,
            descs,
            arteryResourceManager)

        if (SubID.equals(ArteryType.LEFT.code)) {
            mHandImage.setImageDrawable(resources.getDrawable(R.drawable.left_hand,
                application.theme))
        } else {
            mHandImage.setImageDrawable(resources.getDrawable(R.drawable.right_hand,
                application.theme))
        }


        mView.addView(URView)
        mHandImage.bringToFront()
    }

    fun pxToDp(px: Int): Int {
        return (px / resources.displayMetrics.density).toInt()
    }

    fun deleteUserData(user_id: Int) {
        NRPalmView.deleteData(this, user_id, this, if (IsActiveAuditLog) ActivityAuditLog else null)
    }

    override fun handleMessage(arg0: Message): Boolean {
        if (!NRPalmViewMsg.isURMessage(arg0)) return false

        val respons = ArteryActivityResponse()
        respons.Type = NRPalmViewMsg.getMessageType(arg0)
        respons.Value = NRPalmViewMsg.getMessageValue(arg0)
        if (arg0.obj is String) {
            respons.ValueStr = arg0.obj as String
        }
        if (IsActiveAuditLog && respons.Type === NRPalmViewMsg.MsgType.Error) {
            ActivityAuditLog.add(NRPalmView.makeAuditLog(UserID, SubID, "er," + respons.Value))
        }

        if (URView != null) URView!!.getAuditLog(respons.AuditLog)
        respons.AuditLog.addAll(ActivityAuditLog)

        finishActivity(respons)
        return false
    }

    fun finishActivity(respons: ArteryActivityResponse) {
        val res = Intent()
        res.putExtra(KEY_NRPALMACTIVITY_RESPONS, respons)
        this.setResult(1, res)
        finish()
    }

    override fun onPause() {
        if (IsActiveAuditLog) ActivityAuditLog.add(NRPalmView.makeAuditLog(UserID, SubID, "op"))
        URView!!.exitPalmVein(this)
        super.onPause()
    }

    override fun onKeyDown(keycode: Int, event: KeyEvent?): Boolean {
        return if (keycode != KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(keycode, event)
        } else {
            if (IsActiveAuditLog) ActivityAuditLog.add(NRPalmView.makeAuditLog(UserID, SubID, "hb"))
            URView!!.exitPalmVein(this) //ライブラリに終了通知
            false
        }
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }

    override fun onUserLeaveHint() {
        if (IsActiveAuditLog) ActivityAuditLog.add(NRPalmView.makeAuditLog(UserID, SubID, "lh"))
        URView!!.exitPalmVein(this) //ライブラリに終了通知
        this.finish()
    }
}