package com.zyf.wanapp.mvp.model.bean

import com.chad.library.adapter.base.entity.SectionEntity

/**
 * Created by zyf on 2018/9/17.
 */
class TodoDataBean : SectionEntity<TodoBean> {

    constructor(isHeader: Boolean, header: String?): super(isHeader, header)

    constructor(todoBean: TodoBean): super(todoBean)

}