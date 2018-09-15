package com.zyf.wanapp.ui.activity

import android.support.design.widget.TabLayout
import com.zyf.wanapp.R
import com.zyf.wanapp.adapter.TodoPagerAdapter
import com.zyf.wanapp.base.BaseActivity
import com.zyf.wanapp.mvp.model.bean.TodoTypeBean
import kotlinx.android.synthetic.main.activity_todo.*
import kotlinx.android.synthetic.main.toolbar.*

class TodoActivity : BaseActivity() {

    private val data = mutableListOf<TodoTypeBean>()

    private val viewPagerAdapter: TodoPagerAdapter by lazy {
        TodoPagerAdapter(data, supportFragmentManager)
    }

    override fun attachLayoutRes(): Int = R.layout.activity_todo

    override fun initView() {
        initToolbar(toolbar, true, getString(R.string.nav_todo))

        viewPager.run {
            adapter = viewPagerAdapter
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            offscreenPageLimit = data.size
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

    override fun initData() {
        data.add(TodoTypeBean(1, "只有这一个"))
        data.add(TodoTypeBean(2, "工作"))
        data.add(TodoTypeBean(3, "学习"))
        data.add(TodoTypeBean(4, "生活"))
    }
}
