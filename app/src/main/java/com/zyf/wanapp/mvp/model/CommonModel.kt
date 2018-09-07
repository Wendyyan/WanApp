package com.zyf.wanapp.mvp.model

import com.zyf.wanapp.http.RetrofitHelper
import com.zyf.wanapp.mvp.model.bean.HttpResult
import com.zyf.wanapp.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by zyf on 2018/8/30.
 */
open class CommonModel {

    fun addCollectArticle(id: Int): Observable<HttpResult<Any>> = RetrofitHelper.service
            .addCollectArticle(id).compose(SchedulerUtils.ioToMain())

    fun cancelCollectArticle(id: Int): Observable<HttpResult<Any>> = RetrofitHelper.service
            .cancelCollectArticle(id).compose(SchedulerUtils.ioToMain())
}