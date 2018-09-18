package com.zyf.wanapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import com.zyf.wanapp.R
import com.zyf.wanapp.constant.Constant
import com.zyf.wanapp.mvp.model.bean.ArticleBean
import com.zyf.wanapp.mvp.model.bean.NavigationBean
import com.zyf.wanapp.ui.activity.ContentActivity
import org.jetbrains.anko.startActivity

/**
 * Created by zyf on 2018/9/3.
 */
class NavigationAdapter(data: MutableList<NavigationBean>):
        BaseQuickAdapter<NavigationBean, BaseViewHolder>(R.layout.item_navigation_list, data) {

    override fun convert(helper: BaseViewHolder?, item: NavigationBean?) {
        item ?: return
        helper ?: return
        helper.setText(R.id.tv_navigation_title, item.name)
        val tflTag = helper.getView<TagFlowLayout>(R.id.tfl_tag)
        val articles: List<ArticleBean> = item.articles
        tflTag.run {
            adapter = object : TagAdapter<ArticleBean>(articles){
                override fun getView(parent: FlowLayout?, position: Int, article: ArticleBean?): View? {
                    val tv = LayoutInflater.from(context).inflate(R.layout.item_flow_tv, parent,
                            false) as TextView
                    article ?: return null

                    tv.run {
                        text = article.title
                    }
                    setOnTagClickListener { _, pos, _ ->
                        val data: ArticleBean = articles[pos]
                        context.startActivity<ContentActivity>(
                                Constant.CONTENT_URL_KEY to data.link,
                                Constant.CONTENT_TITLE_KEY to data.title,
                                Constant.CONTENT_ID_KEY to data.id)
                        true
                    }
                    return tv
                }
            }
        }
    }
}