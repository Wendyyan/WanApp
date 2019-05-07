package com.zyf.wanapp.ui.activity

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.view.View
import com.zyf.wanapp.R
import com.zyf.wanapp.base.BaseActivity
import com.zyf.wanapp.event.LoginEvent
import com.zyf.wanapp.mvp.contract.RegisterContract
import com.zyf.wanapp.mvp.model.bean.LoginBean
import com.zyf.wanapp.mvp.presenter.RegisterPresenter
import com.zyf.wanapp.util.DelegateExt
import com.zyf.wanapp.util.DialogUtil
import com.zyf.wanapp.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

/**
 * 注册
 */
class RegisterActivity : BaseActivity(), RegisterContract.View {



    private val mDialog by lazy {
        DialogUtil.getWaitDialog(this, getString(R.string.info_registering))
    }

    private val registerPresenter: RegisterPresenter by lazy {
        RegisterPresenter()
    }

    override fun attachLayoutRes(): Int = R.layout.activity_register

    override fun initData() {
    }

    override fun initView() {
        StatusBarUtil.setColorNoTranslucent(this@RegisterActivity,
                ContextCompat.getColor(this, R.color.Blue_Grey_dark))
        initToolbar(toolbar, true, getString(R.string.info_register))
        registerPresenter.attachView(this@RegisterActivity)
        btnRegister.setOnClickListener(View.OnClickListener {
            register();
        })
    }

    /**
     * 注册
     */
    private fun register() {
        when {
            etUsername.text.isEmpty() -> toast(getString(R.string.msg_input_username))
            etPwd.text.isEmpty() -> toast(getString(R.string.msg_input_password))
            etConfirmPwd.text.isEmpty() -> toast(getString(R.string.msg_confirm_password))
            etConfirmPwd.text.toString() != etPwd.text.toString() -> toast(getString(R.string.msg_different_password))
            else -> {
                registerPresenter.register(etUsername.text.toString(), etPwd.text.toString(),
                        etConfirmPwd.text.toString())
            }
        }
    }

    override fun registerSuccess(data: LoginBean) {
        toast(getString(R.string.msg_register_success))
        DelegateExt.run {
            isLogin = true
            username = data.username
            pwd = data.password
        }

        EventBus.getDefault().post(LoginEvent(true))
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun showLoading() {
        mDialog.show()
    }

    override fun hideLoading() {
        mDialog.dismiss()
    }
}
