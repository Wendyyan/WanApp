package com.zyf.wanapp.ui.activity

import com.google.android.material.tabs.TabLayout
import android.view.Menu
import android.view.MenuItem
import com.zyf.wanapp.R
import com.zyf.wanapp.adapter.TodoPagerAdapter
import com.zyf.wanapp.base.BaseActivity
import com.zyf.wanapp.constant.Constant
import com.zyf.wanapp.event.SwitchTodoEvent
import com.zyf.wanapp.mvp.model.bean.TodoTypeBean
import kotlinx.android.synthetic.main.activity_todo.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity

class TodoActivity : BaseActivity() {

    /**
     * 切换todo false->待办 true->已完成
     */
    private var isSwitchDone: Boolean = false

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
                    EventBus.getDefault().post(SwitchTodoEvent(isSwitchDone, viewPager.currentItem))
                }
            })
        }

        fabAdd.setOnClickListener {
            startActivity<AddTodoActivity>(
                    Constant.TODO_ACTION to Constant.Type.ADD_TODO,
                    Constant.TODO_TYPE to viewPager.currentItem)
        }
    }

    override fun initData() {
        data.add(TodoTypeBean(0, "只用这一个"))
        data.add(TodoTypeBean(1, "工作"))
        data.add(TodoTypeBean(2, "学习"))
        data.add(TodoTypeBean(3, "生活"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_switch, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_switch -> {
                isSwitchDone = !isSwitchDone
                toolbar.title = if (isSwitchDone) "TODO(已完成)" else "TODO(未完成)"
                EventBus.getDefault().post(SwitchTodoEvent(isSwitchDone, viewPager.currentItem))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
