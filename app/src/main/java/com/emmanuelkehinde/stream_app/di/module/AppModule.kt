package com.emmanuelkehinde.stream_app.di.module

import android.content.Context
import com.emmanuelkehinde.stream_app.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(var app: App) {

    @Provides
    @Singleton
    internal fun provideApplication(): App {
        return app
    }

    @Provides
    @Singleton
    internal fun provideContext(): Context {
        return app
    }
}