package com.zyf.wanapp.mvp.model

import com.zyf.wanapp.http.RetrofitHelper
import com.zyf.wanapp.mvp.model.bean.HttpResult
import com.zyf.wanapp.mvp.model.bean.LoginBean
import com.zyf.wanapp.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by zyf on 2019/5/7.
 */
class RegisterModel {

    fun register(username: String, pwd: String, rePwd: String): Observable<HttpResult<LoginBean>> =
            RetrofitHelper.service.register(username, pwd, rePwd).compose(SchedulerUtils.ioToMain())

}