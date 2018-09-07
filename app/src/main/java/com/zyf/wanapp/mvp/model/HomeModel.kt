package com.zyf.wanapp.mvp.model

import com.zyf.wanapp.http.RetrofitHelper
import com.zyf.wanapp.mvp.model.bean.ArticleResponseBody
import com.zyf.wanapp.mvp.model.bean.BannerBean
import com.zyf.wanapp.mvp.model.bean.HttpResult
import com.zyf.wanapp.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by zyf on 2018/8/31.
 */
class HomeModel : CommonModel() {

    fun getBanners(): Observable<HttpResult<List<BannerBean>>> = RetrofitHelper.service
            .getBanners().compose(SchedulerUtils.ioToMain())

    fun getArticles(num: Int): Observable<HttpResult<ArticleResponseBody>> =
            RetrofitHelper.service.getArticles(num).compose(SchedulerUtils.ioToMain())

}