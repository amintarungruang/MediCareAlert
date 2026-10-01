package com.example.medicarealert.ui.home

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.medicarealert.R
import com.example.medicarealert.data.meds.Medication
import com.example.medicarealert.data.meds.MedicationRepository
import com.example.medicarealert.ui.manage.currentUserId
import com.example.medicarealert.ui.meds.MedicationsFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import android.content.Context

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var latestMeds: List<Medication> = emptyList()
    private var selectedY = -1
    private var selectedM = -1
    private var selectedD = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        val name = prefs.getString("full_name", "ผู้ใช้") ?: "ผู้ใช้"

        val tvGreeting = view.findViewById<TextView>(R.id.tvGreeting)
        tvGreeting.text = "สวัสดี, $name"

        val repo = MedicationRepository.getInstance(requireContext())
        val txtCount = view.findViewById<TextView>(R.id.txtMedCount)
        val txtNextName = view.findViewById<TextView>(R.id.txtNextName)
        val txtNextTime = view.findViewById<TextView>(R.id.txtNextTime)
        val btnAdd = view.findViewById<MaterialButton>(R.id.btnAddMed)
        val btnSeeAll = view.findViewById<MaterialButton>(R.id.btnSeeAll)
        val calendar = view.findViewById<CalendarView>(R.id.calendarView)

        val me = currentUserId()
        val ownerId = arguments?.getLong(MedicationsFragment.ARG_OWNER_ID, -1L)
            ?.takeIf { it > 0 } ?: me

        Calendar.getInstance().apply {
            selectedY = get(Calendar.YEAR)
            selectedM = get(Calendar.MONTH)
            selectedD = get(Calendar.DAY_OF_MONTH)
        }

        calendar.setOnDateChangeListener { _, y, m, d ->
            selectedY = y
            selectedM = m
            selectedD = d
            renderNextForSelectedDate(
                meds = latestMeds,
                y = selectedY, m = selectedM, d = selectedD,
                txtTitle = txtCount, txtLine1 = txtNextName, txtLine2 = txtNextTime
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repo.getAllOf(ownerId).collectLatest { meds: List<Medication> ->
                latestMeds = meds
                renderNextForSelectedDate(
                    meds = meds,
                    y = selectedY, m = selectedM, d = selectedD,
                    txtTitle = txtCount, txtLine1 = txtNextName, txtLine2 = txtNextTime
                )
            }
        }

        btnSeeAll.setOnClickListener { findNavController().navigate(R.id.action_home_to_meds) }
        btnAdd.setOnClickListener { findNavController().navigate(R.id.action_home_to_add) }
    }

    private fun renderNextForSelectedDate(
        meds: List<Medication>,
        y: Int, m: Int, d: Int,
        txtTitle: TextView, txtLine1: TextView, txtLine2: TextView
    ) {
        val localeTH = Locale("th", "TH")

        val cDay = Calendar.getInstance().apply {
            set(y, m, d, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val dateText = SimpleDateFormat("d MMMM yyyy", localeTH).format(cDay.time)
        txtTitle.text = "ยาวันที่ $dateText"

        if (meds.isEmpty()) {
            txtLine1.text = "ไม่มีรายการยา"
            txtLine2.text = "—"
            return
        }

        val dayStart = cDay.timeInMillis
        val dayEnd = dayStart + 24L * 60 * 60 * 1000


        val listForDay: List<Pair<Medication, Long>> = meds.map { med ->
            med to timeAtDateMillis(dayStart, med.hour, med.minute)
        }.sortedBy { it.second }

        if (listForDay.isEmpty()) {
            txtLine1.text = "ไม่มีรายการยา"
            txtLine2.text = "—"
            return
        }

        val now = System.currentTimeMillis()
        val isToday = now in dayStart until dayEnd


        val pick = if (isToday) {
            listForDay.firstOrNull { it.second >= now } ?: listForDay.last()
        } else {
            listForDay.first()
        }

        val (med, tMillis) = pick
        val c = Calendar.getInstance().apply { timeInMillis = tMillis }
        val hh = c.get(Calendar.HOUR_OF_DAY)
        val mm = c.get(Calendar.MINUTE)

        txtLine1.text = "ยาที่ต้องกินถัดไป"
        txtLine2.text = String.format(localeTH, "%s • %02d:%02d", med.name, hh, mm)
    }

    private fun timeAtDateMillis(dayStart: Long, h: Int, m: Int): Long {
        val c = Calendar.getInstance().apply { timeInMillis = dayStart }
        c.set(Calendar.HOUR_OF_DAY, h)
        c.set(Calendar.MINUTE, m)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        return c.timeInMillis
    }
}
