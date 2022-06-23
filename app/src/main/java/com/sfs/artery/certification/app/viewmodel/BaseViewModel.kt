package com.sfs.artery.certification.app.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()


    /**
     * add된 옵저블 해지
     */
    fun clearDisposable(){
        compositeDisposable.clear()
    }

    /**
     * viewModel이 제거될 때 호출됨
     * compositeDisposable 재사용할 일이 없으므로 dispose() 수행
     * dispose() : 옵저버블에게 더이상 데이터를 발행하지 않도록 구독을 해지하는 함수
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}