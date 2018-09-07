package com.zyf.wanapp.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.MenuItem
import com.zyf.wanapp.App
import com.zyf.wanapp.constant.Constant
import com.zyf.wanapp.util.DelegateExt
import com.zyf.wanapp.util.Preference
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.toast

/**
 * Created by zyf on 2018/8/29.
 */
abstract class BaseActivity : AppCompatActivity() {

    open fun useEventBus(): Boolean = true

    protected abstract fun attachLayoutRes(): Int

    abstract fun initData()

    abstract fun initView()

    @Subscribe
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(attachLayoutRes())
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
        initData()
        initView()
    }

    protected fun initToolbar(toolbar: Toolbar?, homeAsUpEnable: Boolean, title: String){
        toolbar?.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpEnable)
    }

    open fun showError(errorMsg: String){
        toast(errorMsg)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Fragment 逐个出栈
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        App.getRefWatcher(this)?.watch(this)
    }
}