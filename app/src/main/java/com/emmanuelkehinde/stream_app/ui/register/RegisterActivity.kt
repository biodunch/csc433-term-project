package com.emmanuelkehinde.stream_app.ui.register

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.emmanuelkehinde.stream_app.R
import com.emmanuelkehinde.stream_app.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)

        btn_register.setOnClickListener {
            showCustomDialog(true)
        }

        layout_login.setOnClickListener {
            goToLoginActivity()
        }
    }

    private fun goToLoginActivity() {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.transition_left_to_right, R.anim.transition_right_to_left)
    }
}
