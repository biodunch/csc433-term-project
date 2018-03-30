package com.emmanuelkehinde.stream_app.ui.login

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.emmanuelkehinde.stream_app.R
import com.emmanuelkehinde.stream_app.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        addFullScreenParameters()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        btn_login.setOnClickListener {
            showCustomDialog(true)
        }

//        overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit)

//        overridePendingTransition(R.anim.transition_left_to_right, R.anim.transition_right_to_left)

    }
}
