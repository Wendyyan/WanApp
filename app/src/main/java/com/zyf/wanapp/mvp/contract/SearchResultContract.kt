package com.zyf.wanapp.mvp.contract

import com.zyf.wanapp.mvp.model.bean.ArticleResponseBody

/**
 * Created by zyf on 2018/9/19.
 */
class SearchResultContract {

    interface View : CommonContract.View {

        fun setSearchResults(articles: ArticleResponseBody)
    }

    interface Presenter : CommonContract.Presenter<View> {

        fun requestSearchResult(page: Int, key: String)
    }
}