package com.zyf.wanapp

import android.app.Application
import android.content.Context
import android.support.v7.app.AppCompatDelegate
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import com.zyf.wanapp.util.DelegateExt
import org.litepal.LitePal

/**
 * Created by zyf on 2018/8/22.
 */
class App: Application() {

    private var refWatcher: RefWatcher? = null

    companion object {
        var instance: App by DelegateExt.notNullSingleValue()

        fun getRefWatcher(context: Context): RefWatcher?{
            val app = context.applicationContext as App
            return app.refWatcher
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        refWatcher = setupLeakCanary()
        LitePal.initialize(this)
        initTheme()
    }

    private fun setupLeakCanary(): RefWatcher{
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED
        }
        return LeakCanary.install(this)
    }

    private fun initTheme() {
        if (DelegateExt.isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}