package com.zyf.wanapp.mvp.presenter

import com.zyf.wanapp.http.RetryWithDelay
import com.zyf.wanapp.http.exception.ExceptionHandle
import com.zyf.wanapp.mvp.contract.KnowledgeContract
import com.zyf.wanapp.mvp.model.KnowledgeModel

/**
 * Created by zyf on 2018/9/3.
 */
class KnowledgePresenter : CommonPresenter<KnowledgeContract.View>(), KnowledgeContract.Presenter {

    private val knowledgeModel by lazy {
        KnowledgeModel()
    }

    override fun requestArticles(page: Int, cid: Int) {
        mRootView?.showLoading()
        addSubscription(knowledgeModel.getKnowledgeList(page, cid)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        hideLoading()
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            setArticles(it.data)
                        }
                    }
                }, {
                    mRootView?.run {
                        hideLoading()
                        showError(ExceptionHandle.handleException(it))
                    }
                }))
    }
}