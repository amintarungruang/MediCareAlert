package com.example.medicarealert.ui.meds

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicarealert.R
import com.example.medicarealert.data.meds.MedicationRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MedicationsFragment : Fragment(R.layout.fragment_medications) {

    companion object { const val ARG_OWNER_ID = "ownerUserId" }

    private lateinit var adapter: MedAdapter
    private var ownerUserId: Long = -1L
    private var canManage: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val me = currentUserId()

        ownerUserId = arguments?.getLong(ARG_OWNER_ID, -1L) ?: -1L
        if (ownerUserId < 0) ownerUserId = me


        val isOwner = (ownerUserId == me)
        canManage = isOwner

        val repo = MedicationRepository.getInstance(requireContext())
        val rv  = view.findViewById<RecyclerView>(R.id.rvMeds)
        val fab = view.findViewById<FloatingActionButton>(R.id.fabAdd)

        fab.visibility = if (canManage) View.VISIBLE else View.GONE
        fab.setOnClickListener {
            if (!canManage) {
                Toast.makeText(requireContext(), "โหมดอ่านอย่างเดียว", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val args = bundleOf(ARG_OWNER_ID to ownerUserId)
            findNavController().navigate(
                R.id.action_medicationsFragment_to_addMedicineFragment, args
            )
        }

        adapter = MedAdapter(
            onTaken = { med ->
                if (!canManage) {
                    Toast.makeText(requireContext(), "อ่านอย่างเดียว — ยังไม่มีสิทธิ์จัดการยา", Toast.LENGTH_SHORT).show()
                    return@MedAdapter
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    repo.markTaken(ownerUserId, med)
                    Snackbar.make(view, "บันทึกว่า “กินแล้ว”", Snackbar.LENGTH_SHORT).show()
                }
            },
            onEdit = { med ->
                if (!canManage) {
                    Toast.makeText(requireContext(), "อ่านอย่างเดียว — ยังไม่มีสิทธิ์แก้ไข", Toast.LENGTH_SHORT).show()
                    return@MedAdapter
                }
                val args = bundleOf("medId" to med.id, ARG_OWNER_ID to ownerUserId)
                findNavController().navigate(
                    R.id.action_medicationsFragment_to_addMedicineFragment, args
                )
            },
            onDelete = { med ->
                if (!canManage) {
                    Toast.makeText(requireContext(), "อ่านอย่างเดียว — ยังไม่มีสิทธิ์ลบ", Toast.LENGTH_SHORT).show()
                    return@MedAdapter
                }
                // TODO: dialog ยืนยันลบ (ใช้ของเดิมที่เคยทำ)
                viewLifecycleOwner.lifecycleScope.launch {
                    repo.delete(med)
                    Snackbar.make(view, "ลบแล้ว", Snackbar.LENGTH_SHORT).show()
                }
            }
        )

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repo.getAllOf(ownerUserId).collectLatest { meds ->
                adapter.submitList(meds)
            }
        }
    }

    private fun currentUserId(): Long = 1L
}
