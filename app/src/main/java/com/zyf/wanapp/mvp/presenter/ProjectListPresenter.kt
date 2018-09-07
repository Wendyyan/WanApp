package com.zyf.wanapp.mvp.presenter

import com.zyf.wanapp.http.RetryWithDelay
import com.zyf.wanapp.http.exception.ExceptionHandle
import com.zyf.wanapp.mvp.contract.ProjectListContract
import com.zyf.wanapp.mvp.model.ProjectListModel

/**
 * Created by zyf on 2018/9/3.
 */
class ProjectListPresenter : CommonPresenter<ProjectListContract.View>(), ProjectListContract.Presenter {

    private val projectListModel: ProjectListModel by lazy {
        ProjectListModel()
    }

    override fun requestProjectList(page: Int, cid: Int) {
        if(page == 1) mRootView?.showLoading()
        addSubscription(projectListModel.requestProjectList(page, cid)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            setProjectList(it.data)
                        }
                        hideLoading()
                    }
                }, {
                    mRootView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(it))
                    }
                }))
    }
}