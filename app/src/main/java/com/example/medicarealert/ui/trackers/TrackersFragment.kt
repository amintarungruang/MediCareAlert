package com.example.medicarealert.ui.trackers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicarealert.R
import com.example.medicarealert.data.trackers.HealthRecord
import com.example.medicarealert.data.trackers.HealthRecordRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TrackersFragment : Fragment(R.layout.fragment_trackers) {

    private val repo by lazy { HealthRecordRepository.getInstance(requireContext()) }
    private lateinit var adapter: HealthAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvRecords)
        val fab = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAddRecord)

        adapter = HealthAdapter { rec -> showEditDialog(rec) }
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repo.flowByUser(currentUserId()).collectLatest { adapter.submitList(it) }
        }

        fab.setOnClickListener { showEditDialog(null) }
    }

    private fun showEditDialog(existing: HealthRecord?) {
        val v = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_health_record, null, false)
        val etType = v.findViewById<EditText>(R.id.etType)
        val etValue = v.findViewById<EditText>(R.id.etValue)
        val etUnit = v.findViewById<EditText>(R.id.etUnit)
        val etNote = v.findViewById<EditText>(R.id.etNote)

        if (existing != null) {
            etType.setText(existing.type)
            etValue.setText(existing.value)
            etUnit.setText(existing.unit ?: "")
            etNote.setText(existing.note ?: "")
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (existing == null) "เพิ่มบันทึกสุขภาพ" else "แก้ไขบันทึกสุขภาพ")
            .setView(v)
            .setNegativeButton("ยกเลิก", null)
            .setPositiveButton("บันทึก") { _, _ ->
                val now = System.currentTimeMillis()

                val typeStr  = etType.text?.toString()?.trim()
                val valueStr = etValue.text?.toString()?.trim()
                val unitStr  = etUnit.text?.toString()?.trim()
                val noteStr  = etNote.text?.toString()?.trim()

                val type  = if (typeStr.isNullOrBlank()) "General" else typeStr
                val value = valueStr.orEmpty()
                val unit  = unitStr?.takeIf { it.isNotEmpty() }
                val note  = noteStr?.takeIf { it.isNotEmpty() }

                val rec = HealthRecord(
                    id         = existing?.id ?: 0,
                    userId     = currentUserId(),
                    recordedAt = existing?.recordedAt ?: now,
                    type       = type,
                    value      = value,
                    unit       = unit,
                    note       = note
                )

                viewLifecycleOwner.lifecycleScope.launch {
                    if (existing == null) repo.insert(rec) else repo.update(rec)
                }
            }
            .setNeutralButton(if (existing != null) "ลบ" else null) { _, _ ->
                existing?.let {
                    viewLifecycleOwner.lifecycleScope.launch { repo.delete(it) }
                }
            }
            .show()
    }

    private fun currentUserId() = 1L
}
