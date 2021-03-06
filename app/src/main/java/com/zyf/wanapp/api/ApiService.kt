package com.zyf.wanapp.api

import com.zyf.wanapp.mvp.model.bean.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by zyf on 2018/8/30.
 */
interface ApiService {

    /**
     * 登录
     * https://www.wanandroid.com/user/login
     * @param username
     * @param password
     */
    @POST("user/login")
    @FormUrlEncoded
    fun login(@Field("username") username: String,
              @Field("password") password: String): Observable<HttpResult<LoginBean>>

    /**
     * 注册
     * https://www.wanandroid.com/user/register
     * @param username
     * @param password
     */
    @POST("user/register")
    @FormUrlEncoded
    fun register(@Field("username") username: String,
              @Field("password") password: String,
              @Field("repassword") rePassword:String): Observable<HttpResult<LoginBean>>

    /**
     * 收藏站内文章
     * https://www.wanandroid.com/lg/collect/1165/json
     * @param id article id
     */
    @POST("lg/collect/{id}/json")
    fun addCollectArticle(@Path("id") id: Int): Observable<HttpResult<Any>>

    /**
     * 文章列表中取消收藏文章
     * https://www.wanandroid.com/lg/uncollect_originId/2333/json
     * @param id
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun cancelCollectArticle(@Path("id") id: Int): Observable<HttpResult<Any>>

    /**
     * 获取轮播图
     * https://www.wanandroid.com/banner/json
     */
    @GET("banner/json")
    fun getBanners(): Observable<HttpResult<List<BannerBean>>>

    /**
     * 获取文章列表
     * https://www.wanandroid.com/article/list/0/json
     * @param pageNum
     */
    @GET("article/list/{pageNum}/json")
    fun getArticles(@Path("pageNum") pageNum: Int): Observable<HttpResult<ArticleResponseBody>>

    /**
     * 获取知识体系
     * https://www.wanandroid.com/tree/json
     */
    @GET("tree/json")
    fun getKnowledgeTree(): Observable<HttpResult<List<KnowledgeTreeBody>>>

    /**
     * 知识体系下的文章
     * https://www.wanandroid.com/article/list/0/json?cid=168
     * @param page
     * @param cid
     */
    @GET("article/list/{page}/json")
    fun getKnowledgeList(@Path("page") page: Int, @Query("cid") cid: Int):
            Observable<HttpResult<ArticleResponseBody>>

    /**
     * 导航数据
     * https://www.wanandroid.com/navi/json
     */
    @GET("navi/json")
    fun getNavigationList(): Observable<HttpResult<List<NavigationBean>>>

    /**
     * 项目数据
     * https://www.wanandroid.com/project/tree/json
     */
    @GET("project/tree/json")
    fun getProjectTree(): Observable<HttpResult<List<ProjectTreeBean>>>

    /**
     * 项目列表
     * https://www.wanandroid.com/project/list/1/json?cid=294
     * @param page
     * @param cid
     */
    @GET("project/list/{page}/json")
    fun getProjectList(@Path("page") page: Int, @Query("cid") cid: Int):
            Observable<HttpResult<ArticleResponseBody>>

    /**
     *  获取收藏列表
     *  https://www.wanandroid.com/lg/collect/list/0/json
     *  @param page
     */
    @GET("lg/collect/list/{page}/json")
    fun getCollectList(@Path("page") page: Int):
            Observable<HttpResult<CollectionResponseBody<CollectionArticle>>>

    /**
     * 收藏列表中取消收藏文章
     * https://www.wanandroid.com/lg/uncollect/2805/json
     * @param id
     * @param originId
     */
    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    fun removeCollectArticle(@Path("id") id: Int,
                             @Field("originId") originId: Int = -1): Observable<HttpResult<Any>>

    /**
     * 搜索热词
     * https://www.wanandroid.com/hotkey/json
     */
    @GET("hotkey/json")
    fun getHotKey(): Observable<HttpResult<List<HotKeyBean>>>

    /**
     * 搜索
     * https://www.wanandroid.com/article/query/0/json
     * @param page
     * @param key
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    fun queryBySearchKey(@Path("page") page: Int, @Field("k") key: String):
            Observable<HttpResult<ArticleResponseBody>>

    /**
     * 获取TODO列表数据
     * https://wanandroid.com/lg/todo/list/0/json
     * @param type
     */
    @POST("/lg/todo/list/{type}/json")
    fun getTodoList(@Path("type") type: Int): Observable<HttpResult<AllTodoResponseBody>>

    /**
     * 获取未完成Todo列表
     * https://wanandroid.com/lg/todo/listnotdo/0/json/1
     * @param type 类型拼接在链接上，目前支持0,1,2,3
     * @param page 拼接在链接上，从1开始
     */
    @POST("/lg/todo/listnotdo/{type}/json/{page}")
    fun getNoTodoList(@Path("page") page: Int, @Path("type") type: Int):
            Observable<HttpResult<TodoResponseBody>>

    /**
     * 获取已完成Todo列表
     * https://www.wanandroid.com/lg/todo/listdone/0/json/1
     * @param type 类型拼接在链接上，目前支持0,1,2,3
     * @param page 拼接在链接上，从1开始
     */
    @POST("/lg/todo/listdone/{type}/json/{page}")
    fun getDoneList(@Path("page") page: Int, @Path("type") type: Int):
            Observable<HttpResult<TodoResponseBody>>

    /**
     * 仅更新完成状态Todo
     * https://www.wanandroid.com/lg/todo/done/80/json
     * @param id 拼接在链接上，为唯一标识
     * @param status 0或1，传1代表未完成到已完成，反之则反之
     */
    @POST("/lg/todo/done/{id}/json")
    @FormUrlEncoded
    fun updateTodoStatus(@Path("id") id: Int, @Field("status") status: Int):
            Observable<HttpResult<Any>>

    /**
     * 删除一条Todo
     * https://www.wanandroid.com/lg/todo/delete/83/json
     * @param id
     */
    @POST("/lg/todo/delete/{id}/json")
    fun deleteTodoById(@Path("id") id: Int): Observable<HttpResult<Any>>

    /**
     * 新增一条Todo
     * https://www.wanandroid.com/lg/todo/add/json
     * @param body
     *          title: 新增标题
     *          content: 新增详情
     *          date: 2018-08-01
     *          type: 0
     */
    @POST("/lg/todo/add/json")
    @FormUrlEncoded
    fun addTodo(@FieldMap map: MutableMap<String, Any>): Observable<HttpResult<Any>>

    /**
     * 更新一条Todo内容
     * https://www.wanandroid.com/lg/todo/update/83/json
     * @param body
     *          title: 新增标题
     *          content: 新增详情
     *          date: 2018-08-01
     *          status: 0 // 0为未完成，1为完成
     *          type: 0
     */
    @POST("/lg/todo/update/{id}/json")
    @FormUrlEncoded
    fun updateTodo(@Path("id") id: Int, @FieldMap map: MutableMap<String, Any>):
            Observable<HttpResult<Any>>

}