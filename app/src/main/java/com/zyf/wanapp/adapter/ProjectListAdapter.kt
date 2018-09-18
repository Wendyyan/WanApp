package com.zyf.wanapp.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zyf.wanapp.R
import com.zyf.wanapp.mvp.model.bean.ArticleBean
import com.zyf.wanapp.util.load

/**
 * Created by zyf on 2018/8/30.
 */
class ProjectListAdapter(private val context: Context?, data: MutableList<ArticleBean>):
        BaseQuickAdapter<ArticleBean, BaseViewHolder>(R.layout.item_project_list, data){

    override fun convert(helper: BaseViewHolder?, item: ArticleBean?) {
        item ?: return
        helper ?: return
        val charSequence = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(item.title, Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(item.title)
        helper.setText(R.id.tv_title, charSequence)
                .setText(R.id.tv_content, item.desc)
                .setText(R.id.tv_author, item.author)
                .setText(R.id.tv_time, item.niceDate)
                .setImageResource(R.id.iv_like,
                        if (item.collect) R.drawable.ic_like else R.drawable.ic_like_not)
                .addOnClickListener(R.id.iv_like)

        val ivThumbnail = helper.getView<ImageView>(R.id.iv_thumbnail)

        if (item.envelopePic.isNotEmpty()) {
            ivThumbnail.visibility = View.VISIBLE
            ivThumbnail.load(context, item.envelopePic)
        } else {
            ivThumbnail.visibility = View.GONE
        }
    }
}