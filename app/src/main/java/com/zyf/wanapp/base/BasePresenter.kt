package com.zyf.wanapp.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by zyf on 2018/8/31.
 */
open class BasePresenter<V : IView> : IPresenter<V>, LifecycleObserver {

    var mRootView: V? = null
        private set

    //可以快速解除所有添加的Disposable类,每当我们得到一个Disposable时就调用
    private var compositeDisposable = CompositeDisposable()

    override fun attachView(mRootView: V) {
        this.mRootView = mRootView
        if (mRootView is LifecycleOwner) {
            (mRootView as LifecycleOwner).lifecycle.addObserver(this)
        }
    }

    override fun detachView() {
        mRootView = null
        //保证activity结束时取消所有正在执行的订阅
        //isDisposed():查询是否解除订阅 true 代表 已经解除订阅
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }
    }

    fun addSubscription(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

    private val isViewAttached: Boolean
        get() = mRootView == null

    fun checkViewAttached(){
        if (!isViewAttached) throw MvpViewNotAttachedException()
    }

    private class MvpViewNotAttachedException internal constructor():
            RuntimeException("Please call IPresenter.attachView(IBaseView) before " +
                    "requesting data to the IPresenter")

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner){
        detachView()
        owner.lifecycle.removeObserver(this)
    }
}