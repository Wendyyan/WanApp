package com.zyf.wanapp.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zyf.wanapp.R
import com.zyf.wanapp.mvp.model.bean.ArticleBean
import com.zyf.wanapp.mvp.model.bean.CollectionArticle
import com.zyf.wanapp.util.load

/**
 * Created by zyf on 2018/8/30.
 */
class CollectListAdapter(private val context: Context?, data: MutableList<CollectionArticle>):
        BaseQuickAdapter<CollectionArticle, BaseViewHolder>(R.layout.item_home_list, data){

    override fun convert(helper: BaseViewHolder?, item: CollectionArticle?) {
        item ?: return
        helper ?: return
        val charSequence = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(item.title, Html.FROM_HTML_MODE_LEGACY) else Html.fromHtml(item.title)
        helper.setText(R.id.tv_article_title, charSequence)
                .setText(R.id.tv_article_author, item.author)
                .setText(R.id.tv_article_date, item.niceDate)
                .setImageResource(R.id.iv_like, R.drawable.ic_like)
                .addOnClickListener(R.id.iv_like)

        val ivThumbnail = helper.getView<ImageView>(R.id.iv_article_thumbnail)
        val tvTag = helper.getView<TextView>(R.id.tv_article_tag)
        val tvFresh = helper.getView<TextView>(R.id.tv_article_fresh)

        if (item.envelopePic.isNotEmpty()) {
            ivThumbnail.visibility = View.VISIBLE
            ivThumbnail.load(context, item.envelopePic)
        } else {
            ivThumbnail.visibility = View.GONE
        }
        if (item.chapterName.isNotEmpty()){
            tvTag.visibility = View.VISIBLE
            tvTag.text = item.chapterName
        } else {
            tvTag.visibility = View.INVISIBLE
        }
        tvFresh.visibility = View.GONE
    }
}