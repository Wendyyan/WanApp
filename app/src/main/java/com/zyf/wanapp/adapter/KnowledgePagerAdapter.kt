package com.zyf.wanapp.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.zyf.wanapp.mvp.model.bean.KnowledgeBean
import com.zyf.wanapp.ui.fragment.KnowledgeFragment

/**
 * Created by zyf on 2018/9/3.
 */
class KnowledgePagerAdapter(val list: List<KnowledgeBean>, fm: FragmentManager) :
        FragmentStatePagerAdapter(fm) {

    private val fragmentList = mutableListOf<Fragment>()

    init {
        list.forEach {
            fragmentList.add(KnowledgeFragment.getInstance(it.id))
        }
    }

    override fun getItem(pos: Int): Fragment = fragmentList[pos]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? = list[position].name

    override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE
}