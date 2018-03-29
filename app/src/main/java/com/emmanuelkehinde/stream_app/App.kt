package com.emmanuelkehinde.stream_app

import android.app.Application
import com.emmanuelkehinde.stream_app.di.component.ApiComponent
import com.emmanuelkehinde.stream_app.di.component.DaggerApiComponent
import com.emmanuelkehinde.stream_app.di.module.ApiModule
import com.emmanuelkehinde.stream_app.di.module.AppModule
import com.squareup.leakcanary.LeakCanary

class App: Application() {

    private lateinit var mInstance: App

    private lateinit var apiComponent: ApiComponent

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        initLeakCanary()

        apiComponent = DaggerApiComponent.builder()
                .appModule(AppModule(this))
                .apiModule(ApiModule())
                .build()
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
    }

    fun getInstance(): App {
        return mInstance
    }

    fun getApiComponent(): ApiComponent {
        return apiComponent
    }
}