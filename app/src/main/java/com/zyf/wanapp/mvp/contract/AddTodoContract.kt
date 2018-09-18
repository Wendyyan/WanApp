package com.zyf.wanapp.mvp.contract

import com.zyf.wanapp.base.IPresenter
import com.zyf.wanapp.base.IView

/**
 * Created by zyf on 2018/9/17.
 */
interface AddTodoContract {

    interface View : IView {

        fun addTodoSuccess(success: Boolean)

        fun updateTodoSuccess(success: Boolean)
    }

    interface Presenter : IPresenter<View> {

        fun addTodo(map: MutableMap<String, Any>)

        fun updateTodo(id: Int, map: MutableMap<String, Any>)
    }
}