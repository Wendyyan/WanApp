package com.zyf.wanapp.ui.fragment

import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zyf.wanapp.R
import com.zyf.wanapp.adapter.KnowledgeTreeAdapter
import com.zyf.wanapp.base.BaseFragment
import com.zyf.wanapp.constant.Constant
import com.zyf.wanapp.mvp.contract.KnowledgeTreeContract
import com.zyf.wanapp.mvp.model.bean.KnowledgeTreeBody
import com.zyf.wanapp.mvp.presenter.KnowledgeTreePresenter
import com.zyf.wanapp.ui.activity.KnowledgeActivity
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * Created by zyf on 2018/9/1.
 */
class KnowledgeTreeFragment : BaseFragment(), KnowledgeTreeContract.View{

    companion object {
        fun getInstance(): KnowledgeTreeFragment = KnowledgeTreeFragment()
    }

    private val knowledgeTreePresenter: KnowledgeTreePresenter by lazy {
        KnowledgeTreePresenter()
    }

    private val knowledgeTreeList = mutableListOf<KnowledgeTreeBody>()

    private val knowledgeTreeAdapter: KnowledgeTreeAdapter by lazy {
        KnowledgeTreeAdapter(activity, knowledgeTreeList)
    }

    private val linearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager by lazy {
        androidx.recyclerview.widget.LinearLayoutManager(activity)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_home

    override fun initView() {
        knowledgeTreePresenter.attachView(this)
        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
            setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary))
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = knowledgeTreeAdapter
            itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        }

        knowledgeTreeAdapter.run {
            bindToRecyclerView(recyclerView)
            setEnableLoadMore(false)
            setEmptyView(R.layout.layout_empty, recyclerView)
            onItemClickListener = this@KnowledgeTreeFragment.onItemClickListener
        }
    }

    override fun lazyLoad() {
        knowledgeTreePresenter.requestKnowledgeTree()
    }

    override fun setKnowledgeTree(list: List<KnowledgeTreeBody>) {
        knowledgeTreeAdapter.replaceData(list)
    }


    override fun scrollToTop() {
        recyclerView.run {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 20){
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
        }
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
        knowledgeTreeAdapter.loadMoreComplete()
    }

    override fun showError(errorMsg: String) {
        knowledgeTreeAdapter.loadMoreFail()
        activity?.toast(errorMsg)
    }

    private val onRefreshListener = androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
        knowledgeTreePresenter.requestKnowledgeTree()
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (knowledgeTreeList.size != 0) {
            val data = knowledgeTreeList[position]
            activity?.startActivity<KnowledgeActivity>(Constant.CONTENT_DATA_KEY to data)
        }
    }
}