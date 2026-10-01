package com.example.medicarealert.ui.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicarealert.R
import com.example.medicarealert.data.diary.DiaryEntry
import com.example.medicarealert.data.diary.DiaryRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

class DiaryFragment : Fragment(R.layout.fragment_diary) {

    private val repo by lazy { DiaryRepository.getInstance(requireContext()) }
    private lateinit var adapter: DiaryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvDiary)
        val fab = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAddDiary)

        adapter = DiaryAdapter { e -> editDiary(e) }
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repo.flowByUser(currentUserId()).collectLatest { adapter.submitList(it) }
        }

        fab.setOnClickListener { editDiary(null) }
    }

    private fun editDiary(existing: DiaryEntry?) {
        val v = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_diary, null, false)
        val et = v.findViewById<EditText>(R.id.etDiary)
        if (existing != null) et.setText(existing.content)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (existing == null) "บันทึกไดอารี่" else "แก้ไขไดอารี่")
            .setView(v)
            .setNegativeButton("ยกเลิก", null)
            .setPositiveButton("บันทึก") { _, _ ->
                val startOfDay = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                val e = DiaryEntry(
                    id = existing?.id ?: 0,
                    userId = currentUserId(),
                    day = existing?.day ?: startOfDay,
                    content = et.text.toString().trim()
                )
                viewLifecycleOwner.lifecycleScope.launch {
                    if (existing == null) repo.insert(e) else repo.update(e)
                }
            }
            .setNeutralButton(if (existing != null) "ลบ" else null) { _, _ ->
                existing?.let { viewLifecycleOwner.lifecycleScope.launch { repo.delete(it) } }
            }
            .show()
    }

    private fun currentUserId() = 1L
}
