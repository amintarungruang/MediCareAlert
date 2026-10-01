package com.example.medicarealert.notif

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationHelper {
    const val CHANNEL_ID_MED = "medicare_alert_channel_med"
    private const val CHANNEL_NAME = "การเตือนทานยา"
    private const val CHANNEL_DESC = "แจ้งเตือนเมื่อถึงเวลากินยา"

    fun ensureCreated(ctx: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = ctx.getSystemService(NotificationManager::class.java)
            val ch = NotificationChannel(
                CHANNEL_ID_MED,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
                enableLights(true)
                enableVibration(true)
            }
            nm.createNotificationChannel(ch)
        }
    }
}
