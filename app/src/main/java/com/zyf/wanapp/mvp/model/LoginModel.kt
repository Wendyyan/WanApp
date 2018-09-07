package com.zyf.wanapp.mvp.model

import com.zyf.wanapp.http.RetrofitHelper
import com.zyf.wanapp.mvp.model.bean.HttpResult
import com.zyf.wanapp.mvp.model.bean.LoginBean
import com.zyf.wanapp.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by zyf on 2018/9/1.
 */
class LoginModel {

    fun login(username: String, pwd: String): Observable<HttpResult<LoginBean>> = RetrofitHelper
            .service.login(username, pwd).compose(SchedulerUtils.ioToMain())

}