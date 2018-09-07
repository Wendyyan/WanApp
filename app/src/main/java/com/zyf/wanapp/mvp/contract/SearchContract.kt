package com.zyf.wanapp.mvp.contract

import com.zyf.wanapp.base.IPresenter
import com.zyf.wanapp.base.IView
import com.zyf.wanapp.mvp.model.bean.HotKeyBean
import com.zyf.wanapp.mvp.model.bean.SearchHistoryBean

/**
 * Created by zyf on 2018/9/5.
 */
class SearchContract {

    interface View: IView {

        fun setHotKeys(hotKeys: List<HotKeyBean>)

        fun setHistoryList(list: List<SearchHistoryBean>)
    }

    interface Presenter : IPresenter<View> {

        fun requestHotKey()

        fun requestSearchHistory()

        fun saveSearchKey(key: String)

        fun deleteHistoryById(id: Long)

        fun clearHistory()
    }
}