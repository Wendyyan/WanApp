package com.zyf.wanapp.mvp.presenter

import com.zyf.wanapp.base.BasePresenter
import com.zyf.wanapp.http.RetryWithDelay
import com.zyf.wanapp.http.exception.ExceptionHandle
import com.zyf.wanapp.mvp.contract.AddTodoContract
import com.zyf.wanapp.mvp.model.AddTodoModel

/**
 * Created by zyf on 2018/9/17.
 */
class AddTodoPresenter : BasePresenter<AddTodoContract.View>(), AddTodoContract.Presenter {

    private val addTodoModel: AddTodoModel by lazy {
        AddTodoModel()
    }

    override fun addTodo(map: MutableMap<String, Any>) {
        mRootView?.showLoading()
        addSubscription(addTodoModel.addTodo(map)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            addTodoSuccess(true)
                        }
                        hideLoading()
                    }
                }, {
                    mRootView?.run {
                        hideLoading()
                        showError(ExceptionHandle.handleException(it))
                    }
                }))
    }

    override fun updateTodo(id: Int, map: MutableMap<String, Any>) {
        mRootView?.showLoading()
        addSubscription(addTodoModel.updateTodo(id, map)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0){
                            showError(it.errorMsg)
                        } else {
                            updateTodoSuccess(true)
                        }
                        hideLoading()
                    }
                }, {
                    mRootView?.run {
                        hideLoading()
                        showError(ExceptionHandle.handleException(it))
                    }
                }))
    }
}