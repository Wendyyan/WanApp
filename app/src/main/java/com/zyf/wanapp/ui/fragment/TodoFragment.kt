package com.zyf.wanapp.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zyf.wanapp.R
import com.zyf.wanapp.adapter.TodoAdapter
import com.zyf.wanapp.base.BaseFragment
import com.zyf.wanapp.constant.Constant
import com.zyf.wanapp.event.RefreshTodoEvent
import com.zyf.wanapp.event.SwitchTodoEvent
import com.zyf.wanapp.mvp.contract.TodoContract
import com.zyf.wanapp.mvp.model.bean.AllTodoResponseBody
import com.zyf.wanapp.mvp.model.bean.TodoBean
import com.zyf.wanapp.mvp.model.bean.TodoDataBean
import com.zyf.wanapp.mvp.model.bean.TodoResponseBody
import com.zyf.wanapp.mvp.presenter.TodoPresenter
import com.zyf.wanapp.ui.activity.AddTodoActivity
import com.zyf.wanapp.util.DialogUtil
import com.zyf.wanapp.widge.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_todo.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class TodoFragment : BaseFragment(), TodoContract.View {

    companion object {
        fun getInstance(type: Int): TodoFragment {
            val fragment = TodoFragment()
            val args = Bundle()
            args.putInt(Constant.TODO_TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }

    private var type: Int = 0

    /**
     * 是否刷新
     */
    private var isRefresh = true

    /**
     * 是否是已完成 false->待办 true->已完成
     */
    private var isDone: Boolean = false

    private val todoList = mutableListOf<TodoDataBean>()

    private val todoAdapter by lazy {
        TodoAdapter(todoList)
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    /**
     * RecyclerView Divider
     */
    private val recyclerViewItemDecoration by lazy {
        SpaceItemDecoration(activity!!)
    }

    private val todoPresenter by lazy {
        TodoPresenter()
    }

    override fun useEventBus(): Boolean = true

    override fun attachLayoutRes(): Int = R.layout.fragment_todo

    override fun initView() {
        type = arguments?.getInt(Constant.TODO_TYPE) ?: 0
        todoPresenter.attachView(this)

        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
            setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary))
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = todoAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recyclerViewItemDecoration)
        }

        todoAdapter.run {
            bindToRecyclerView(recyclerView)
            setEmptyView(R.layout.layout_empty, recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@TodoFragment.onItemClickListener
            onItemLongClickListener = this@TodoFragment.onItemLongClickListener
            onItemChildClickListener = this@TodoFragment.onItemChildClickListener
        }
    }

    override fun lazyLoad() {
        if (isDone) {
            todoPresenter.getDoneList(1, type)
        } else {
            todoPresenter.getNoTodoList(1, type)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshTodoEvent(event: RefreshTodoEvent){
        if (event.isRefresh && type == event.type) {
            lazyLoad()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun switchTodo(event: SwitchTodoEvent) {
        if (type == event.curIndex && isDone != event.switchDone) {
            isDone = event.switchDone
            lazyLoad()
        }
    }

    override fun showAllTodoList(allTodoResponseBody: AllTodoResponseBody) {

    }

    override fun showTodoList(todoResponseBody: TodoResponseBody) {
        // TODO 待优化
        val list = mutableListOf<TodoDataBean>()
        var isHeader: Boolean
        todoResponseBody.datas.forEach { todoBean ->
            isHeader = true
            for (i in list.indices) {
                if (todoBean.dateStr == list[i].header) {
                    isHeader = false
                    break
                }
            }
            if (isHeader)
                list.add(TodoDataBean(true, todoBean.dateStr))
            list.add(TodoDataBean(todoBean))
        }

        list.let {
            todoAdapter.run {
                if (isRefresh) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                if (it.size < todoResponseBody.size) {
                    loadMoreEnd(isRefresh)
                } else {
                    loadMoreComplete()
                }
            }
        }
    }

    override fun showDeleteSuccess(success: Boolean) {
        if (success) {
            activity?.toast(getString(R.string.msg_delete_success))
        }
    }

    override fun showUpdateSuccess(success: Boolean, status: Int) {
        if (success) {
            activity?.run {
                if (status == 1) {
                    toast(getString(R.string.msg_have_finished))
                } else {
                    toast(getString(R.string.msg_have_recovery))
                }
            }
        }
    }

    override fun showLoading() {
        swipeRefreshLayout?.isRefreshing = false
        if (isRefresh) {
            todoAdapter.setEnableLoadMore(true)
        }
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
        if (isRefresh) {
            todoAdapter.setEnableLoadMore(true)
        }
    }

    override fun showError(errorMsg: String) {
        todoAdapter.run {
            if (isRefresh) setEnableLoadMore(true) else loadMoreFail()
        }
        activity?.toast(errorMsg)
    }

    /**
     * 刷新数据
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        todoAdapter.setEnableLoadMore(false)
        lazyLoad()
    }

    /**
     * 加载更多
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = todoAdapter.data.size / 20 + 1
        if (isDone) {
            todoPresenter.getDoneList(page, type)
        } else {
            todoPresenter.getNoTodoList(page, type)
        }
    }

    /**
     * 点击item
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (todoList.size != 0){
            val data = todoList[position].t as TodoBean
            activity?.startActivity<AddTodoActivity>(
                    if (isDone)
                        Constant.TODO_ACTION to Constant.Type.SEE_TODO
                    else
                        Constant.TODO_ACTION to Constant.Type.EDIT_TODO,
                    Constant.TODO_BEAN to data)
        }
    }

    /**
     * 长按删除todo
     */
    private val onItemLongClickListener = BaseQuickAdapter.OnItemLongClickListener { adapter, view, position ->
        if (todoList.size != 0){
            val data = todoList[position].t
            activity?.let {
                DialogUtil.getConfirmDialog(it, getString(R.string.msg_confirm_delete),
                        DialogInterface.OnClickListener { _, _ ->
                            todoPresenter.deleteTodoById(data.id)
                            todoAdapter.remove(position)
                        }).show()
            }
        }
        true
    }

    /**
     * 修改todo状态
     */
    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        if (todoList.size != 0) {
            val data = todoList[position].t
            when(view.id) {
                R.id.tv_finish -> {
                    todoPresenter.updateTodoStatus(data.id, 1)//状态修改为完成
                    todoAdapter.remove(position)
                }
                R.id.tv_recovery -> {
                    todoPresenter.updateTodoStatus(data.id, 0)//状态修改为未完成
                    todoAdapter.remove(position)
                }
            }
        }
    }
}
