package com.zyf.wanapp.mvp.presenter

import com.zyf.wanapp.base.BasePresenter
import com.zyf.wanapp.http.RetryWithDelay
import com.zyf.wanapp.http.exception.ExceptionHandle
import com.zyf.wanapp.mvp.contract.CommonContract
import com.zyf.wanapp.mvp.model.CommonModel

/**
 * Created by zyf on 2018/8/30.
 */
open class CommonPresenter<V: CommonContract.View> :
        BasePresenter<V>(), CommonContract.Presenter<V> {

    private val mModel: CommonModel by lazy {
        CommonModel()
    }

    override fun addCollectArticle(id: Int) {
        val disposable = mModel.addCollectArticle(id)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            showCollectSuccess(true)
                        }
                    }
                }, {
                    mRootView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(it))
                    }
                })
        addSubscription(disposable)
    }

    override fun cancelCollectArticle(id: Int) {
        val disposable = mModel.cancelCollectArticle(id)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            showCancelCollectSuccess(true)
                        }
                    }
                }, {
                    mRootView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(it))
                    }
                })
        addSubscription(disposable)
    }
}