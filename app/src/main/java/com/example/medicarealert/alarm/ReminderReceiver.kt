package com.example.medicarealert.alarm

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.medicarealert.R
import com.example.medicarealert.notif.NotificationHelper
import com.example.medicarealert.MainActivity

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context, intent: Intent) {

        // Android 13+: ต้องมีสิทธิ์ก่อน
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                ctx,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return
        }

        // สร้าง channel ถ้ายังไม่มี
        NotificationHelper.ensureCreated(ctx)

        val title = intent.getStringExtra("title") ?: "ถึงเวลาทานยา"
        val requestId = intent.getIntExtra("requestId", 0)

        // เมื่อแตะการแจ้งเตือน → เปิดแอป
        val openIntent = Intent(ctx, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val contentPendingIntent = PendingIntent.getActivity(
            ctx,
            requestId,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // ใช้ไอคอนระบบถ้ายังไม่มี ic_stat_pill
        val smallIcon = try {
            R.drawable.ic_stat_pill
        } catch (_: Exception) {
            android.R.drawable.ic_popup_reminder
        }

        val notification = NotificationCompat.Builder(ctx, NotificationHelper.CHANNEL_ID_MED)
            .setSmallIcon(smallIcon)
            .setContentTitle("เตือนทานยา")
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(contentPendingIntent)
            .build()

        val nm = ctx.getSystemService(NotificationManager::class.java)
        nm.notify(requestId, notification)
    }
}
