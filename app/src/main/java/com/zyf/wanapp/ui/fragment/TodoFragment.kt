package com.zyf.wanapp.ui.fragment

import android.os.Bundle
import com.zyf.wanapp.R
import com.zyf.wanapp.base.BaseFragment
import com.zyf.wanapp.constant.Constant

class TodoFragment : BaseFragment() {

    companion object {
        fun getInstance(type: Int): TodoFragment {
            val fragment = TodoFragment();
            val args = Bundle();
            args.putInt(Constant.TODO_TYPE, type);
            fragment.arguments = args;
            return fragment
        }
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_todo

    override fun initView() {
    }

    override fun lazyLoad() {
    }
}
