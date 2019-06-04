package com.zyf.wanapp.ui.fragment

import com.google.android.material.tabs.TabLayout
import com.zyf.wanapp.R
import com.zyf.wanapp.adapter.ProjectPagerAdapter
import com.zyf.wanapp.base.BaseFragment
import com.zyf.wanapp.mvp.contract.ProjectContract
import com.zyf.wanapp.mvp.model.bean.ProjectTreeBean
import com.zyf.wanapp.mvp.presenter.ProjectTreePresenter
import kotlinx.android.synthetic.main.fragment_project.*
import org.jetbrains.anko.toast

class ProjectFragment : BaseFragment(), ProjectContract.View {

    companion object {
        fun getInstance(): ProjectFragment = ProjectFragment()
    }

    private val projectPresenter: ProjectTreePresenter by lazy {
        ProjectTreePresenter()
    }

    private val projectTreeList = mutableListOf<ProjectTreeBean>()

    private val viewPagerAdapter: ProjectPagerAdapter by lazy {
        ProjectPagerAdapter(projectTreeList, childFragmentManager)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_project

    override fun initView() {
        projectPresenter.attachView(this)

        viewPager.run {
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
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
                    viewPager.currentItem = tab?.position!!
                }
            })
        }
    }

    override fun lazyLoad() {
        projectPresenter.requestProjectTree()
    }

    override fun setProjectTree(list: List<ProjectTreeBean>) {
        list.let {
            projectTreeList.addAll(it)
            viewPager.run {
                adapter = viewPagerAdapter
                offscreenPageLimit = projectTreeList.size
            }
        }
    }

    override fun scrollToTop() {
        if(viewPagerAdapter.count == 0) return
        val fragment = viewPagerAdapter.getItem(viewPager.currentItem) as ProjectListFragment
        fragment.scrollToTop()
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showError(errorMsg: String) {
        activity?.toast(errorMsg)
    }
}
