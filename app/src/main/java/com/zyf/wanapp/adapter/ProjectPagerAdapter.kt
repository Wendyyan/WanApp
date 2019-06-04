package com.zyf.wanapp.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.zyf.wanapp.mvp.model.bean.ProjectTreeBean
import com.zyf.wanapp.ui.fragment.ProjectListFragment

/**
 * Created by zyf on 2018/9/4.
 */
class ProjectPagerAdapter(val list: MutableList<ProjectTreeBean>, fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = mutableListOf<androidx.fragment.app.Fragment>()

    init {
        fragmentList.clear()
        list.forEach {
            fragmentList.add(ProjectListFragment.getInstance(it.id))
        }
    }

    override fun getItem(pos: Int): androidx.fragment.app.Fragment = fragmentList[pos]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? = list[position].name

    override fun getItemPosition(`object`: Any): Int = androidx.viewpager.widget.PagerAdapter.POSITION_NONE
}