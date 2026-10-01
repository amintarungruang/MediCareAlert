package com.example.medicarealert.ui.meds

import android.content.Context
import com.example.medicarealert.notif.MedicineScheduler

object AlarmHook {


    fun scheduleDaily(context: Context, requestId: Int, hour: Int, minute: Int, title: String): Boolean {
        val scheduler = MedicineScheduler(context)
        val ok = scheduler.scheduleDaily(requestId, hour, minute, title)
        if (!ok) {

            val now = System.currentTimeMillis()
            scheduler.scheduleWindow(requestId, now + 5 * 60_000, 10 * 60_000, title)
        }
        return ok
    }

    fun cancel(context: Context, requestId: Int) {
        MedicineScheduler(context).cancel(requestId)
    }
}
