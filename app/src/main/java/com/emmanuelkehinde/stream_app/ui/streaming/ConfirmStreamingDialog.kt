package com.emmanuelkehinde.stream_app.ui.streaming

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.emmanuelkehinde.stream_app.R
import kotlinx.android.synthetic.main.confirm_streaming_dialog.*

class ConfirmStreamingDialog: DialogFragment() {

    private var confirmStreamingListener: ConfirmStreamingListener? = null
    private var edtStreamName: EditText? = null

    interface ConfirmStreamingListener {
        fun onYesTapped(streamName: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = LayoutInflater.from(activity)
        @SuppressLint("InflateParams") val custom = inflater.inflate(R.layout.confirm_streaming_dialog, null)
        edtStreamName = custom.findViewById(R.id.edtStreamName) as EditText

        builder.setView(custom)
        builder.setPositiveButton("YES", null)
        builder.setNegativeButton("NO", null)

        val dialog = builder.create()
        dialog.setTitle("Start LiveStream?")
        dialog.setOnShowListener {
            val btnYes = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val btnNo = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            btnYes.setOnClickListener {
                if (edtStreamName?.text.isNullOrEmpty()) {
                    Toast.makeText(activity,"Insert your preferred stream name",Toast.LENGTH_SHORT).show()
                } else {
                    confirmStreamingListener?.onYesTapped(edtStreamName?.text.toString())
                    dismissAllowingStateLoss()
                }
            }

            btnNo.setOnClickListener {
                dismissAllowingStateLoss()
            }

        }

        return dialog
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            confirmStreamingListener = context as ConfirmStreamingListener?
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(context!!.toString() + " must implement ConfirmStreamingListener")
        }

    }
}