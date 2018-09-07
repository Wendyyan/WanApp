package com.zyf.wanapp.mvp.presenter

import com.zyf.wanapp.base.BasePresenter
import com.zyf.wanapp.http.RetryWithDelay
import com.zyf.wanapp.http.exception.ExceptionHandle
import com.zyf.wanapp.mvp.contract.SearchContract
import com.zyf.wanapp.mvp.model.SearchModel
import com.zyf.wanapp.mvp.model.bean.SearchHistoryBean
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.litepal.LitePal

/**
 * Created by zyf on 2018/9/5.
 */
class SearchPresenter : BasePresenter<SearchContract.View>(), SearchContract.Presenter {

    private val searchModel: SearchModel by lazy {
        SearchModel()
    }

    override fun requestHotKey() {
        mRootView?.showLoading()
        addSubscription(searchModel.requestHotKey()
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            setHotKeys(it.data)
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

    override fun requestSearchHistory() {
        doAsync {
            val historyList = LitePal.findAll(SearchHistoryBean::class.java)
            historyList.reverse()
            uiThread {
                mRootView?.setHistoryList(historyList)
            }
        }
    }

    override fun saveSearchKey(key: String) {
        doAsync {
            val bean = SearchHistoryBean(key.trim())
            val historyList = LitePal.where("key = '${key.trim()}'").find(SearchHistoryBean::class.java)
            if (historyList.size > 0) deleteHistoryById(historyList[0].id)
            bean.save()
        }
    }

    override fun deleteHistoryById(id: Long) {
        doAsync {
            LitePal.delete(SearchHistoryBean::class.java, id)
        }
    }

    override fun clearHistory() {
        doAsync {
            LitePal.deleteAll(SearchHistoryBean::class.java)
        }
    }
}