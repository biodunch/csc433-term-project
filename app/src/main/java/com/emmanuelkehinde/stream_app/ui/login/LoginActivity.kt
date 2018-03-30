package com.emmanuelkehinde.stream_app.ui.login

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.emmanuelkehinde.stream_app.R
import com.emmanuelkehinde.stream_app.ui.base.BaseActivity
import com.emmanuelkehinde.stream_app.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        btn_login.setOnClickListener {
            showCustomDialog(true)
        }

        layout_register.setOnClickListener {
            goToRegisterActivity()
        }
    }

    private fun goToRegisterActivity() {
        startActivity(Intent(this,RegisterActivity::class.java))
        overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit)
    }
}
