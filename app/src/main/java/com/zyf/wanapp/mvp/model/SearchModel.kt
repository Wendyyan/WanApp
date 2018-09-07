package com.zyf.wanapp.mvp.model

import com.zyf.wanapp.http.RetrofitHelper
import com.zyf.wanapp.mvp.model.bean.HotKeyBean
import com.zyf.wanapp.mvp.model.bean.HttpResult
import com.zyf.wanapp.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by zyf on 2018/9/5.
 */
class SearchModel {

    fun requestHotKey(): Observable<HttpResult<List<HotKeyBean>>> =
            RetrofitHelper.service.getHotKey().compose(SchedulerUtils.ioToMain())
}