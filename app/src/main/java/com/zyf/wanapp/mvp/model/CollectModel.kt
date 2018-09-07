package com.zyf.wanapp.mvp.model

import com.zyf.wanapp.http.RetrofitHelper
import com.zyf.wanapp.mvp.model.bean.CollectionArticle
import com.zyf.wanapp.mvp.model.bean.CollectionResponseBody
import com.zyf.wanapp.mvp.model.bean.HttpResult
import com.zyf.wanapp.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by zyf on 2018/9/6.
 */
class CollectModel : CommonModel() {

    fun getCollectList(page: Int): Observable<HttpResult<CollectionResponseBody<CollectionArticle>>> =
            RetrofitHelper.service.getCollectList(page).compose(SchedulerUtils.ioToMain())

    fun removeCollectArticle(id: Int, originId: Int): Observable<HttpResult<Any>> =
            RetrofitHelper.service.removeCollectArticle(id, originId)
                    .compose(SchedulerUtils.ioToMain())
}