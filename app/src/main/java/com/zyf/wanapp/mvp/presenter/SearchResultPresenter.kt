package com.zyf.wanapp.mvp.presenter

import com.zyf.wanapp.http.RetryWithDelay
import com.zyf.wanapp.http.exception.ExceptionHandle
import com.zyf.wanapp.mvp.contract.SearchResultContract
import com.zyf.wanapp.mvp.model.SearchResultModel

/**
 * Created by zyf on 2018/9/19.
 */
class SearchResultPresenter : CommonPresenter<SearchResultContract.View>(),
        SearchResultContract.Presenter {

    private val searchResultModel by lazy {
        SearchResultModel()
    }

    override fun requestSearchResult(page: Int, key: String) {
        if (page == 0) mRootView?.showLoading()
        addSubscription(searchResultModel.requestSearchResult(page, key)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            setSearchResults(it.data)
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