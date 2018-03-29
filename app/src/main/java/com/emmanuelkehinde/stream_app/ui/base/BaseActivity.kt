package com.emmanuelkehinde.stream_app.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import android.widget.Toast

open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    internal fun addFullScreenParameters() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    internal fun showToast(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    internal fun showLongToast(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }
}