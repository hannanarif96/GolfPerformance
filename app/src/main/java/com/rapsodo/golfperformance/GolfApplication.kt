package com.rapsodo.golfperformance

import android.app.Application
import com.rapsodo.golfperformance.data.remote.LocalMockServer
import com.rapsodo.golfperformance.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class GolfApplication : Application() {
    private lateinit var server: LocalMockServer

    override fun onCreate() {
        super.onCreate()
        
        setupLocalServer()
        
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@GolfApplication)
            modules(appModule)
        }
    }

    // Using a locally served mock server inside the app for testing
    private fun setupLocalServer() {
        try {
            server = LocalMockServer(this)
            server.start()
            Timber.d("LocalMockServer started on port ${server.listeningPort}")
        } catch (e: Exception) {
            Timber.e(e, "Failed to start LocalMockServer")
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }
}
