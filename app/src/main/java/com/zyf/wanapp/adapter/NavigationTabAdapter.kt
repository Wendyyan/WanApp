package com.zyf.wanapp.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import com.zyf.wanapp.R
import com.zyf.wanapp.mvp.model.bean.NavigationBean
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView

/**
 * Created by zyf on 2018/9/3.
 */
class NavigationTabAdapter(private val context: Context?, private val data: List<NavigationBean>):
        TabAdapter {

    override fun getIcon(position: Int): ITabView.TabIcon? = null

    override fun getBadge(position: Int): ITabView.TabBadge? = null

    override fun getBackground(position: Int): Int = -1

    override fun getTitle(position: Int): ITabView.TabTitle {
        return ITabView.TabTitle.Builder()
                .setContent(data[position].name)
                .setTextColor(ContextCompat.getColor(context!!, R.color.vertical_tab_tv_color),
                        ContextCompat.getColor(context, R.color.Grey500))
                .setTextSize(15)
                .build()
    }

    override fun getCount(): Int = data.size
}