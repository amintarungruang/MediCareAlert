package com.example.medicarealert.ui.manage

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.medicarealert.R
import com.example.medicarealert.data.invite.Invite
import com.example.medicarealert.data.invite.InviteRepository
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch

class InviteMedfriendFragment : Fragment(R.layout.fragment_invite_medfriend) {

    private lateinit var repo: InviteRepository
    private var lastUri: Uri? = null
    private var lastCode: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repo = InviteRepository.getInstance(requireContext())

        val chipRole = view.findViewById<ChipGroup>(R.id.chipRole)
        val btnCreate = view.findViewById<Button>(R.id.btnCreate)
        val tvCode = view.findViewById<TextView>(R.id.tvCode)
        val btnCopy = view.findViewById<ImageButton>(R.id.btnCopy)
        val btnShare = view.findViewById<ImageButton>(R.id.btnShare)
        val ivQr = view.findViewById<ImageView>(R.id.ivQr)

        btnCreate.setOnClickListener {
            val role = when (chipRole.checkedChipId) {
                R.id.chipCaregiver -> "caregiver"
                else -> "friend"
            }
            val code = genInviteCode()
            val now = System.currentTimeMillis()
            val invite = Invite(
                inviterUserId = currentUserId(),
                code = code,
                role = role,
                createdAt = now,
                expiresAt = now + 48 * 60 * 60 * 1000
            )

            viewLifecycleOwner.lifecycleScope.launch {
                repo.insert(invite)
                val uri = Uri.parse("medicare://invite")
                    .buildUpon()
                    .appendQueryParameter("code", code)
                    .appendQueryParameter("role", role)
                    .build()

                lastCode = code
                lastUri = uri
                tvCode.text = code
                ivQr.setImageBitmap(makeQr(uri.toString()))
                Toast.makeText(requireContext(), "สร้างลิงก์เรียบร้อย", Toast.LENGTH_SHORT).show()
            }
        }

        btnCopy.setOnClickListener {
            lastUri?.let { uri ->
                val cm = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.setPrimaryClip(ClipData.newPlainText("invite", uri.toString()))
                Toast.makeText(requireContext(), "คัดลอกลิงก์แล้ว", Toast.LENGTH_SHORT).show()
            }
        }

        btnShare.setOnClickListener {
            lastUri?.let { uri ->
                val share = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "เข้าร่วมดูแลกันใน MediCareAlert: $uri")
                }
                startActivity(Intent.createChooser(share, "Share invite"))
            }
        }
    }
}
