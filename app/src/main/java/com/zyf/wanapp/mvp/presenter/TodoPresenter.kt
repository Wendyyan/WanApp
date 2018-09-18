package com.zyf.wanapp.mvp.presenter

import com.zyf.wanapp.base.BasePresenter
import com.zyf.wanapp.http.RetryWithDelay
import com.zyf.wanapp.http.exception.ExceptionHandle
import com.zyf.wanapp.mvp.contract.TodoContract
import com.zyf.wanapp.mvp.model.TodoModel

/**
 * Created by zyf on 2018/9/17.
 */
class TodoPresenter : BasePresenter<TodoContract.View>(), TodoContract.Presenter {

    private val todoModel: TodoModel by lazy {
        TodoModel()
    }

    override fun getTodoList(type: Int) {
        mRootView?.showLoading()
        addSubscription(todoModel.getTodoList(type)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            showAllTodoList(it.data)
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

    override fun getNoTodoList(page: Int, type: Int) {
        if (page == 1) mRootView?.showLoading()
        addSubscription(todoModel.getNoTodoList(page, type)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            showTodoList(it.data)
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

    override fun getDoneList(page: Int, type: Int) {
        if (page == 1) mRootView?.showLoading()
        addSubscription(todoModel.getDoneList(page, type)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            showTodoList(it.data)
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

    override fun deleteTodoById(id: Int) {
        mRootView?.showLoading()
        addSubscription(todoModel.deleteTodoById(id)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            showDeleteSuccess(true)
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

    override fun updateTodoStatus(id: Int, status: Int) {
        mRootView?.showLoading()
        addSubscription(todoModel.updateTodoStatus(id, status)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    mRootView?.run {
                        if (it.errorCode != 0) {
                            showError(it.errorMsg)
                        } else {
                            showUpdateSuccess(true, status)
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