package com.zyf.wanapp.mvp.contract

import com.zyf.wanapp.base.IPresenter
import com.zyf.wanapp.base.IView
import com.zyf.wanapp.mvp.model.bean.LoginBean

/**
 * Created by zyf on 2018/9/1.
 */
interface LoginContract {

    interface View : IView {

        fun loginSuccess(data: LoginBean)

    }

    interface Presenter : IPresenter<View> {

        fun login(username: String, password: String)
    }
}