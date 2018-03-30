package com.emmanuelkehinde.stream_app.ui.custom

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.emmanuelkehinde.stream_app.R

class CustomDialog(context: Context, private val isCancellable: Boolean = false) : ProgressDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setContentView(R.layout.progress_bar)
        setCancelable(isCancellable)
    }
}