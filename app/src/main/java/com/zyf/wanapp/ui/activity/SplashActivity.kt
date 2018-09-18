package com.zyf.wanapp.ui.activity

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.zyf.wanapp.R
import com.zyf.wanapp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity

class SplashActivity : BaseActivity() {

    private var alphaAnimation: AlphaAnimation? = null

    override fun attachLayoutRes(): Int = R.layout.activity_splash

    override fun initData() {
    }

    override fun initView() {
        alphaAnimation = AlphaAnimation(0.3f, 1.0f)
        alphaAnimation?.run {
            duration = 2000
            setAnimationListener(object: Animation.AnimationListener{
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    startActivity<MainActivity>()
                    finish()
                }

                override fun onAnimationStart(p0: Animation?) {
                }
            })
        }
        splashLayout.startAnimation(alphaAnimation)
    }


}
