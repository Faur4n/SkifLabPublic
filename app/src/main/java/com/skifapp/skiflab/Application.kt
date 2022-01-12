package com.skifapp.skiflab

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.LogPriority

@HiltAndroidApp
class SkifApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // Log all priorities in debug builds, no-op in release builds.
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
    }
}