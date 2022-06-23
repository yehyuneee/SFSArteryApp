package com.sfs.artery.certification.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * @HiltAndroidApp 어노테이션을 통해 의존성 주입의 시작점 알린다.
 * Hilt는 Application 생명주기를 따르며 컴파일 단계 시 DI 필요한 구성요소들을 초기화한다.
 */
@HiltAndroidApp
class SFSApplication : Application() {
}