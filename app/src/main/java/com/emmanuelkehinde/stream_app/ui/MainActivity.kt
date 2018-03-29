package com.emmanuelkehinde.stream_app.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.emmanuelkehinde.shutdown.Shutdown
import com.emmanuelkehinde.stream_app.R
import com.emmanuelkehinde.stream_app.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        Shutdown.now(this)
    }
}
