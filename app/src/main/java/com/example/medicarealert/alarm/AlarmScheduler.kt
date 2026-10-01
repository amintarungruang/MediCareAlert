package com.example.medicarealert.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.medicarealert.R
import java.util.Calendar

class AlarmScheduler(private val ctx: Context) {

    /**
     * ตั้งปลุกแบบ exact; คืนค่า true ถ้าสำเร็จ, false ถ้าถูกบล็อค/ไม่มีสิทธิ์
     */
    fun scheduleExact(requestId: Int, hour: Int, minute: Int, title: String): Boolean {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DATE, 1)
        }

        val intent = Intent(ctx, ReminderReceiver::class.java).apply {
            putExtra("title", title.ifBlank { ctx.getString(R.string.app_name) })
        }

        val pi = PendingIntent.getBroadcast(
            ctx,
            requestId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val am = ctx.getSystemService(AlarmManager::class.java)

        // Android 12+ ต้องได้รับอนุญาต
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !am.canScheduleExactAlarms()) {
            // ผู้ใช้ยังไม่ได้อนุญาต exact alarm
            return false
        }

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
            } else {
                @Suppress("DEPRECATION")
                am.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
            }
            true
        } catch (se: SecurityException) {
            // กันแอปแครชถ้าโดนปฏิเสธสิทธิ์
            false
        }
    }
}
