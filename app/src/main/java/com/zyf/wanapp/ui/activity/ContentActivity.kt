package com.zyf.wanapp.ui.activity

import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.zyf.wanapp.R
import com.zyf.wanapp.base.BaseActivity
import com.zyf.wanapp.constant.Constant
import com.zyf.wanapp.event.RefreshHomeEvent
import com.zyf.wanapp.mvp.contract.ContentContract
import com.zyf.wanapp.mvp.presenter.ContentPresenter
import com.zyf.wanapp.util.DelegateExt
import com.zyf.wanapp.util.getAgentWeb
import kotlinx.android.synthetic.main.container.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.browse
import org.jetbrains.anko.share
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class ContentActivity : BaseActivity(), ContentContract.View {

    private var agentWeb: AgentWeb? = null
    private var shareId: Int = 0
    private lateinit var shareTitle: String
    private lateinit var shareUrl: String

    private val contentPresenter: ContentPresenter by lazy {
        ContentPresenter()
    }

    override fun attachLayoutRes(): Int = R.layout.activity_content

    override fun initData() {
    }

    override fun initView() {
        contentPresenter.attachView(this)
        initToolbar(toolbar, true, getString(R.string.info_is_loading))

        intent.extras.let {
            shareId = it.getInt(Constant.CONTENT_ID_KEY, -1)
            shareTitle = it.getString(Constant.CONTENT_TITLE_KEY, "")
            shareUrl = it.getString(Constant.CONTENT_URL_KEY, "")
        }

        agentWeb = shareUrl.getAgentWeb(this, container,
                LinearLayout.LayoutParams(-1, -1),
                webChromeClient, webViewClient)
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success) {
            toast(getString(R.string.msg_collect_success))
            EventBus.getDefault().post(RefreshHomeEvent(true))
        }
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success) {
            toast(getString(R.string.msg_cancel_collect_success))
            EventBus.getDefault().post(RefreshHomeEvent(true))
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_content, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_share -> {
                share(getString(R.string.info_share_article_url,
                        getString(R.string.app_name), shareTitle, shareUrl))
                return true
            }
            R.id.action_like -> {
                if (DelegateExt.isLogin) {
                    contentPresenter.addCollectArticle(shareId)
                } else {
                    startActivity<LoginActivity>()
                    toast(getString(R.string.msg_login_tint))
                }
                return true
            }
            R.id.action_browser -> {
                browse(shareUrl)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (agentWeb?.handleKeyEvent(keyCode, event)!!) {
            true
        } else {
            finish()
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onResume() {
        agentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onPause() {
        agentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        agentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }

    private val webViewClient = object : WebViewClient(){

    }

    private val webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            toolbar.title = title
        }
    }
}
