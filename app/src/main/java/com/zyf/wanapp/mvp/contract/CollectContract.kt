package com.zyf.wanapp.mvp.contract

import com.zyf.wanapp.base.IPresenter
import com.zyf.wanapp.base.IView
import com.zyf.wanapp.mvp.model.bean.CollectionArticle
import com.zyf.wanapp.mvp.model.bean.CollectionResponseBody

/**
 * Created by zyf on 2018/9/6.
 */
interface CollectContract {

    interface View : IView {

        fun setCollectList(articles: CollectionResponseBody<CollectionArticle>)

        fun removeCollectSuccess(success: Boolean)
    }

    interface Presenter : IPresenter<View> {

        fun requestCollectList(page: Int)

        fun removeCollectArticle(id: Int, originId: Int)
    }
}