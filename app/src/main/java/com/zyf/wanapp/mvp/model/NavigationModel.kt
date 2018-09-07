package com.zyf.wanapp.mvp.model

import com.zyf.wanapp.http.RetrofitHelper
import com.zyf.wanapp.mvp.model.bean.HttpResult
import com.zyf.wanapp.mvp.model.bean.NavigationBean
import com.zyf.wanapp.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by zyf on 2018/9/3.
 */
class NavigationModel {

    fun requestNavigationList(): Observable<HttpResult<List<NavigationBean>>> = RetrofitHelper
            .service.getNavigationList().compose(SchedulerUtils.ioToMain())
}