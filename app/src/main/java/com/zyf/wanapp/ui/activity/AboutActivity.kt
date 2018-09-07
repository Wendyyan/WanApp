package com.zyf.wanapp.ui.activity

import android.os.Build
import android.text.Html
import android.text.method.LinkMovementMethod
import com.zyf.wanapp.R
import com.zyf.wanapp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.toolbar.*

class AboutActivity : BaseActivity() {

    override fun attachLayoutRes(): Int = R.layout.activity_about

    override fun initData() {
    }

    override fun initView() {
        initToolbar(toolbar, true, getString(R.string.nav_about_us))
        tvContent.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                text = Html.fromHtml(getString(R.string.about_content), Html.FROM_HTML_MODE_LEGACY)
            } else {
                text = Html.fromHtml(getString(R.string.about_content))
            }
            movementMethod = LinkMovementMethod.getInstance()
        }
    }
}
