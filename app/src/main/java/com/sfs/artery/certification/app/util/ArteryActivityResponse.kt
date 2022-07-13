package com.sfs.artery.certification.app.util

import jp.co.normee.palmvein.NRPalmViewMsg.MsgType
import java.io.Serializable
import java.util.ArrayList

class ArteryActivityResponse : Serializable {
    private val serialVersionUID = 1L
    var Type: MsgType? = null
    var Value = 0
    var ValueStr: String? = null
    var AuditLog = ArrayList<String>()
}