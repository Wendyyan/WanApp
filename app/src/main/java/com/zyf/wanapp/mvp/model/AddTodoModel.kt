package com.zyf.wanapp.mvp.model

import com.zyf.wanapp.http.RetrofitHelper
import com.zyf.wanapp.mvp.model.bean.HttpResult
import com.zyf.wanapp.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by zyf on 2018/9/17.
 */
class AddTodoModel {

    fun addTodo(map: MutableMap<String, Any>): Observable<HttpResult<Any>> =
            RetrofitHelper.service.addTodo(map).compose(SchedulerUtils.ioToMain())

    fun updateTodo(id: Int, map: MutableMap<String, Any>): Observable<HttpResult<Any>> =
            RetrofitHelper.service.updateTodo(id, map).compose(SchedulerUtils.ioToMain())
}