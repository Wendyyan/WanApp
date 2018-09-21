package com.zyf.wanapp.ui.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zyf.wanapp.R
import com.zyf.wanapp.adapter.ProjectListAdapter
import com.zyf.wanapp.base.BaseFragment
import com.zyf.wanapp.constant.Constant
import com.zyf.wanapp.mvp.contract.ProjectListContract
import com.zyf.wanapp.mvp.model.bean.ArticleBean
import com.zyf.wanapp.mvp.model.bean.ArticleResponseBody
import com.zyf.wanapp.mvp.presenter.ProjectListPresenter
import com.zyf.wanapp.ui.activity.ContentActivity
import com.zyf.wanapp.ui.activity.LoginActivity
import com.zyf.wanapp.util.DelegateExt
import com.zyf.wanapp.widge.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * Created by zyf on 2018/9/4.
 */
class ProjectListFragment: BaseFragment(), ProjectListContract.View {

    companion object {
        fun getInstance(cid: Int): ProjectListFragment {
            val fragment = ProjectListFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY, cid)
            fragment.arguments = args
            return fragment
        }
    }

    private val projectListPresenter: ProjectListPresenter by lazy {
        ProjectListPresenter()
    }

    private var cid: Int = 0

    private val articleList = mutableListOf<ArticleBean>()

    private val projectListAdapter: ProjectListAdapter by lazy {
        ProjectListAdapter(activity, articleList)
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    private val recyclerViewItemDecoration by lazy {
        SpaceItemDecoration(activity!!)
    }

    private var isRefresh = true //是否刷新

    override fun attachLayoutRes(): Int = R.layout.fragment_home

    override fun initView() {
        projectListPresenter.attachView(this)
        cid = arguments?.getInt(Constant.CONTENT_CID_KEY) ?: 0
        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
            setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary))
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = projectListAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
        }

        projectListAdapter.run {
            bindToRecyclerView(recyclerView)
            setEmptyView(R.layout.layout_empty, recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@ProjectListFragment.onItemClickListener
            onItemChildClickListener = this@ProjectListFragment.onItemChildClickListener
        }
    }

    override fun lazyLoad() {
        projectListPresenter.requestProjectList(1, cid)
    }

    override fun setProjectList(articles: ArticleResponseBody) {
        articles.datas.let {
            projectListAdapter.run {
                if (isRefresh) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                if (it.size < articles.size) {
                    loadMoreEnd(isRefresh)
                } else {
                    loadMoreComplete()
                }
            }
        }
    }

    override fun scrollToTop() {
        recyclerView.run {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 20) {
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success) activity?.toast(getString(R.string.msg_collect_success))
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success) activity?.toast(getString(R.string.msg_cancel_collect_success))
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = isRefresh
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
        if (isRefresh) {
            projectListAdapter.setEnableLoadMore(true)
        }
    }

    override fun showError(errorMsg: String) {
        projectListAdapter.run {
            if (isRefresh) setEnableLoadMore(true) else loadMoreFail()
        }
        activity?.toast(errorMsg)
    }

    /**
     * 刷新数据
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        projectListAdapter.setEnableLoadMore(false)
        projectListPresenter.requestProjectList(1, cid)
    }

    /**
     * 加载更多
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = articleList.size / 15 + 1
        projectListPresenter.requestProjectList(page, cid)
    }

    /**
     * 点击item
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (articleList.size != 0) {
            val data = articleList[position]
            activity!!.startActivity<ContentActivity>(
                    Constant.CONTENT_URL_KEY to data.link,
                    Constant.CONTENT_TITLE_KEY to data.title,
                    Constant.CONTENT_ID_KEY to data.id)
        }
    }

    /**
     * 点击item下的控件
     */
    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        if (articleList.size != 0) {
            val data = articleList[position]
            when(view.id){
                R.id.iv_like -> {
                    if (DelegateExt.isLogin) {
                        val collect = data.collect
                        data.collect = !collect
                        projectListAdapter.setData(position, data)
                        if (collect) {
                            projectListPresenter.cancelCollectArticle(data.id)
                        } else {
                            projectListPresenter.addCollectArticle(data.id)
                        }
                    } else {
                        activity!!.run {
                            startActivity<LoginActivity>()
                            toast(getString(R.string.msg_login_tint))
                        }
                    }
                }
            }
        }
    }
}