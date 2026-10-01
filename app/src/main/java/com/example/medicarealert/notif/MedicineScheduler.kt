package com.example.medicarealert.notif

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import java.util.Calendar

class MedicineScheduler(private val ctx: Context) {


    fun scheduleDaily(requestId: Int, hour: Int, minute: Int, title: String): Boolean {
        val whenMs = nextTriggerAt(hour, minute)

        val am = ctx.getSystemService(AlarmManager::class.java)

        // Android 13+: ถ้าไม่ให้สิทธิ์ Notification จะเด้งไม่ได้อยู่ดี — แจ้งผู้เรียกให้ไปขอสิทธิ์ก่อน
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                ctx, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return false
        }

        // Android 12+: ต้องมีสิทธิ์ exact alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !am.canScheduleExactAlarms()) {
            return false
        }

        val pi = buildPendingIntent(requestId, title)

        return try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, whenMs, pi)
            } else {
                @Suppress("DEPRECATION")
                am.setExact(AlarmManager.RTC_WAKEUP, whenMs, pi)
            }
            true
        } catch (_: SecurityException) {
            false
        }
    }


    fun scheduleWindow(requestId: Int, triggerAt: Long, windowMs: Long, title: String) {
        val am = ctx.getSystemService(AlarmManager::class.java)
        val pi = buildPendingIntent(requestId, title)
        @Suppress("DEPRECATION")
        am.setWindow(AlarmManager.RTC_WAKEUP, triggerAt, windowMs, pi)
    }


    fun cancel(requestId: Int) {
        val am = ctx.getSystemService(AlarmManager::class.java)
        val pi = buildPendingIntent(requestId, "<cancel>")
        am.cancel(pi)
    }


    private fun nextTriggerAt(hour: Int, minute: Int): Long {
        val cal = Calendar.getInstance().apply {
            set(Calendar.MILLISECOND, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MINUTE, minute)
            set(Calendar.HOUR_OF_DAY, hour)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DATE, 1)
        }
        return cal.timeInMillis
    }


    private fun buildPendingIntent(requestId: Int, title: String): PendingIntent {
        val intent = Intent(ctx, com.example.medicarealert.alarm.ReminderReceiver::class.java).apply {
            action = "com.example.medicarealert.ACTION_REMINDER_$requestId"
            putExtra("title", title.ifBlank { "ถึงเวลาทานยา" })
            // ใส่ requestId เผื่อ Receiver อยากใช้
            putExtra("requestId", requestId)
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or
                PendingIntent.FLAG_CANCEL_CURRENT or
                PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getBroadcast(ctx, requestId, intent, flags)
    }
}
