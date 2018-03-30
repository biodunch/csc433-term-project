package com.emmanuelkehinde.stream_app.ui.splash

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.emmanuelkehinde.stream_app.R
import com.emmanuelkehinde.stream_app.ui.base.BaseActivity
import com.emmanuelkehinde.stream_app.ui.MainActivity
import com.emmanuelkehinde.stream_app.ui.login.LoginActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        addFullScreenParameters()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashViewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        splashViewModel.startCountDown()
        splashViewModel.goToNext().observe(this, Observer { t ->
            if (t == true) {
                goToLoginActivity()
//                goToMainActivity()
            }
        })
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun goToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }


}
