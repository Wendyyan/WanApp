package com.zyf.wanapp.mvp.model

import com.zyf.wanapp.http.RetrofitHelper
import com.zyf.wanapp.mvp.model.bean.ArticleResponseBody
import com.zyf.wanapp.mvp.model.bean.HttpResult
import com.zyf.wanapp.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by zyf on 2018/9/19.
 */
class SearchResultModel {

    fun requestSearchResult(page: Int, key: String): Observable<HttpResult<ArticleResponseBody>> =
            RetrofitHelper.service.queryBySearchKey(page, key).compose(SchedulerUtils.ioToMain())
}