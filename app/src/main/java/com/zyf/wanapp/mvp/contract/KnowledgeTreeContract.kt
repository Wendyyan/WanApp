package com.zyf.wanapp.mvp.contract

import com.zyf.wanapp.base.IPresenter
import com.zyf.wanapp.base.IView
import com.zyf.wanapp.mvp.model.bean.KnowledgeTreeBody

/**
 * Created by zyf on 2018/9/3.
 */
interface KnowledgeTreeContract {

    interface View : IView {

        fun scrollToTop()

        fun setKnowledgeTree(list: List<KnowledgeTreeBody>)
    }

    interface Presenter: IPresenter<View> {

        fun requestKnowledgeTree()

    }
}