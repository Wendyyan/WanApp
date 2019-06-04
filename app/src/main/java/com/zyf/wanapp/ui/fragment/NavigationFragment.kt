package com.zyf.wanapp.ui.fragment

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zyf.wanapp.R
import com.zyf.wanapp.adapter.NavigationAdapter
import com.zyf.wanapp.adapter.NavigationTabAdapter
import com.zyf.wanapp.base.BaseFragment
import com.zyf.wanapp.mvp.contract.NavigationContract
import com.zyf.wanapp.mvp.model.bean.NavigationBean
import com.zyf.wanapp.mvp.presenter.NavigationPresenter
import kotlinx.android.synthetic.main.fragment_navigation.*
import org.jetbrains.anko.toast
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.widget.TabView

class NavigationFragment : BaseFragment(), NavigationContract.View {

    companion object {
        fun getInstance(): NavigationFragment = NavigationFragment()
    }

    private var currentIndex: Int = 0

    private var isClickTab: Boolean = false
    private var isScrollTop: Boolean = false //点击tab让recyclerView对应内容滑动到顶部
    private var isLinkTab: Boolean = false //滑动recyclerView联动对应的tab

    private val navigationPresenter: NavigationPresenter by lazy {
        NavigationPresenter()
    }

    private val navigationList = mutableListOf<NavigationBean>()

    private val navigationAdapter: NavigationAdapter by lazy {
        NavigationAdapter(navigationList)
    }

    private val linearLayoutManager by lazy {
        androidx.recyclerview.widget.LinearLayoutManager(activity)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_navigation

    override fun initView() {
        navigationPresenter.attachView(this)

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = navigationAdapter
            itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
            setHasFixedSize(true)

            //scroll recyclerView link to tab
            addOnScrollListener(object: androidx.recyclerview.widget.RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (isScrollTop) scrollRecyclerView()
                }

                override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE) {
                        if (isScrollTop) scrollRecyclerView()
                        if (isClickTab){
                            isClickTab = false
                            return
                        }
                        val firstPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                        val firstPos = if (firstPosition > -1) firstPosition else linearLayoutManager.findFirstVisibleItemPosition()
                        if (firstPos != currentIndex) {
                            currentIndex = firstPos
                            navTabLayout.setTabSelected(currentIndex)
                            isLinkTab = true
                        }
                    }
                }
            })
        }

        navigationAdapter.run {
            bindToRecyclerView(recyclerView)
        }

        //select tab link to recyclerView
        navTabLayout.addOnTabSelectedListener(object: VerticalTabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabView?, position: Int) {
            }

            override fun onTabSelected(tab: TabView?, position: Int) {
                if (isLinkTab) {
                    isLinkTab = false
                } else {
                    isClickTab = true
                    currentIndex = position
                    recyclerView.stopScroll()
                    smoothScrollToPosition(position)
                }
            }
        })
    }

    //点击tab让recyclerView滑动到对应位置
    private fun smoothScrollToPosition(position: Int){
        val firstPosition = linearLayoutManager.findFirstVisibleItemPosition()
        val lastPosition = linearLayoutManager.findLastVisibleItemPosition()
        when{
            position <= firstPosition -> recyclerView.smoothScrollToPosition(position)
            position <= lastPosition -> {
                val top = recyclerView.getChildAt(position - firstPosition).top
                recyclerView.smoothScrollBy(0, top)
            }
            else -> {
                recyclerView.smoothScrollToPosition(position)
                isScrollTop = true
            }
        }
    }

    //recyclerView对应内容上滑到屏幕顶部
    private fun scrollRecyclerView(){
        isScrollTop = false
        val indexDistance: Int = currentIndex - linearLayoutManager.findFirstVisibleItemPosition()
        if (indexDistance > 0 && indexDistance < recyclerView!!.childCount) {
            val top: Int = recyclerView.getChildAt(indexDistance).top
            recyclerView.smoothScrollBy(0, top)
        }
    }

    override fun lazyLoad() {
        navigationPresenter.requestNavigationList()
    }

    override fun setNavigationList(list: List<NavigationBean>) {
        list.let {
            navTabLayout.setTabAdapter(NavigationTabAdapter(activity, list))
            navigationAdapter.run {
                replaceData(it)
                loadMoreComplete()
                loadMoreEnd()
                setEnableLoadMore(true)
            }
        }
    }

    override fun scrollToTop() {
        navTabLayout.setTabSelected(0)
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showError(errorMsg: String) {
        activity?.toast(errorMsg)
    }
}
