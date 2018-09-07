package com.zyf.wanapp.mvp.presenter

import com.zyf.wanapp.base.BasePresenter
import com.zyf.wanapp.http.RetryWithDelay
import com.zyf.wanapp.http.exception.ExceptionHandle
import com.zyf.wanapp.mvp.contract.LoginContract
import com.zyf.wanapp.mvp.model.LoginModel

/**
 * Created by zyf on 2018/9/1.
 */
class LoginPresenter : BasePresenter<LoginContract.View>(), LoginContract.Presenter {

    private val loginModel: LoginModel by lazy {
        LoginModel()
    }

    override fun login(username: String, password: String) {
        mRootView?.showLoading()
        addSubscription(loginModel.login(username, password)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            loginSuccess(it.data)
                        }
                        hideLoading()
                    }
                }, {
                    mRootView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(it))
                    }
                }))
    }
}