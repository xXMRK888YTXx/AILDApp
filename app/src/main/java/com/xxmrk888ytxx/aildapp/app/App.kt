package com.xxmrk888ytxx.aildapp.app

import android.app.Application
import androidx.work.Configuration
import com.xxmrk888ytxx.aildapp.di.applicationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(applicationModule)
        }
    }

    override val workManagerConfiguration: Configuration by lazy {
        Configuration.Builder()
            .setDefaultProcessName("${packageName}:remoteWorkManagerProcess")
            .build()
    }


}