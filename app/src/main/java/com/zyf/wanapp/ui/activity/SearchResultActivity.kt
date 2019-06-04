package com.zyf.wanapp.ui.activity

import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zyf.wanapp.R
import com.zyf.wanapp.adapter.KnowledgeAdapter
import com.zyf.wanapp.base.BaseActivity
import com.zyf.wanapp.constant.Constant
import com.zyf.wanapp.mvp.contract.SearchResultContract
import com.zyf.wanapp.mvp.model.bean.ArticleBean
import com.zyf.wanapp.mvp.model.bean.ArticleResponseBody
import com.zyf.wanapp.mvp.presenter.SearchResultPresenter
import com.zyf.wanapp.util.DelegateExt
import com.zyf.wanapp.widge.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class SearchResultActivity : BaseActivity(), SearchResultContract.View {

    private var isRefresh = true

    private var key: String = ""

    private val searchResultList = mutableListOf<ArticleBean>()

    private val searchAdapter: KnowledgeAdapter by lazy {
        KnowledgeAdapter(this, searchResultList)
    }

    private val linearLayoutManager by lazy {
        androidx.recyclerview.widget.LinearLayoutManager(this)
    }

    private val spaceItemDecoration by lazy {
        SpaceItemDecoration(this)
    }

    private val searchResultPresenter by lazy {
        SearchResultPresenter()
    }

    override fun attachLayoutRes(): Int = R.layout.activity_search_result

    override fun initData() {
        key = intent.extras?.getString(Constant.SEARCH_KEY) ?: ""
        searchResultPresenter.requestSearchResult(0, key)
    }

    override fun initView() {
        searchResultPresenter.attachView(this)
        initToolbar(toolbar, true, key)

        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
            setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary))
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = searchAdapter
            itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
            addItemDecoration(spaceItemDecoration)
        }

        searchAdapter.run {
            bindToRecyclerView(recyclerView)
            setEmptyView(R.layout.layout_empty, recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@SearchResultActivity.onItemClickListener
            onItemChildClickListener = this@SearchResultActivity.onItemChildClickListener
        }
    }

    override fun setSearchResults(articles: ArticleResponseBody) {
        articles.datas.let {
            searchAdapter.run {
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

    override fun showCollectSuccess(success: Boolean) {
        if (success) toast(getString(R.string.msg_collect_success))
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success) toast(getString(R.string.msg_cancel_collect_success))
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = isRefresh
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
        if (isRefresh) {
            searchAdapter.setEnableLoadMore(true)
        }
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        searchAdapter.run {
            if (isRefresh) setEnableLoadMore(true) else loadMoreFail()
        }
    }

    /**
     * 刷新数据
     */
    private val onRefreshListener = androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        searchAdapter.setEnableLoadMore(false)
        searchResultPresenter.requestSearchResult(0, key)
    }

    /**
     * 加载更多
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = searchAdapter.data.size / 20 + 1
        searchResultPresenter.requestSearchResult(page, key)
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (searchResultList.size > 0) {
            val data = searchResultList[position]
            startActivity<ContentActivity>(
                    Constant.CONTENT_URL_KEY to data.link,
                    Constant.CONTENT_TITLE_KEY to data.title,
                    Constant.CONTENT_ID_KEY to data.id)
        }
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        if (searchResultList.size > 0) {
            val data = searchResultList[position]
            when (view.id) {
                R.id.iv_like -> {
                    if (DelegateExt.isLogin) {
                        val collect = data.collect
                        data.collect = !collect
                        searchAdapter.setData(position, data)
                        if (collect) {
                            searchResultPresenter.cancelCollectArticle(data.id)
                        } else {
                            searchResultPresenter.addCollectArticle(data.id)
                        }
                    } else {
                        toast(getString(R.string.msg_login_tint))
                        startActivity<LoginActivity>()
                    }
                }
            }
        }
    }
}
