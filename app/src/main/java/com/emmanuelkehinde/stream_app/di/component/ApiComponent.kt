package com.emmanuelkehinde.stream_app.di.component

import com.emmanuelkehinde.stream_app.di.module.ApiModule
import com.emmanuelkehinde.stream_app.di.module.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, ApiModule::class))
interface ApiComponent {

}