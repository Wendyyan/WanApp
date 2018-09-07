package com.zyf.wanapp.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zyf.wanapp.R
import com.zyf.wanapp.mvp.model.bean.KnowledgeTreeBody

/**
 * Created by zyf on 2018/9/1.
 */
class KnowledgeTreeAdapter(val context: Context?, data: MutableList<KnowledgeTreeBody>?):
        BaseQuickAdapter<KnowledgeTreeBody, BaseViewHolder>(R.layout.item_knowledge_tree_list, data) {

    override fun convert(helper: BaseViewHolder?, item: KnowledgeTreeBody?) {
        helper?.setText(R.id.tv_title_first, item?.name)
        item?.children.let {
            helper?.setText(R.id.tv_title_second,
                    it?.joinToString("    ", transform = {child -> child.name}))
        }
    }
}