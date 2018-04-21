package com.emmanuelkehinde.stream_app

import android.app.Application
import com.emmanuelkehinde.stream_app.di.component.ApiComponent
import com.emmanuelkehinde.stream_app.di.component.DaggerApiComponent
import com.emmanuelkehinde.stream_app.di.component.DaggerDependencyComponent
import com.emmanuelkehinde.stream_app.di.component.DependencyComponent
import com.emmanuelkehinde.stream_app.di.module.ApiModule
import com.emmanuelkehinde.stream_app.di.module.AppModule
import com.emmanuelkehinde.stream_app.di.module.DependencyModule
import com.squareup.leakcanary.LeakCanary

class App: Application() {


    private lateinit var apiComponent: ApiComponent

    private lateinit var dependencyComponent: DependencyComponent

    companion object {
        private lateinit var mInstance: App

        fun getInstance(): App {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        initLeakCanary()

        apiComponent = DaggerApiComponent.builder()
                .appModule(AppModule(this))
                .apiModule(ApiModule())
                .build()

        dependencyComponent = DaggerDependencyComponent.builder()
                .apiModule(ApiModule())
                .dependencyModule(DependencyModule())
                .build()
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
//        LeakCanary.install(this)
    }


    fun getApiComponent(): ApiComponent {
        return apiComponent
    }

    fun getDependencyComponent(): DependencyComponent {
        return dependencyComponent
    }
}