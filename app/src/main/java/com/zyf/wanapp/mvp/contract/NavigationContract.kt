package com.zyf.wanapp.mvp.contract

import com.zyf.wanapp.base.IPresenter
import com.zyf.wanapp.base.IView
import com.zyf.wanapp.mvp.model.bean.NavigationBean

/**
 * Created by zyf on 2018/9/3.
 */
interface NavigationContract {

    interface View: IView{

        fun scrollToTop()

        fun setNavigationList(list: List<NavigationBean>)
    }

    interface Presenter: IPresenter<View>{

        fun requestNavigationList()
    }
}