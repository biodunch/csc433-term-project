package com.emmanuelkehinde.stream_app.di.component

import com.emmanuelkehinde.stream_app.di.module.ApiModule
import com.emmanuelkehinde.stream_app.di.module.AppModule
import com.emmanuelkehinde.stream_app.di.module.DependencyModule
import com.emmanuelkehinde.stream_app.ui.login.LoginViewModel
import com.emmanuelkehinde.stream_app.ui.register.RegisterViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApiModule::class, DependencyModule::class))
interface DependencyComponent {
    fun inject(loginViewModel: LoginViewModel)
    fun inject(registerViewModel: RegisterViewModel)

}