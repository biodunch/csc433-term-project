package com.emmanuelkehinde.stream_app.ui.splash

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import android.util.Log

class SplashViewModel: ViewModel() {

    private var next: MutableLiveData<Boolean> = MutableLiveData()

    fun goToNext(): MutableLiveData<Boolean> {
        return next
    }

    fun startCountDown()  {
        val handler = Handler()

        handler.postDelayed({
            next.value = true
        }, 3000)
    }
}