package com.zyf.wanapp.ui.activity

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zyf.wanapp.R
import com.zyf.wanapp.adapter.CollectListAdapter
import com.zyf.wanapp.base.BaseActivity
import com.zyf.wanapp.constant.Constant
import com.zyf.wanapp.mvp.contract.CollectContract
import com.zyf.wanapp.mvp.model.bean.CollectionArticle
import com.zyf.wanapp.mvp.model.bean.CollectionResponseBody
import com.zyf.wanapp.mvp.presenter.CollectPresenter
import com.zyf.wanapp.widge.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class CollectActivity : BaseActivity(), CollectContract.View {

    private val collectPresenter: CollectPresenter by lazy {
        CollectPresenter()
    }

    private var isRefresh = true

    private val articleList = mutableListOf<CollectionArticle>()

    private val collectAdapter: CollectListAdapter by lazy {
        CollectListAdapter(this, articleList)
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val recyclerViewItemDecoration by lazy {
        SpaceItemDecoration(this)
    }

    override fun attachLayoutRes(): Int = R.layout.activity_collect

    override fun initData() {
        collectPresenter.requestCollectList(0)
    }

    override fun initView() {
        collectPresenter.attachView(this)
        initToolbar(toolbar, true, getString(R.string.nav_my_collect))
        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
            setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary))
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = collectAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
        }

        collectAdapter.run {
            bindToRecyclerView(recyclerView)
            setEmptyView(R.layout.layout_empty, recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@CollectActivity.onItemClickListener
            onItemChildClickListener = this@CollectActivity.onItemChildClickListener
        }
    }

    override fun setCollectList(articles: CollectionResponseBody<CollectionArticle>) {
        articles.datas.let {
            collectAdapter.run {
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

    override fun removeCollectSuccess(success: Boolean) {
        if (success) {
            toast(getString(R.string.msg_cancel_collect_success))
        }
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = isRefresh
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
        if (isRefresh) {
            collectAdapter.setEnableLoadMore(true)
        }
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        collectAdapter.run {
            if (isRefresh) setEnableLoadMore(true) else loadMoreFail()
        }
    }

    /**
     * 刷新数据
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        collectAdapter.setEnableLoadMore(false)
        collectPresenter.requestCollectList(0)
    }

    /**
     * 加载更多
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = collectAdapter.data.size / 20
        collectPresenter.requestCollectList(page)
    }

    /**
     * 点击文章item
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (articleList.size != 0){
            val data = articleList[position]
            startActivity<ContentActivity>(
                    Constant.CONTENT_URL_KEY to data.link,
                    Constant.CONTENT_TITLE_KEY to data.title,
                    Constant.CONTENT_ID_KEY to data.id)
        }
    }

    /**
     * 收藏&取消收藏文章
     */
    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        if (articleList.size != 0){
            val data = articleList[position]
            when(view.id){
                R.id.iv_like ->{
                    collectAdapter.remove(position)
                    collectPresenter.removeCollectArticle(data.id, data.originId)
                }
            }
        }
    }
}
