package com.zyf.wanapp.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zyf.wanapp.App
import com.zyf.wanapp.constant.Constant
import com.zyf.wanapp.util.DelegateExt
import com.zyf.wanapp.util.Preference
import org.jetbrains.anko.toast

/**
 * Created by zyf on 2018/8/30.
 */
abstract class BaseFragment : Fragment() {

    //用户是否登录
    protected var isLogin = DelegateExt.isLogin

    //视图是否加载完毕
    private var isViewPrepare = false

    //数据是否加载过
    private var hasLoadData = false

    abstract fun attachLayoutRes(): Int

    abstract fun initView()

    abstract fun lazyLoad()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(attachLayoutRes(), null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewPrepare = true
        initView()
        lazyLoadDataIfPrepared()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) lazyLoadDataIfPrepared()
    }

    private fun lazyLoadDataIfPrepared() {
        if (userVisibleHint && isViewPrepare && !hasLoadData) {
            lazyLoad()
            hasLoadData = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.let { App.getRefWatcher(it)?.watch(it) }
    }
}