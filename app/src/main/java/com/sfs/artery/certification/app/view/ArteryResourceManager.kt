package com.sfs.artery.certification.app.view

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.sfs.artery.certification.app.R
import jp.co.normee.palmveinui.*
import jp.co.normee.palmveinui.IResource.ResID
import jp.co.normee.palmveinui.exui.IAnimGenerator
import jp.co.normee.palmveinui.exui.IAnimeBase
import java.util.*

class ArteryResourceManager(activity: Activity) : IResource {

    val mActivity = activity

    val mScreenSetting = ScreenSetting()
    val mPictures = arrayListOf<Bitmap>()
    val mChips = arrayListOf<PictChip>()
    val mTable = hashMapOf<IResource.ResID, IDefPos>()

    init {
        val resources = activity.resources
        val bitmapOption = BitmapFactory.Options()
        bitmapOption.inScaled = false
        bitmapOption.inDither = false //ディザをOFFに
        bitmapOption.inPreferQualityOverSpeed = true
        bitmapOption.inPreferredConfig = Bitmap.Config.ARGB_8888

        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.tex2,
            bitmapOption))
        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.etc_btn_box_layout,
            bitmapOption))

        mChips.add(PictChip(mPictures.get(0), 0, 100, 512, 100)) // 메세지 박스
        mChips.add(PictChip(mPictures.get(0), 308, 1, 50, 45)) // x 표시
        mChips.add(PictChip(mPictures.get(1), 0, 0, 0, 0)) // 빈 메세지 박스

        setPhoneResource()

        mTable.put(ResID.IDSCR_GUIDE,
            PosGuide(0.5F,
                mActivity.getString(R.string.artery_left_hand),
                mActivity.getString(R.string.artery_right_hand)))
    }

    fun setPhoneResource() {
        mTable.put(ResID.IDBTN_REGBREAK, PosButton(mChips.get(1), 420, -100))
        mTable.put(ResID.IDWND_REG_HOWTO_MESSAGE,
            PosMsg(10,
                -50,
                460,
                65,
                mActivity.getString(R.string.artery_main_info_text),
                20,
                0,
                mChips.get(0)))
        mTable.put(ResID.IDWND_REG_USERCHECK_MESSAGE,
            PosMsg(10,
                5,
                460,
                65,
                mActivity.getString(R.string.artery_main_user_check_text),
                20,
                0,
                mChips.get(0)))
        mTable.put(ResID.IDWND_REG_USERCHECK_NEXTCAP_MESSAGE,
            PosMsg(10,
                280,
                460,
                65,
                mActivity.getString(R.string.artery_main_one_more),
                20,
                3000,
                mChips.get(0)))
        mTable.put(ResID.ID_MSG_REG_CHECK_NO_PALM,
            PosMsg(10,
                5,
                460,
                65,
                mActivity.getString(R.string.artery_proper_position),
                20,
                0,
                mChips.get(0)))
        mTable.put(ResID.ID_MSG_REG_CHECK_WRONG_POS,
            PosMsg(10,
                5,
                460,
                65,
                mActivity.getString(R.string.artery_hand_position_center),
                20,
                0,
                mChips.get(0)))
        mTable.put(ResID.ID_MSG_REG_CHECK_BAD_QUALITY,
            PosMsg(10,
                5,
                460,
                65,
                mActivity.getString(R.string.artery_bad_img_quality),
                20,
                0,
                mChips.get(0)))
        mTable.put(ResID.ID_MSG_REG_CHECK_INVALID,
            PosMsg(10,
                5,
                460,
                65,
                mActivity.getString(R.string.artery_invalid_hand),
                20,
                0,
                mChips.get(0)))
        mTable.put(ResID.IDBTN_REG_USERCHECK_OK,
            PosButton(mChips.get(0),
                85,
                570,
                150,
                65,
                mActivity.getString(R.string.artery_main_ok),
                20))
        mTable.put(ResID.IDBTN_REG_USERCHECK_RETRY,
            PosButton(mChips.get(0),
                85 + 150 + 10,
                570,
                200,
                65,
                mActivity.getString(R.string.artery_main_again),
                20))
        mTable.put(ResID.IDBTN_REG_USERCHECK_RETRY,
            PosButton(mChips.get(0),
                85 + 150 + 10,
                570,
                200,
                65,
                mActivity.getString(R.string.artery_main_again),
                20))
        mTable.put(ResID.IDWND_REG_FAILE_MESSAGE,
            PosMsg(10,
                280,
                460,
                85,
                mActivity.getString(R.string.artery_enroll_fail),
                20,
                0,
                mChips.get(0)))
        mTable.put(ResID.IDBTN_REG_FAILE_MESSAGE,
            PosButton(mChips.get(0),
                120,
                400,
                240,
                65,
                mActivity.getString(R.string.artery_main_ok),
                20))
        mTable.put(ResID.IDWND_MATCH_FAILE_MESSAGE,
            PosMsg(10,
                280,
                460,
                85,
                mActivity.getString(R.string.artery_verificate_fail),
                20,
                0,
                mChips.get(0)))
        mTable.put(ResID.IDBTN_MATCH_FAILE_MESSAGE,
            PosButton(mChips.get(0),
                120,
                400,
                240,
                65,
                mActivity.getString(R.string.artery_main_ok),
                20))
    }

    override fun getPositions(): HashMap<IResource.ResID, IDefPos> {
        return mTable
    }

    override fun getScreen(): ScreenSetting {
        return mScreenSetting
    }

    override fun recycleBitmap() {
        for (bmp in mPictures) {
            bmp.recycle()
        }
        mPictures.clear()
    }
}