package com.emmanuelkehinde.stream_app.di.module

import android.content.SharedPreferences
import com.emmanuelkehinde.stream_app.helper.SharedPrefHelper
import com.emmanuelkehinde.stream_app.network.ApiService
import com.emmanuelkehinde.stream_app.ui.login.LoginRepository
import com.emmanuelkehinde.stream_app.ui.register.RegisterRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DependencyModule {

    @Provides
    @Singleton
    fun provideLoginRepository(apiService: ApiService): LoginRepository {
        return LoginRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideRegisterRepository(apiService: ApiService): RegisterRepository {
        return RegisterRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideSharedPrefHelper(sharedPreferences: SharedPreferences): SharedPrefHelper {
        return SharedPrefHelper(sharedPreferences)
    }
}