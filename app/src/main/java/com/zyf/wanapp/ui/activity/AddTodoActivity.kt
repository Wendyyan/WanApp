package com.zyf.wanapp.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.util.Log
import android.view.View
import com.zyf.wanapp.R
import com.zyf.wanapp.base.BaseActivity
import com.zyf.wanapp.constant.Constant
import com.zyf.wanapp.event.RefreshTodoEvent
import com.zyf.wanapp.mvp.contract.AddTodoContract
import com.zyf.wanapp.mvp.model.bean.TodoBean
import com.zyf.wanapp.mvp.presenter.AddTodoPresenter
import com.zyf.wanapp.util.closeKeyBoard
import com.zyf.wanapp.util.formatCurrentDate
import kotlinx.android.synthetic.main.activity_add_todo.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast
import java.util.*

class AddTodoActivity : BaseActivity(), AddTodoContract.View {

    /**
     * todo类型
     */
    private var type: Int = 0

    /**
     * 行为：添加/查看/编辑
     */
    private var todoAction: String? = null

    private var todoBean: TodoBean? = null

    private val addTodoPresenter by lazy {
        AddTodoPresenter()
    }

    override fun attachLayoutRes(): Int = R.layout.activity_add_todo

    override fun initData() {
        type = intent.extras?.getInt(Constant.TODO_TYPE) ?: 0
        todoAction = intent.extras?.getString(Constant.TODO_ACTION)
        todoBean = intent.extras?.getParcelable(Constant.TODO_BEAN)
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        addTodoPresenter.attachView(this)

        when (todoAction) {
            Constant.Type.ADD_TODO -> {
                initToolbar(toolbar, true, getString(R.string.info_add))
                tvDate.text = formatCurrentDate()
            }
            Constant.Type.SEE_TODO -> {
                initToolbar(toolbar, true, getString(R.string.info_detail))
                todoBean.let {
                    etTitle.isEnabled = false
                    etDetails.isEnabled = false
                    tvDate.isEnabled = false
                    etTitle.setText(it?.title)
                    etDetails.setText(it?.content)
                    tvDate.text = it?.dateStr
                    tvSave.visibility = View.INVISIBLE
                }
            }
            Constant.Type.EDIT_TODO ->{
                initToolbar(toolbar, true, getString(R.string.info_edit))
                todoBean.let {
                    etTitle.setText(it?.title)
                    etDetails.setText(it?.content)
                    tvDate.text = it?.dateStr
                }
            }
        }

        llDate.setOnClickListener {
            closeKeyBoard(etDetails, this)
            val now = Calendar.getInstance()
            val dpd = DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        val currentMonth = month + 1
                        tvDate.text = "$year-$currentMonth-$dayOfMonth"
                    }, now[Calendar.YEAR], now[Calendar.MONTH], now[Calendar.DAY_OF_MONTH])
            dpd.show()
        }

        tvSave.setOnClickListener {
            val map = mutableMapOf("title" to etTitle.text.toString(),
                    "content" to etDetails.text.toString(),
                    "date" to tvDate.text, "type" to type)

            if (todoAction.equals(Constant.Type.ADD_TODO)) {//添加todo
                addTodoPresenter.addTodo(map)
            } else { //更新todo
                map["status"] = todoBean!!.status
                addTodoPresenter.updateTodo(todoBean!!.id, map)
            }
        }
    }

    override fun addTodoSuccess(success: Boolean) {
        if (success) {
            toast(getString(R.string.msg_save_success))
            EventBus.getDefault().post(RefreshTodoEvent(true, type))
            finish()
        }
    }

    override fun updateTodoSuccess(success: Boolean) {
        if (success) {
            toast(getString(R.string.msg_save_success))
            EventBus.getDefault().post(RefreshTodoEvent(true, type))
            finish()
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }
}
