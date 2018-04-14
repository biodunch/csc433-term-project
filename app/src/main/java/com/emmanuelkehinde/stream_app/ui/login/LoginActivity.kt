package com.emmanuelkehinde.stream_app.ui.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.emmanuelkehinde.stream_app.App
import com.emmanuelkehinde.stream_app.R
import com.emmanuelkehinde.stream_app.ui.base.BaseActivity
import com.emmanuelkehinde.stream_app.ui.register.RegisterActivity
import com.emmanuelkehinde.stream_app.ui.streaming.StreamingActivity
import com.emmanuelkehinde.stream_app.ui.streaming.StreamingActivity2
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java).also {
            App.getInstance().getDependencyComponent().inject(it)
        }
        attachObserver()

        btn_login.setOnClickListener {
            loginViewModel.loginUser(edt_email.text.toString(),edt_password.text.toString())
        }

        layout_register.setOnClickListener {
            goToRegisterActivity()
        }
    }

    private fun attachObserver() {
        loginViewModel.getValidationError().observe(this,Observer<String> {
            it?.let {
                showLongToast(it)
            }
        })

        loginViewModel.isLoading().observe(this, Observer<Boolean> {
            it?.let {
                if (it) {
                    showCustomDialog()
                }
                else hideCustomDialog()
            }
        })
        loginViewModel.getLoginError().observe(this, Observer<Throwable> {
            it?.let {
                showLongToast("Login Failed: " + it.localizedMessage)
            }
        })
        loginViewModel.getLoginResponse().observe(this, Observer<String> {
            it?.let {
                goToStreamingActivity()
            }
        })
    }

    private fun goToRegisterActivity() {
        startActivity(Intent(this,RegisterActivity::class.java))
        overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit)
    }

    private fun goToStreamingActivity() {
        startActivity(Intent(this, StreamingActivity2::class.java))
        finish()
    }
}
