package com.zyf.wanapp.mvp.presenter

import com.zyf.wanapp.base.BasePresenter
import com.zyf.wanapp.http.RetryWithDelay
import com.zyf.wanapp.http.exception.ExceptionHandle
import com.zyf.wanapp.mvp.contract.KnowledgeTreeContract
import com.zyf.wanapp.mvp.model.KnowledgeTreeModel

/**
 * Created by zyf on 2018/9/3.
 */
class KnowledgeTreePresenter : BasePresenter<KnowledgeTreeContract.View>(), KnowledgeTreeContract.Presenter {

    private val knowledgeTreeModel by lazy {
        KnowledgeTreeModel()
    }

    override fun requestKnowledgeTree() {
        mRootView?.showLoading()
        addSubscription(knowledgeTreeModel.requestKnowledgeTree()
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0){
                            showError(it.errorMsg)
                        } else{
                            setKnowledgeTree(it.data)
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