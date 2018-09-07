package com.zyf.wanapp.base

/**
 * Created by zyf on 2018/8/30.
 */
interface IPresenter<in V: IView> {

    fun attachView(mRootView: V)

    fun detachView()
}