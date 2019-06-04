package com.zyf.wanapp.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.zyf.wanapp.App
import com.zyf.wanapp.R

@SuppressLint("RestrictedApi", "WrongConstant")
fun BottomNavigationView.disableShiftMode() {

    val menuView = getChildAt(0) as BottomNavigationMenuView
    try {
        menuView.labelVisibilityMode = 1

        for (i in 0 until menuView.childCount) {
            (menuView.getChildAt(i) as BottomNavigationItemView).apply {
                setShifting(false)
                setChecked(itemData.isChecked)
            }
        }
    } catch (e: NoSuchFieldException) {
        Log.e("BNVHelper", "Unable to get shift mode field", e)
    } catch (e: IllegalAccessException) {
        Log.e("BNVHelper", "Unable to change value of shift mode", e)
    }
}

/**
 * 加载图片
 * @param context
 * @param url
 */
fun ImageView.load(context: Context?, url: String?){
    // 1.开启无图模式 2.非WiFi环境 不加载图片
    val isLoadImage = !DelegateExt.switchNoPhotoMode || NetWorkUtil.isWifi(App.instance)
    val options = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA)
    if (isLoadImage) {
        Glide.with(context!!)
                .load(url)
                .transition(DrawableTransitionOptions().crossFade())
                .apply(options)
                .into(this)
    }
}

fun String.getAgentWeb(activity: Activity, webContent: ViewGroup,
                       layoutParams: ViewGroup.LayoutParams,
                       webChromeClient: WebChromeClient,
                       webViewClient: WebViewClient) =
        AgentWeb.with(activity)
                .setAgentWebParent(webContent, layoutParams)//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                .useDefaultIndicator() //使用默认进度条
                .setWebChromeClient(webChromeClient)
                .setWebViewClient(webViewClient)
                .setMainFrameErrorView(R.layout.layout_agent_web_error_page, -1)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
                .createAgentWeb()
                .ready()
                .go(this)!!