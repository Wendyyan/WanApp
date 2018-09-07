package com.zyf.wanapp.mvp.contract

/**
 * Created by zyf on 2018/8/31.
 */
interface ContentContract {

    interface View : CommonContract.View

    interface Presenter : CommonContract.Presenter<View>
}