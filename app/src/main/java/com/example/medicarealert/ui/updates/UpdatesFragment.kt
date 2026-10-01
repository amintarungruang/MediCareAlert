package com.example.medicarealert.ui.updates

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicarealert.R
import com.example.medicarealert.data.meds.MedicationRepository
import com.example.medicarealert.databinding.FragmentUpdatesBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UpdatesFragment : Fragment(R.layout.fragment_updates) {

    private var _vb: FragmentUpdatesBinding? = null
    private val vb get() = _vb!!

    private lateinit var adapter: UpdatesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _vb = FragmentUpdatesBinding.bind(view)

        adapter = UpdatesAdapter()

        vb.rvUpdates.layoutManager = LinearLayoutManager(requireContext())
        vb.rvUpdates.adapter = adapter

        val repo = MedicationRepository.getInstance(requireContext())

        val me = currentUserId()
        val ownerId = arguments?.getLong(
            com.example.medicarealert.ui.meds.MedicationsFragment.ARG_OWNER_ID, -1L
        )?.takeIf { it > 0 } ?: me

        val today = java.time.LocalDate.now()
        val zone = java.time.ZoneId.systemDefault()
        val startMillis = today.atStartOfDay(zone).toInstant().toEpochMilli()
        val endMillis   = today.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

        viewLifecycleOwner.lifecycleScope.launch {
            repo.observeLogsByDay(ownerId, startMillis, endMillis).collectLatest { logs ->
                adapter.submitList(logs)
                vb.emptyView.visibility = if (logs.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun currentUserId(): Long = 1L // TODO: ต่อ session จริง

    override fun onDestroyView() {
        if (this::adapter.isInitialized) vb.rvUpdates.adapter = null
        _vb = null
        super.onDestroyView()
    }
}
