package com.example.medicarealert.ui.manage

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicarealert.R
import com.example.medicarealert.data.manage.CareLinkRepository
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController
import com.example.medicarealert.data.manage.CareLink
import com.example.medicarealert.ui.meds.MedicationsFragment


class FriendsListFragment : Fragment(R.layout.fragment_friends_list) {

    private lateinit var rvCaregivers: RecyclerView
    private lateinit var rvPatients: RecyclerView
    private lateinit var emptyCaregivers: TextView
    private lateinit var emptyPatients: TextView

    private val caregiversAdapter = FriendsAdapter(
        onToggleManage = { link, enabled ->  },
        onClick = {  }
    )

    private val patientsAdapter = FriendsAdapter(
        onToggleManage = { link, enabled -> /* ตามเดิม */ },
        onClick = { link ->

            val args = bundleOf(MedicationsFragment.ARG_OWNER_ID to link.patientId)
            findNavController().navigate(R.id.medicationsFragment, args)
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvCaregivers = view.findViewById(R.id.rvCaregivers)
        rvPatients = view.findViewById(R.id.rvPatients)
        emptyCaregivers = view.findViewById(R.id.tvEmptyCaregivers)
        emptyPatients = view.findViewById(R.id.tvEmptyPatients)

        rvCaregivers.layoutManager = LinearLayoutManager(requireContext())
        rvCaregivers.adapter = caregiversAdapter
        rvPatients.layoutManager = LinearLayoutManager(requireContext())
        rvPatients.adapter = patientsAdapter

        val repo = CareLinkRepository.getInstance(requireContext())
        val me = currentUserId()

        viewLifecycleOwner.lifecycleScope.launch {
            val caregivers = repo.caregiversOf(me)
            val patients = repo.patientsOf(me)

            caregiversAdapter.submitList(caregivers)
            patientsAdapter.submitList(patients)

            emptyCaregivers.visibility = if (caregivers.isEmpty()) View.VISIBLE else View.GONE
            emptyPatients.visibility = if (patients.isEmpty()) View.VISIBLE else View.GONE
        }


        view.findViewById<View>(R.id.fabInvite).setOnClickListener {
            findNavController().navigate(R.id.inviteMedfriendFragment)
        }
    }

    private fun currentUserId(): Long = 1L // TODO: ดึงจาก session จริง
}
