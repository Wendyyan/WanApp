package com.zyf.wanapp.mvp.contract

import com.zyf.wanapp.base.IPresenter
import com.zyf.wanapp.base.IView
import com.zyf.wanapp.mvp.model.bean.AllTodoResponseBody
import com.zyf.wanapp.mvp.model.bean.TodoResponseBody

/**
 * Created by zyf on 2018/9/17.
 */
interface TodoContract {

    interface View : IView {

        fun showAllTodoList(allTodoResponseBody: AllTodoResponseBody)

        fun showTodoList(todoResponseBody: TodoResponseBody)

        fun showDeleteSuccess(success: Boolean)

        fun showUpdateSuccess(success: Boolean, status: Int)
    }

    interface Presenter : IPresenter<View>{

        fun getTodoList(type: Int)

        fun getNoTodoList(page: Int, type: Int)

        fun getDoneList(page: Int, type: Int)

        fun deleteTodoById(id: Int)

        fun updateTodoStatus(id: Int, status: Int)
    }
}