package com.zyf.wanapp.mvp.contract

import com.zyf.wanapp.mvp.model.bean.ArticleResponseBody

/**
 * Created by zyf on 2018/9/3.
 */
interface ProjectListContract {

    interface View : CommonContract.View {

        fun scrollToTop()

        fun setProjectList(articles: ArticleResponseBody)
    }

    interface Presenter : CommonContract.Presenter<View>{

        fun requestProjectList(page: Int, cid: Int)
    }
}