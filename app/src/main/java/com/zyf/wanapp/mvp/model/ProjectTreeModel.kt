package com.zyf.wanapp.mvp.model

import com.zyf.wanapp.http.RetrofitHelper
import com.zyf.wanapp.mvp.model.bean.HttpResult
import com.zyf.wanapp.mvp.model.bean.ProjectTreeBean
import com.zyf.wanapp.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by zyf on 2018/9/4.
 */
class ProjectTreeModel {

    fun requestProjectTree(): Observable<HttpResult<List<ProjectTreeBean>>> = RetrofitHelper.service
            .getProjectTree().compose(SchedulerUtils.ioToMain())
}