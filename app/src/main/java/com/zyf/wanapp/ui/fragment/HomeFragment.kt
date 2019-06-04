package com.zyf.wanapp.ui.fragment

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import cn.bingoogolapple.bgabanner.BGABanner
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zyf.wanapp.mvp.contract.HomeContract
import com.zyf.wanapp.R
import com.zyf.wanapp.adapter.HomeAdapter
import com.zyf.wanapp.base.BaseFragment
import com.zyf.wanapp.constant.Constant
import com.zyf.wanapp.mvp.model.bean.ArticleBean
import com.zyf.wanapp.mvp.model.bean.ArticleResponseBody
import com.zyf.wanapp.mvp.model.bean.BannerBean
import com.zyf.wanapp.mvp.presenter.HomePresenter
import com.zyf.wanapp.ui.activity.ContentActivity
import com.zyf.wanapp.ui.activity.LoginActivity
import com.zyf.wanapp.util.DelegateExt
import com.zyf.wanapp.widge.SpaceItemDecoration
import com.zyf.wanapp.util.load
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_banner.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class HomeFragment : BaseFragment(), HomeContract.View {

    companion object {
        fun getInstance(): HomeFragment = HomeFragment()
    }

    private val homePresenter: HomePresenter by lazy {
        HomePresenter()
    }

    private var isRefresh = true //是否刷新

    private lateinit var bannerList: ArrayList<BannerBean>

    private var bannerView: View? = null

    private val articleList = mutableListOf<ArticleBean>()

    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter(activity, articleList)
    }

    private val bannerAdapter: BGABanner.Adapter<ImageView, String> by lazy {
        BGABanner.Adapter<ImageView, String> { _, itemView, model, _ ->
            itemView.load(activity, model) }
    }

    private val linearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager by lazy {
        androidx.recyclerview.widget.LinearLayoutManager(activity)
    }

    private val recyclerViewItemDecoration by lazy {
        SpaceItemDecoration(activity!!)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_home

    @SuppressLint("InflateParams")
    override fun initView() {
        homePresenter.attachView(this)

        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
            setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary))
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = homeAdapter
            itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
        }

        bannerView = layoutInflater.inflate(R.layout.layout_banner, null)
        bannerView?.banner?.setDelegate(bannerDelegate)

        homeAdapter.run {
            bindToRecyclerView(recyclerView)
            setEmptyView(R.layout.layout_empty, recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@HomeFragment.onItemClickListener
            onItemChildClickListener = this@HomeFragment.onItemChildClickListener
            addHeaderView(bannerView)
        }
    }

    override fun lazyLoad() {
        homePresenter.requestBanner()
        homePresenter.requestArticle(0)
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

    @SuppressLint("CheckResult")
    override fun setBanners(banners: List<BannerBean>) {
        bannerList = banners as ArrayList<BannerBean>
        val bannerFeedList = ArrayList<String>()
        val bannerTitleList = ArrayList<String>()
        Observable.fromIterable(banners)
                .subscribe { banner ->
                    bannerFeedList.add(banner.imagePath)
                    bannerTitleList.add(banner.title)
                }
        bannerView?.banner?.run {
            setAutoPlayAble(true)
            setData(bannerFeedList, bannerTitleList)
            setAdapter(bannerAdapter)
        }
    }

    override fun setArticles(articles: ArticleResponseBody) {
        articles.datas.let {
            homeAdapter.run {
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
            homeAdapter.setEnableLoadMore(true)
        }
    }

    override fun showError(errorMsg: String) {
        homeAdapter.run {
            if (isRefresh) setEnableLoadMore(true) else loadMoreFail()
        }
        activity?.toast(errorMsg)
    }

    /**
     * 刷新数据
     */
    private val onRefreshListener = androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        homeAdapter.setEnableLoadMore(false)
        homePresenter.requestBanner()
        homePresenter.requestArticle(0)
    }

    /**
     * 加载更多
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = homeAdapter.data.size / 20
        homePresenter.requestArticle(page)
    }

    /**
     * 点击文章item
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (articleList.size != 0){
            val data = articleList[position]
            activity!!.startActivity<ContentActivity>(
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
                    if (DelegateExt.isLogin) {
                        val collect = data.collect
                        data.collect = !collect
                        homeAdapter.setData(position, data)
                        if (collect) {
                            homePresenter.cancelCollectArticle(data.id)
                        } else {
                            homePresenter.addCollectArticle(data.id)
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

    /**
     * 轮播图点击事件
     */
    private val bannerDelegate = BGABanner.Delegate<ImageView, String> { _, _, _, position ->
        if (bannerList.size != 0){
            val data = bannerList[position]
            activity!!.startActivity<ContentActivity>(
                    Constant.CONTENT_URL_KEY to data.url,
                    Constant.CONTENT_TITLE_KEY to data.title,
                    Constant.CONTENT_ID_KEY to data.id)
        }
    }
}
