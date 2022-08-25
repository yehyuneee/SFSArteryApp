package com.sfs.artery.certification.app.view

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import com.sfs.artery.certification.app.R
import jp.co.normee.palmveinui.*
import jp.co.normee.palmveinui.IResource.ResID
import java.util.*


class ArteryResourceManager(activity: Activity) : IResource {
    val mActivity = activity

    //    val mScreenSetting = ScreenSetting()
    var mScreenSetting: ScreenSetting
    val mPictures = arrayListOf<Bitmap>()
    val mChips = arrayListOf<PictChip>()
    val mTable = hashMapOf<IResource.ResID, IDefPos>()

    var width = 0
    var height = 0

    init {
        mScreenSetting = ScreenSetting()

        val resources = activity.resources
        val bitmapOption = BitmapFactory.Options()
        bitmapOption.inScaled = false
        bitmapOption.inDither = false
        bitmapOption.inPreferQualityOverSpeed = true
        bitmapOption.inPreferredConfig = Bitmap.Config.ARGB_8888

        val display = mActivity.windowManager.defaultDisplay // in case of Activity
        val size = Point()
        display.getRealSize(size)
        width = pxToDp(size.x)
        height = pxToDp(size.y) - 180

        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.tex2,
            bitmapOption))
        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.artery_cancel_btn,
            bitmapOption)) // 1. x표시
        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.artery_confirm,
            bitmapOption))// 2. 확인
        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.artery_fail_again,
            bitmapOption)) // 3. 인증 실패
        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.artery_auth_fail,
            bitmapOption)) // 4. 등록 실패
        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.artery_again,
            bitmapOption)) // 5. 다시 시도 (재촬영)
        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.artery_fail,
            bitmapOption)) // 6. 인식 실패
        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.artery_success,
            bitmapOption)) // 7. 인식 성공
        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.artery_info,
            bitmapOption)) // 8. 손바닥을 원형 프레임을..
        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.artery_white_frame,
            bitmapOption)) // 9. 흰색 프레임에..
        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.artery_again_one_more,
            bitmapOption)) // 10. 한번 더 촬영
        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.artery_not_correct_hand,
            bitmapOption)) // 11. 손바닥이 올바른 위치에..
        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.artery_center_hand,
            bitmapOption)) // 12. 화면 중앙에 ..
        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.artery_other_place,
            bitmapOption)) // 13. 촬영 품질이 좋지..
        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.artery_check_hand,
            bitmapOption)) // 14. 정상적이지 않은..

        mPictures.add(BitmapFactory.decodeResource(resources,
            R.drawable.textbox,
            bitmapOption))

        mChips.add(PictChip(mPictures.get(0), 0, 100, 512, 100)) // 메세지 박스
        mChips.add(PictChip(mPictures.get(1))) // x 표시 (1)
        mChips.add(PictChip(mPictures.get(2)))
        mChips.add(PictChip(mPictures.get(3)))
        mChips.add(PictChip(mPictures.get(4)))
        mChips.add(PictChip(mPictures.get(5)))
        mChips.add(PictChip(mPictures.get(6)))
        mChips.add(PictChip(mPictures.get(7)))
        mChips.add(PictChip(mPictures.get(8)))
        mChips.add(PictChip(mPictures.get(9)))
        mChips.add(PictChip(mPictures.get(10)))
        mChips.add(PictChip(mPictures.get(11)))
        mChips.add(PictChip(mPictures.get(12)))
        mChips.add(PictChip(mPictures.get(13)))
        mChips.add(PictChip(mPictures.get(14)))
        mChips.add(PictChip(mPictures.get(15)))

        mTable.put(ResID.IDSCR_GUIDE,
            PosGuide(0.5F,
                "",
                ""));

        setPhoneResource()
    }

    fun pxToDp(px: Int): Int {
        return (px / mActivity.resources.displayMetrics.density).toInt()
    }

    fun setPhoneResource() {
        mTable.put(ResID.IDBTN_REGBREAK, PosButton(mChips.get(1), 420, -100, 50, 50, "", 0))
        mTable.put(ResID.IDWND_REG_HOWTO_MESSAGE,
            PosMsg(50,
                height,
                400,
                65,
                "",
                20,
                0,
                mChips.get(8)))
        mTable.put(ResID.IDWND_REG_USERCHECK_MESSAGE,
            PosMsg(50,
                height - 70,
                400,
                65,
                "",
                20,
                0,
                mChips.get(9)))
        mTable.put(ResID.IDWND_REG_USERCHECK_NEXTCAP_MESSAGE,
            PosMsg(50,
                height - 50,
                400,
                45,
                "",
                20,
                0,
                mChips.get(10)))
        mTable.put(ResID.ID_MSG_REG_CHECK_NO_PALM,
            PosMsg(50,
                height - 70,
                400,
                65,
                "",
                20,
                0,
                mChips.get(11)))
        mTable.put(ResID.ID_MSG_REG_CHECK_WRONG_POS,
            PosMsg(50,
                height,
                400,
                45,
                "",
                20,
                0,
                mChips.get(12)))
        mTable.put(ResID.ID_MSG_REG_CHECK_BAD_QUALITY,
            PosMsg(50,
                height,
                400,
                65,
                "",
                20,
                0,
                mChips.get(13)))
        mTable.put(ResID.ID_MSG_REG_CHECK_INVALID,
            PosMsg(50,
                height,
                400,
                45,
                "",
                20,
                0,
                mChips.get(14)))
        mTable.put(ResID.IDBTN_REG_USERCHECK_OK,
            PosButton(mChips.get(2),
                85,
                height,
                150,
                40,
                "",
                20))
        mTable.put(ResID.IDBTN_REG_USERCHECK_RETRY,
            PosButton(mChips.get(5),
                85 + 150 + 10,
                height,
                150,
                40,
                "",
                20))
        mTable.put(ResID.IDBTN_REG_USERCHECK_RETRY,
            PosButton(mChips.get(5),
                85 + 150 + 10,
                height,
                150,
                40,
                "",
                20))
        mTable.put(ResID.IDWND_REG_FAILE_MESSAGE,
            PosMsg(50,
                height,
                400,
                70,
                "",
                20,
                0,
                mChips.get(4)))
        mTable.put(ResID.IDBTN_REG_FAILE_MESSAGE,
            PosButton(mChips.get(2),
                85,
                height,
                150,
                40,
                "",
                20))
        mTable.put(ResID.IDWND_MATCH_FAILE_MESSAGE,
            PosMsg(50,
                height,
                400,
                70,
                "",
                20,
                0,
                mChips.get(3)))
        mTable.put(ResID.IDBTN_MATCH_FAILE_MESSAGE,
            PosButton(mChips.get(2),
                85,
                height,
                150,
                40,
                "",
                20))
    }

    override fun getPositions(): HashMap<IResource.ResID, IDefPos> {
        mTable.put(ResID.IDBTN_REG_USERCHECK_OK,
            PosButton(mChips.get(2),
                63,
                height,
                160,
                40,
                "",
                20))
        mTable.put(ResID.IDBTN_REG_USERCHECK_RETRY,
            PosButton(mChips.get(5),
                85 + 150 + 13,
                height,
                160,
                40,
                "",
                20))
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