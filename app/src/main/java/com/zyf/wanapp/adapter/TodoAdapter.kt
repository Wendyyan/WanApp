package com.zyf.wanapp.adapter

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zyf.wanapp.R
import com.zyf.wanapp.mvp.model.bean.TodoDataBean

/**
 * Created by zyf on 2018/9/17.
 */
class TodoAdapter(data: MutableList<TodoDataBean>) :
        BaseSectionQuickAdapter<TodoDataBean, BaseViewHolder>(
                R.layout.item_list_todo, R.layout.item_sticky_header, data) {

    override fun convertHead(helper: BaseViewHolder?, item: TodoDataBean?) {
        helper ?: return
        item ?: return
        helper.setText(R.id.tv_date, item.header)
    }

    override fun convert(helper: BaseViewHolder?, item: TodoDataBean?) {
        helper ?: return
        item?.t ?: return
        helper.setText(R.id.tv_title, item.t.title)
                .setText(R.id.tv_content, item.t.content)
                .addOnClickListener(R.id.tv_finish)
                .addOnClickListener(R.id.tv_recovery)

        val tvFinish = helper.getView<TextView>(R.id.tv_finish)
        val tvRecovery = helper.getView<TextView>(R.id.tv_recovery)
        tvFinish.visibility = if (item.t.status == 0) View.VISIBLE else View.GONE
        tvRecovery.visibility = if (item.t.status == 0) View.GONE else View.VISIBLE
    }
}