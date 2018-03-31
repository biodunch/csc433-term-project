package com.emmanuelkehinde.stream_app.ui.base

import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.emmanuelkehinde.stream_app.ui.custom.CustomDialog

open class BaseActivity: AppCompatActivity() {

    private lateinit var customDialog: CustomDialog

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

    internal fun showCustomDialog(cancellable: Boolean = false) {
        customDialog = CustomDialog(this,cancellable)
        customDialog.show()
    }

    internal fun hideCustomDialog() {
        if (customDialog.isShowing) {
            customDialog.cancel()
        }
    }

    internal fun showConfirmDialog(title: String?, message: String?,
                                   positiveBtnClickListener: () -> Unit, negativeBtnClickListener: (() -> Unit)?){
        val builder = AlertDialog.Builder(this)
                .setCancelable(false)
                .setPositiveButton("YES", { dialogInterface, i ->
                    positiveBtnClickListener.invoke()
                })
                .setNegativeButton("NO", { dialogInterface, i ->
                    dialogInterface.cancel()
                    negativeBtnClickListener?.invoke()
                })
        val alertDialog = builder.create()
        title?.let { alertDialog.setTitle(it) }
        message?.let { alertDialog.setMessage(it) }
        alertDialog.show()
    }
}