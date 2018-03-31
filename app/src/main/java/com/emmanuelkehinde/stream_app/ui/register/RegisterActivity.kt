package com.emmanuelkehinde.stream_app.ui.register

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.emmanuelkehinde.stream_app.App
import com.emmanuelkehinde.stream_app.R
import com.emmanuelkehinde.stream_app.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java).also {
            App.getInstance().getDependencyComponent().inject(it)
        }
        attachObserver()

        btn_register.setOnClickListener {
            registerViewModel.registerUser(edt_fullname.text.toString(),edt_email.text.toString(),edt_password.text.toString())
        }

        layout_login.setOnClickListener {
            goToLoginActivity()
        }
    }

    private fun attachObserver() {
        registerViewModel.getValidationError().observe(this, Observer<String> {
            it?.let {
                showLongToast(it)
            }
        })

        registerViewModel.isLoading().observe(this, Observer<Boolean> {
            it?.let {
                if (it) {
                    showCustomDialog()
                }
                else hideCustomDialog()
            }
        })
        registerViewModel.getRegisterError().observe(this, Observer<Throwable> {
            it?.let {
                showLongToast("Registration Failed: " + it.localizedMessage)
            }
        })
        registerViewModel.getRegisterResponse().observe(this, Observer<String> {
            it?.let {
                goToLoginActivity()
            }
        })
    }

    private fun goToLoginActivity() {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.transition_left_to_right, R.anim.transition_right_to_left)
    }
}
