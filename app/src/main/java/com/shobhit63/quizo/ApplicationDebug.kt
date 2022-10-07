package com.shobhit63.quizo

import android.app.Application
import timber.log.Timber

class ApplicationDebug:Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}