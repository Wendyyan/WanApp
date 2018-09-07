package com.zyf.wanapp.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.zyf.wanapp.mvp.model.bean.ProjectTreeBean
import com.zyf.wanapp.ui.fragment.ProjectListFragment

/**
 * Created by zyf on 2018/9/4.
 */
class ProjectPagerAdapter(val list: MutableList<ProjectTreeBean>, fm: FragmentManager?) :
        FragmentStatePagerAdapter(fm) {

    private val fragmentList = mutableListOf<Fragment>()

    init {
        fragmentList.clear()
        list.forEach {
            fragmentList.add(ProjectListFragment.getInstance(it.id))
        }
    }

    override fun getItem(pos: Int): Fragment = fragmentList[pos]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? = list[position].name

    override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE
}