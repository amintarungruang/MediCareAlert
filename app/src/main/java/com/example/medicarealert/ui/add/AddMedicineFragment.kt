package com.example.medicarealert.ui.add

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.medicarealert.R
import com.example.medicarealert.data.meds.Medication
import com.example.medicarealert.data.meds.MedicationRepository
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import com.example.medicarealert.ui.meds.AlarmHook   // ✅ ต้องอยู่บนสุด (import ต้องมาก่อน class)

private fun Long.asRequestId(): Int = (this xor (this ushr 32)).toInt()

class AddMedicineFragment : Fragment(R.layout.fragment_add_medicine) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = MedicationRepository.getInstance(requireContext())

        val etName = view.findViewById<TextInputEditText>(R.id.etName)
        val etDose = view.findViewById<TextInputEditText>(R.id.etDose)
        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
        val btnSave = view.findViewById<Button>(R.id.btnSave)

        val me = 1L // หรืออ่านจาก session จริง
        val ownerId = arguments?.getLong("ownerUserId", -1L)?.takeIf { it > 0 } ?: me

        btnSave.setOnClickListener {
            val name = etName.text?.toString()?.trim().orEmpty()
            val dose = etDose.text?.toString()?.trim().orEmpty()

            val (h, m) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker.hour to timePicker.minute
            } else {
                @Suppress("DEPRECATION")
                timePicker.currentHour to timePicker.currentMinute
            }

            if (name.isBlank()) {
                etName.error = "กรอกชื่อยา"
                return@setOnClickListener
            }

            val med = Medication(
                ownerUserId = ownerId,
                name = name,
                dose = dose.ifBlank { null },
                hour = h,
                minute = m
            )

            viewLifecycleOwner.lifecycleScope.launch {

                repo.upsert(med)

                val medId = med.id
                val requestId = medId.asRequestId()

                val title = if (dose.isBlank()) name else "$name ($dose)"
                AlarmHook.cancel(requireContext(), requestId)
                val ok = AlarmHook.scheduleDaily(requireContext(), requestId, h, m, title)

                if (!ok) {
                    Toast.makeText(
                        requireContext(),
                        "บันทึกแล้ว แต่ต้องอนุญาตการแจ้งเตือนก่อน",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "บันทึกยาและตั้งเตือนแล้ว",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                findNavController().popBackStack()
            }
        }
    }
}
