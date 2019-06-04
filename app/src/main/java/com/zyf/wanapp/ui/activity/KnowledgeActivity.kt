package com.zyf.wanapp.ui.activity

import com.google.android.material.tabs.TabLayout
import android.view.Menu
import android.view.MenuItem
import com.zyf.wanapp.R
import com.zyf.wanapp.adapter.KnowledgePagerAdapter
import com.zyf.wanapp.base.BaseActivity
import com.zyf.wanapp.constant.Constant
import com.zyf.wanapp.mvp.model.bean.KnowledgeBean
import com.zyf.wanapp.mvp.model.bean.KnowledgeTreeBody
import kotlinx.android.synthetic.main.activity_knowledge.*
import org.jetbrains.anko.share

class KnowledgeActivity : BaseActivity() {

    private lateinit var toolbarTitle: String

    private var knowledgeList = mutableListOf<KnowledgeBean>()

    private val viewPagerAdapter by lazy {
        KnowledgePagerAdapter(knowledgeList, supportFragmentManager)
    }

    override fun attachLayoutRes(): Int = R.layout.activity_knowledge

    override fun initData() {
        intent.extras?.getParcelable<KnowledgeTreeBody>(Constant.CONTENT_DATA_KEY).let {
            toolbarTitle = it!!.name
            knowledgeList.addAll(it.children)
        }
    }

    override fun initView() {
        initToolbar(toolbar, true, toolbarTitle)
        viewPager.run {
            adapter = viewPagerAdapter
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            offscreenPageLimit = knowledgeList.size
        }

        tabLayout.run {
            setupWithViewPager(viewPager)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
            addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    // 默认切换的时候，会有一个过渡动画，设为false后，取消动画，直接显示
//                    viewPager.setCurrentItem(tab?.position!!, false)
                    viewPager.currentItem = tab?.position!!
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_share -> {
                share(getString(
                        R.string.info_share_article_url,
                        getString(R.string.app_name),
                        knowledgeList[tabLayout.selectedTabPosition].name,
                        knowledgeList[tabLayout.selectedTabPosition].id.toString()
                ))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
