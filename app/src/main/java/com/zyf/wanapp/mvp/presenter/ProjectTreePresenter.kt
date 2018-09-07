package com.zyf.wanapp.mvp.presenter

import com.zyf.wanapp.base.BasePresenter
import com.zyf.wanapp.http.RetryWithDelay
import com.zyf.wanapp.http.exception.ExceptionHandle
import com.zyf.wanapp.mvp.contract.ProjectContract
import com.zyf.wanapp.mvp.model.ProjectTreeModel

/**
 * Created by zyf on 2018/9/4.
 */
class ProjectTreePresenter : BasePresenter<ProjectContract.View>(),
        ProjectContract.Presenter {

    private val projectTreeModel by lazy {
        ProjectTreeModel()
    }

    override fun requestProjectTree() {
        mRootView?.showLoading()
        addSubscription(projectTreeModel.requestProjectTree()
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            setProjectTree(it.data)
                        }
                        hideLoading()
                    }
                }, {
                    mRootView?.run {
                        showError(ExceptionHandle.handleException(it))
                        hideLoading()
                    }
                }))
    }
}