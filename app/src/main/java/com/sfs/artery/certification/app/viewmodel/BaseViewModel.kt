package com.sfs.artery.certification.app.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()

    /**
     * Observer add
     */
    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    /**
     * add된 Observer 해지
     */
    fun clearDisposable() {
        compositeDisposable.clear()
    }

    /**
     * viewModel이 제거될 때 호출됨
     * compositeDisposable 재사용할 일이 없으므로 dispose() 수행
     * dispose() : 옵저버블에게 더이상 데이터를 발행하지 않도록 구독을 해지하는 함수
     */
    override fun onCleared() {
        super.onCleared()
        if (compositeDisposable.isDisposed) {
            // 리소스가 제대로 폐기됬는지 확인한 후 리소스 폐기한다.
            compositeDisposable.dispose()
        }
    }
}