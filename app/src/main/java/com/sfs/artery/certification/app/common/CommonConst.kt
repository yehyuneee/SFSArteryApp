package com.sfs.artery.certification.app.common

import androidx.annotation.StringRes
import com.sfs.artery.certification.app.R

/**
 * 회원가입란 필수 체크 타입
 */
enum class SignFormErrorType(@StringRes val msg: Int) {
    ID_EMPTY(msg = R.string.sign_in_id_empty),
    ID_OVERLAP_CHK_EMPTY(msg = R.string.sign_in_overlap_chk_empty),
    PW_EMPTY(msg = R.string.sign_in_pw_empty),
    PW_COMFIRM_CHK_EMPTY(msg = R.string.sign_in_pw_confirm_empty),
    PW_NOT_CORRECT(msg = R.string.sign_in_pw_confirm_error),
    NAME_EMPTY(msg = R.string.sign_in_name_empty),
    PHONE_NUM_EMPTY(msg = R.string.sign_in_number_empty),
    PHONE_NUM_NOT_CORRECT(msg = R.string.sign_in_number_not_correct),
    COMPANY_CODE_EMPTY(msg = R.string.sign_in_company_code_empty),
    COMPANY_CODE_CHK_EMPTY(msg = R.string.sign_in_company_code_confirm_empty)
}

/**
 * Alert 버튼 타입
 */
enum class AlertDialogBtnType {
    ONE
}

/**
 * 정맥(손) 타입
 */
enum class ArteryType {
    LEFT,
    RIGHT
}