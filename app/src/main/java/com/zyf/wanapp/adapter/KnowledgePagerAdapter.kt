package com.zyf.wanapp.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.zyf.wanapp.mvp.model.bean.KnowledgeBean
import com.zyf.wanapp.ui.fragment.KnowledgeFragment

/**
 * Created by zyf on 2018/9/3.
 */
class KnowledgePagerAdapter(val list: List<KnowledgeBean>, fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = mutableListOf<androidx.fragment.app.Fragment>()

    init {
        list.forEach {
            fragmentList.add(KnowledgeFragment.getInstance(it.id))
        }
    }

    override fun getItem(pos: Int): androidx.fragment.app.Fragment = fragmentList[pos]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? = list[position].name

    override fun getItemPosition(`object`: Any): Int = androidx.viewpager.widget.PagerAdapter.POSITION_NONE
}