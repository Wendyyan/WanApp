package com.zyf.wanapp.mvp.contract

import com.zyf.wanapp.mvp.model.bean.ArticleResponseBody

/**
 * Created by zyf on 2018/9/3.
 */
interface KnowledgeContract {

    interface View : CommonContract.View {

        fun setArticles(articles: ArticleResponseBody)
    }

    interface Presenter : CommonContract.Presenter<View>{

        fun requestArticles(page: Int, cid: Int)
    }
}