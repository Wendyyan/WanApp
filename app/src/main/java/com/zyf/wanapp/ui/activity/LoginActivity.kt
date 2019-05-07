package com.zyf.wanapp.ui.activity

import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.zyf.wanapp.R
import com.zyf.wanapp.base.BaseActivity
import com.zyf.wanapp.event.LoginEvent
import com.zyf.wanapp.mvp.contract.LoginContract
import com.zyf.wanapp.mvp.model.bean.LoginBean
import com.zyf.wanapp.mvp.presenter.LoginPresenter
import com.zyf.wanapp.util.DelegateExt
import com.zyf.wanapp.util.DialogUtil
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class LoginActivity : BaseActivity(), LoginContract.View {

    companion object {
        private const val CODE_GOTO_REGISTER = 101
    }

    private val mDialog by lazy {
        DialogUtil.getWaitDialog(this, getString(R.string.info_login_ing))
    }

    private val loginPresenter: LoginPresenter by lazy {
        LoginPresenter()
    }

    override fun attachLayoutRes(): Int = R.layout.activity_login

    override fun initData() {
    }

    override fun initView() {
        loginPresenter.attachView(this)
        initToolbar(toolbar, true, getString(R.string.info_login))
        btnLogin.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btnLogin ->{
                login()
            }
        }
    }

    private fun login(){
        DelegateExt.run {
            username = etUsername.text.toString()
            pwd = etPwd.text.toString()
            when {
                username.isEmpty() -> toast(getString(R.string.msg_input_username))
                pwd.isEmpty() -> toast(getString(R.string.msg_input_password))
                else -> {
                    loginPresenter.login(username, pwd)
                }
            }
        }
    }

    override fun loginSuccess(data: LoginBean) {
        toast(getString(R.string.msg_login_success))
        DelegateExt.run {
            isLogin = true
            username = data.username
            pwd = data.password
        }

        EventBus.getDefault().post(LoginEvent(true))
        finish()
    }

    override fun showLoading() {
        mDialog.show()
    }

    override fun hideLoading() {
        mDialog.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_register, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_register ->{
                this.startActivityForResult<RegisterActivity>(CODE_GOTO_REGISTER)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == CODE_GOTO_REGISTER) {
            finish()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
