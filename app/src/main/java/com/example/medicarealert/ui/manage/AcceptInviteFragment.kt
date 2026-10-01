package com.example.medicarealert.ui.manage

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.medicarealert.R
import com.example.medicarealert.data.invite.InviteRepository
import com.example.medicarealert.data.manage.CareLinkRepository
import kotlinx.coroutines.launch

class AcceptInviteFragment : Fragment(R.layout.fragment_accept_invite) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val code = requireActivity().intent?.data?.getQueryParameter("code")
        val role = requireActivity().intent?.data?.getQueryParameter("role") ?: "friend"

        val tvInfo = view.findViewById<TextView>(R.id.tvInfo)
        val btnAccept = view.findViewById<Button>(R.id.btnAccept)
        val btnDecline = view.findViewById<Button>(R.id.btnDecline)

        val inviteRepo = InviteRepository.getInstance(requireContext())
        val careRepo = CareLinkRepository.getInstance(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            inviteRepo.expireOld(System.currentTimeMillis())
            val invite = code?.let { inviteRepo.findByCode(it) }

            if (invite == null || invite.status != "active" || invite.expiresAt <= System.currentTimeMillis()) {
                tvInfo.text = "ลิงก์ไม่ถูกต้องหรือหมดอายุ"
                btnAccept.isEnabled = false
                return@launch
            }

            // แสดงข้อความในหน้ารับคำเชิญ
            tvInfo.text = "คำเชิญจากผู้ใช้ #${invite.inviterUserId} เป็น $role"

            btnAccept.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    val me = currentUserId()

                    val caregiverId = if (role == "caregiver") me else invite.inviterUserId
                    val patientId   = if (role == "caregiver") invite.inviterUserId else me

                    careRepo.insertLink(
                        caregiverId = caregiverId,
                        patientId = patientId,
                        relation = role,
                        status = "ใช้งาน"
                    )

                    inviteRepo.markUsed(invite.inviteId)
                    Toast.makeText(requireContext(), "เชื่อมต่อสำเร็จ!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.manageFragment)
                }
            }

            btnDecline.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun currentUserId(): Long = 1L // TODO: ดึงจาก session จริง
}
