package com.zyf.wanapp.mvp.contract

import com.zyf.wanapp.base.IPresenter
import com.zyf.wanapp.base.IView
import com.zyf.wanapp.mvp.model.bean.LoginBean

/**
 * Created by zyf on 2019/5/7.
 */
interface RegisterContract {

    interface View : IView {

        fun registerSuccess(data: LoginBean)

    }

    interface Presenter : IPresenter<View> {

        fun register(username: String, password: String, rePassword: String)
    }
}