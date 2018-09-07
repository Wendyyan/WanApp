package com.zyf.wanapp.mvp.presenter

import com.zyf.wanapp.base.BasePresenter
import com.zyf.wanapp.http.RetryWithDelay
import com.zyf.wanapp.http.exception.ExceptionHandle
import com.zyf.wanapp.mvp.contract.CollectContract
import com.zyf.wanapp.mvp.model.CollectModel

/**
 * Created by zyf on 2018/9/6.
 */
class CollectPresenter : BasePresenter<CollectContract.View>(), CollectContract.Presenter {

    private val collectModel: CollectModel by lazy {
        CollectModel()
    }

    override fun requestCollectList(page: Int) {
        mRootView?.showLoading()
        addSubscription(collectModel.getCollectList(page)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            setCollectList(it.data)
                        }
                        hideLoading()
                    }
                }, {
                    mRootView?.run {
                        hideLoading()
                        showError(ExceptionHandle.handleException(it))
                    }
                }))
    }

    override fun removeCollectArticle(id: Int, originId: Int) {
        addSubscription(collectModel.removeCollectArticle(id, originId)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            removeCollectSuccess(true)
                        }
                        hideLoading()
                    }
                }, {
                    mRootView?.run {
                        hideLoading()
                        showError(ExceptionHandle.handleException(it))
                    }
                }))
    }
}