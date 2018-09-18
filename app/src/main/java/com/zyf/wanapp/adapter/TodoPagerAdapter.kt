package com.zyf.wanapp.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import com.zyf.wanapp.mvp.model.bean.TodoTypeBean
import com.zyf.wanapp.ui.fragment.TodoFragment

/**
 * Created by zyf on 2018/9/4.
 */
class TodoPagerAdapter(val list: List<TodoTypeBean>, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragmentList = mutableListOf<Fragment>()

    init {
        list.forEach {
            fragmentList.add(TodoFragment.getInstance(it.type))
        }
    }

    override fun getItem(pos: Int): Fragment = fragmentList[pos]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? = list[position].name

    override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE
}