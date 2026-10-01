package com.example.medicarealert

import android.app.Application
import com.example.medicarealert.notif.NotificationHelper

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.ensureCreated(this)
    }
}
