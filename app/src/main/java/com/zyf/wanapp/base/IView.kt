package com.zyf.wanapp.base

/**
 * Created by zyf on 2018/8/30.
 */
interface IView {

    fun showLoading()

    fun hideLoading()

    fun showError(errorMsg: String)

}