package com.zyf.wanapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.zyf.wanapp.mvp.model.bean.TodoTypeBean
import com.zyf.wanapp.ui.fragment.TodoFragment

/**
 * Created by zyf on 2018/9/4.
 */
class TodoPagerAdapter(val list: List<TodoTypeBean>, fm: androidx.fragment.app.FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = mutableListOf<androidx.fragment.app.Fragment>()

    init {
        list.forEach {
            fragmentList.add(TodoFragment.getInstance(it.type))
        }
    }

    override fun getItem(pos: Int): androidx.fragment.app.Fragment = fragmentList[pos]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? = list[position].name

    override fun getItemPosition(`object`: Any): Int = androidx.viewpager.widget.PagerAdapter.POSITION_NONE
}