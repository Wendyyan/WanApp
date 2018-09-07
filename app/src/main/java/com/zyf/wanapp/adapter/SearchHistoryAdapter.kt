package com.zyf.wanapp.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zyf.wanapp.R
import com.zyf.wanapp.mvp.model.bean.SearchHistoryBean

/**
 * Created by zyf on 2018/9/5.
 */
class SearchHistoryAdapter(val context: Context, data: MutableList<SearchHistoryBean>):
        BaseQuickAdapter<SearchHistoryBean, BaseViewHolder>(R.layout.item_search_history, data) {

    override fun convert(helper: BaseViewHolder?, item: SearchHistoryBean?) {
        helper ?: return
        item ?: return
        helper.setText(R.id.tv_search_key, item.key)
                .addOnClickListener(R.id.iv_delete)
    }
}