package com.zyf.wanapp.mvp.model

import com.zyf.wanapp.http.RetrofitHelper
import com.zyf.wanapp.mvp.model.bean.AllTodoResponseBody
import com.zyf.wanapp.mvp.model.bean.HttpResult
import com.zyf.wanapp.mvp.model.bean.TodoResponseBody
import com.zyf.wanapp.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by zyf on 2018/9/17.
 */
class TodoModel {

    fun getTodoList(type: Int): Observable<HttpResult<AllTodoResponseBody>> {
        return RetrofitHelper.service.getTodoList(type)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getNoTodoList(page: Int, type: Int): Observable<HttpResult<TodoResponseBody>> {
        return RetrofitHelper.service.getNoTodoList(page, type)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getDoneList(page: Int, type: Int): Observable<HttpResult<TodoResponseBody>> {
        return RetrofitHelper.service.getDoneList(page, type)
                .compose(SchedulerUtils.ioToMain())
    }

    fun deleteTodoById(id: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.deleteTodoById(id)
                .compose(SchedulerUtils.ioToMain())
    }

    fun updateTodoStatus(id: Int, status: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.updateTodoStatus(id, status)
                .compose(SchedulerUtils.ioToMain())
    }
}
