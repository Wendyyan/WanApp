package com.zyf.wanapp.mvp.presenter

import com.zyf.wanapp.base.BasePresenter
import com.zyf.wanapp.http.RetryWithDelay
import com.zyf.wanapp.http.exception.ExceptionHandle
import com.zyf.wanapp.mvp.contract.NavigationContract
import com.zyf.wanapp.mvp.model.NavigationModel

/**
 * Created by zyf on 2018/9/3.
 */
class NavigationPresenter : BasePresenter<NavigationContract.View>(), NavigationContract.Presenter {

    private val navigationModel: NavigationModel by lazy {
        NavigationModel()
    }

    override fun requestNavigationList() {
        mRootView?.showLoading()
        addSubscription(navigationModel.requestNavigationList()
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            setNavigationList(it.data)
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