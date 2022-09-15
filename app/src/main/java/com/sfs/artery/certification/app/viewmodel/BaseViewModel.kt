package com.sfs.artery.certification.app.viewmodel

import androidx.lifecycle.ViewModel
import com.sfs.artery.certification.app.common.LoadingType
import com.sfs.artery.certification.app.common.SingleLiveEvent
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.observers.ConsumerSingleObserver

abstract class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()
    val loadingDialogState = SingleLiveEvent<LoadingType>()

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
     * 구독한 시점에 Loading Dialog 띄운다.
     */
    fun <A : Any, S : Single<A>> S.startLoading(): Single<A> =
        doOnSubscribe { showDialog() }

    /**
     * 데이터 발행 완료 시점
     * 성공했을 경우에만 발행된 데이터 세팅 가능
     */
    fun <A : Any, S : Single<A>> S.doComplete(success: (A) -> Unit, fail: () -> Unit): Disposable {
        val observer: ConsumerSingleObserver<A> = ConsumerSingleObserver({ response ->
            dismissDialog()
            success.invoke(response)
        }, {
            dismissDialog()
            fail.invoke()
        })
        subscribe(observer)
        return observer
    }

    /**
     * Show Loading Dialog
     */
    fun showDialog() {
        loadingDialogState.postValue(LoadingType.SHOW)
    }

    /**
     * Dismiss Loading Dialog
     */
    fun dismissDialog() {
        loadingDialogState.postValue(LoadingType.DISMISS)
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