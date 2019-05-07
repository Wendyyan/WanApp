package com.zyf.wanapp.mvp.presenter

import com.zyf.wanapp.base.BasePresenter
import com.zyf.wanapp.http.RetryWithDelay
import com.zyf.wanapp.http.exception.ExceptionHandle
import com.zyf.wanapp.mvp.contract.RegisterContract
import com.zyf.wanapp.mvp.model.RegisterModel

/**
 * Created by zyf on 2019/5/7.
 */
class RegisterPresenter : BasePresenter<RegisterContract.View>(), RegisterContract.Presenter {

    private val registerModel: RegisterModel by lazy {
        RegisterModel()
    }

    override fun register(username: String, password: String, rePassword: String) {
        mRootView?.showLoading()
        addSubscription(registerModel.register(username, password, rePassword)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            registerSuccess(it.data)
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