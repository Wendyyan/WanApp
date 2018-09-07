package com.zyf.wanapp.mvp.contract

import com.zyf.wanapp.base.IPresenter
import com.zyf.wanapp.base.IView
import com.zyf.wanapp.mvp.model.bean.ProjectTreeBean

/**
 * Created by zyf on 2018/9/4.
 */
interface ProjectContract {

    interface View: IView{

        fun scrollToTop()

        fun setProjectTree(list: List<ProjectTreeBean>)
    }

    interface Presenter: IPresenter<View>{

        fun requestProjectTree()
    }
}