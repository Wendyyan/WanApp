package com.zyf.wanapp.ui.activity

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zyf.wanapp.R
import com.zyf.wanapp.adapter.SearchHistoryAdapter
import com.zyf.wanapp.base.BaseActivity
import com.zyf.wanapp.mvp.contract.SearchContract
import com.zyf.wanapp.mvp.model.bean.HotKeyBean
import com.zyf.wanapp.mvp.model.bean.SearchHistoryBean
import com.zyf.wanapp.mvp.presenter.SearchPresenter
import com.zyf.wanapp.util.CommonUtil
import com.zyf.wanapp.widge.SpaceItemDecoration
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class SearchActivity : BaseActivity(), SearchContract.View {

    private var mHotKeys = mutableListOf<HotKeyBean>()

    private val historyList = mutableListOf<SearchHistoryBean>()

    private val searchHistoryAdapter by lazy {
        SearchHistoryAdapter(this, historyList)
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val searchPresenter: SearchPresenter by lazy {
        SearchPresenter()
    }

    override fun attachLayoutRes(): Int = R.layout.activity_search

    override fun initData() {

    }

    override fun initView() {
        searchPresenter.attachView(this)
        tvCancel.setOnClickListener { finish() }
        tflHotSearch.setOnTagClickListener { _, position, _ ->
            if (mHotKeys.size > 0) {
                gotoSearchList(mHotKeys[position].name)
            }
            true
        }
        rvHistory.run {
            layoutManager = linearLayoutManager
            adapter = searchHistoryAdapter
            itemAnimator = DefaultItemAnimator()
        }

        searchHistoryAdapter.run {
            bindToRecyclerView(rvHistory)
            onItemClickListener = this@SearchActivity.onItemClickListener
            onItemChildClickListener = this@SearchActivity.onItemChildClickListener
        }

        tvClear.setOnClickListener {
            historyList.clear()
            searchHistoryAdapter.replaceData(historyList)
            searchPresenter.clearHistory()
            tvSearchHistory.visibility = View.INVISIBLE
            tvClear.visibility = View.INVISIBLE
        }
        
        etSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (etSearch.text.isEmpty()) {
                    toast(getString(R.string.msg_input_search_content))
                } else {
                    gotoSearchList(etSearch.text.toString())
                }
                true
            }
            false
        }

        searchPresenter.requestHotKey()
    }

    override fun onResume() {
        super.onResume()
        searchPresenter.requestSearchHistory()
    }

    override fun setHotKeys(hotKeys: List<HotKeyBean>) {
        mHotKeys.addAll(hotKeys)
        tflHotSearch.run {
            adapter = object: TagAdapter<HotKeyBean>(hotKeys){
                override fun getView(parent: FlowLayout?, position: Int, hotKeyBean: HotKeyBean?): View {
                    val tv = LayoutInflater.from(context).inflate(R.layout.item_flow_tv, parent,
                            false) as TextView
                    tv.text = hotKeyBean?.name
                    return tv
                }
            }
        }
    }

    override fun setHistoryList(list: List<SearchHistoryBean>) {
        searchHistoryAdapter.replaceData(list)
        tvSearchHistory.visibility = if (list.isEmpty()) View.INVISIBLE else View.VISIBLE
        tvClear.visibility = if (list.isEmpty()) View.INVISIBLE else View.VISIBLE
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    private fun gotoSearchList(key: String){
        etSearch.setText(key)
        etSearch.setSelection(key.length)
        searchPresenter.saveSearchKey(key)
        startActivity<LoginActivity>()
    }

    /**
     * 点击文章item
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (searchHistoryAdapter.data.size != 0){
            val item = searchHistoryAdapter.data[position]
            gotoSearchList(item.key)
        }
    }

    /**
     * 删除指定搜索记录
     */
    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        if (searchHistoryAdapter.data.size != 0) {
            val item = searchHistoryAdapter.data[position]
            when (view.id) {
                R.id.iv_delete -> {
                    searchPresenter.deleteHistoryById(item.id)
                    searchHistoryAdapter.remove(position)
                }
            }
        }
    }
}
