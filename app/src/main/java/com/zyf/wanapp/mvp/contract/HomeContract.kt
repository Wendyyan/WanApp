package com.zyf.wanapp.mvp.contract

import com.zyf.wanapp.mvp.model.bean.ArticleResponseBody
import com.zyf.wanapp.mvp.model.bean.BannerBean

/**
 * Created by zyf on 2018/8/30.
 */
interface HomeContract {

    interface View : CommonContract.View {

        fun scrollToTop()

        fun setBanners(banners: List<BannerBean>)

        fun setArticles(articles: ArticleResponseBody)
    }

    interface Presenter: CommonContract.Presenter<View>{

        fun requestBanner()

        fun requestArticle(num: Int)
    }
}