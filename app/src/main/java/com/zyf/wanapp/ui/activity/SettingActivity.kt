package com.zyf.wanapp.ui.activity

import com.zyf.wanapp.R
import com.zyf.wanapp.base.BaseActivity
import com.zyf.wanapp.ui.fragment.SettingFragment
import kotlinx.android.synthetic.main.toolbar.*

class SettingActivity : BaseActivity() {

    override fun attachLayoutRes(): Int = R.layout.activity_setting

    override fun initData() {

    }

    override fun initView() {
        initToolbar(toolbar, true, getString(R.string.nav_setting))
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, SettingFragment.getInstance()).commit()
    }
}
