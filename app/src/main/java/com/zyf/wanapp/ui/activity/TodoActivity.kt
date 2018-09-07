package com.zyf.wanapp.ui.activity

import android.os.Bundle
import com.zyf.wanapp.R
import com.zyf.wanapp.base.BaseActivity
import kotlinx.android.synthetic.main.toolbar.*

class TodoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
    }

    override fun attachLayoutRes(): Int = R.layout.activity_todo

    override fun initData() {

    }

    override fun initView() {
        initToolbar(toolbar, true, getString(R.string.nav_todo))
    }
}
