package com.zyf.wanapp.mvp.model

import com.zyf.wanapp.http.RetrofitHelper
import com.zyf.wanapp.mvp.model.bean.ArticleResponseBody
import com.zyf.wanapp.mvp.model.bean.HttpResult
import com.zyf.wanapp.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by zyf on 2018/9/3.
 */
class KnowledgeModel : CommonModel() {

    fun getKnowledgeList(page: Int, cid: Int): Observable<HttpResult<ArticleResponseBody>>
            = RetrofitHelper.service.getKnowledgeList(page, cid).compose(SchedulerUtils.ioToMain())

}
