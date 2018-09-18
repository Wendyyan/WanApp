package com.zyf.wanapp.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatDelegate
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zyf.wanapp.R
import com.zyf.wanapp.base.BaseActivity
import com.zyf.wanapp.event.LoginEvent
import com.zyf.wanapp.event.RefreshHomeEvent
import com.zyf.wanapp.ui.fragment.HomeFragment
import com.zyf.wanapp.ui.fragment.KnowledgeTreeFragment
import com.zyf.wanapp.ui.fragment.NavigationFragment
import com.zyf.wanapp.ui.fragment.ProjectFragment
import com.zyf.wanapp.util.DelegateExt
import com.zyf.wanapp.util.Preference
import com.zyf.wanapp.util.disableShiftMode
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {

    companion object {
        private const val FRAGMENT_HOME = 0x01
        private const val FRAGMENT_KNOWLEDGE = 0x02
        private const val FRAGMENT_NAVIGATION = 0x03
        private const val FRAGMENT_PROJECT = 0x04
        private const val BOTTOM_INDEX = "bottom_index"
    }

    private var mIndex = FRAGMENT_HOME

    private var mHomeFragment: HomeFragment? = null
    private var mKnowledgeFragment: KnowledgeTreeFragment? = null
    private var mNavigationFragment: NavigationFragment? = null
    private var mProjectFragment: ProjectFragment? = null

    private var tvUsername: TextView? = null

    override fun useEventBus(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt(BOTTOM_INDEX)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.putInt(BOTTOM_INDEX, mIndex)
    }

    override fun attachLayoutRes(): Int = R.layout.activity_main

    override fun initData() {

    }

    override fun initView() {
        initToolbar(toolbar, false, getString(R.string.app_name))

        bottomNavigation.run {
            disableShiftMode()
            setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        }

        initDrawerLayout()

        navView.run {
            val img = "http://img.hb.aicdn.com/bf6d426c3b7d5695d2f944c16ff84884394af298345b-Gj0B0P_fw658"
            val ivHeadImg = getHeaderView(0).findViewById<ImageView>(R.id.iv_head_img)
            tvUsername = getHeaderView(0).findViewById(R.id.tv_username)
            Glide.with(this).load(img).apply(RequestOptions.circleCropTransform()).into(ivHeadImg)

            menu.findItem(R.id.nav_logout).isVisible = DelegateExt.isLogin
            menu.findItem(R.id.nav_night_mode).title = if (DelegateExt.isNightMode){
                getString(R.string.nav_day_mode)
            } else {
                getString(R.string.nav_night_mode)
            }

            setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)
        }

        tvUsername?.run {
            text = if (!DelegateExt.isLogin) getString(R.string.info_login) else DelegateExt.username
            setOnClickListener {
                if (!DelegateExt.isLogin) startActivity<LoginActivity>()
            }
        }

        showFragment(mIndex)
        floatingActionBtn.setOnClickListener(onFabClickListener)
    }

    /**
     * init DrawerLayout
     */
    private fun initDrawerLayout() {
        drawerLayout.run {
            val toggle = ActionBarDrawerToggle(this@MainActivity, this,
                    toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            addDrawerListener(toggle)
            toggle.syncState()
        }

    }

    private val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                return@OnNavigationItemSelectedListener when(item.itemId){
                    R.id.action_home -> {
                        showFragment(FRAGMENT_HOME)
                        true
                    }
                    R.id.action_knowledge_system -> {
                        showFragment(FRAGMENT_KNOWLEDGE)
                        true
                    }
                    R.id.action_navigation -> {
                        showFragment(FRAGMENT_NAVIGATION)
                        true
                    }
                    R.id.action_project -> {
                        showFragment(FRAGMENT_PROJECT)
                        true
                    }
                    else -> false
                }}

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_search -> {
                startActivity<SearchActivity>()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: LoginEvent){
        if (event.isLogin) {
            tvUsername?.text = DelegateExt.username
            navView.menu.findItem(R.id.nav_logout).isVisible = true
            mHomeFragment?.lazyLoad()
        } else {
            tvUsername?.text = getString(R.string.info_login)
            navView.menu.findItem(R.id.nav_logout).isVisible = false
            mHomeFragment?.lazyLoad()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshHomeEvent(event: RefreshHomeEvent){
        if (event.isRefresh) {
            mHomeFragment?.lazyLoad()
        }
    }

    private fun showFragment(index: Int){
        val transaction = supportFragmentManager.beginTransaction()
        hideFragment(transaction)
        mIndex = index
        when(index){
            FRAGMENT_HOME ->{
                toolbar.title = getString(R.string.app_name)
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.getInstance()
                    transaction.add(R.id.container, mHomeFragment!!, "home")
                } else {
                    transaction.show(mHomeFragment!!)
                }
            }
            FRAGMENT_KNOWLEDGE ->{
                toolbar.title = getString(R.string.info_knowledge_system)
                if (mKnowledgeFragment == null) {
                    mKnowledgeFragment = KnowledgeTreeFragment.getInstance()
                    transaction.add(R.id.container, mKnowledgeFragment!!, "knowledge")
                } else {
                    transaction.show(mKnowledgeFragment!!)
                }
            }
            FRAGMENT_NAVIGATION ->{
                toolbar.title = getString(R.string.info_navigation)
                if (mNavigationFragment == null) {
                    mNavigationFragment = NavigationFragment.getInstance()
                    transaction.add(R.id.container, mNavigationFragment!!, "knowledge")
                } else {
                    transaction.show(mNavigationFragment!!)
                }
            }
            FRAGMENT_PROJECT ->{
                toolbar.title = getString(R.string.info_project)
                if (mProjectFragment == null){
                    mProjectFragment = ProjectFragment.getInstance()
                    transaction.add(R.id.container, mProjectFragment!!, "project")
                } else {
                    transaction.show(mProjectFragment!!)
                }
            }
        }
        transaction.commit()
    }

    private fun hideFragment(transaction: FragmentTransaction){
        mHomeFragment?.let { transaction.hide(it) }
        mKnowledgeFragment?.let { transaction.hide(it) }
        mNavigationFragment?.let { transaction.hide(it) }
        mProjectFragment?.let { transaction.hide(it) }
    }

    private val onFabClickListener = View.OnClickListener {
        when(mIndex){
            FRAGMENT_HOME ->{
                mHomeFragment?.scrollToTop()
            }
            FRAGMENT_KNOWLEDGE ->{
                mKnowledgeFragment?.scrollToTop()
            }
            FRAGMENT_NAVIGATION ->{
                mNavigationFragment?.scrollToTop()
            }
            FRAGMENT_PROJECT ->{
                mProjectFragment?.scrollToTop()
            }
        }
        toolbar.collapseActionView()
    }

    private val onDrawerNavigationItemSelectedListener =
            NavigationView.OnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_collect -> {
                        if (DelegateExt.isLogin) {
                            startActivity<CollectActivity>()
                        } else {
                            toast(getString(R.string.msg_login_tint))
                            startActivity<LoginActivity>()
                        }
                    }
                    R.id.nav_todo -> {
                        if (DelegateExt.isLogin) {
                            startActivity<TodoActivity>()
                        } else {
                            toast(getString(R.string.msg_login_tint))
                            startActivity<LoginActivity>()
                        }
                    }
                    R.id.nav_night_mode -> {
                        DelegateExt.run {
                            if (isNightMode) {
                                isNightMode = false
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            } else {
                                isNightMode = true
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            }
                        }
                        recreate()
                    }
                    R.id.nav_setting -> startActivity<SettingActivity>()
                    R.id.nav_about_us -> startActivity<AboutActivity>()
                    R.id.nav_logout -> logout()
                }
                true
            }

    private fun logout(){
        Preference.clearPreference()
        toast(getString(R.string.msg_logout_success))
        DelegateExt.isLogin = false
        EventBus.getDefault().post(LoginEvent(false))
    }

    private var mExitTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(mExitTime) < 2000) {
                finish()
            } else {
                mExitTime = System.currentTimeMillis()
                toast(getString(R.string.msg_exit_tip))
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
