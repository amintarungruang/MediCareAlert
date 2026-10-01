package com.example.medicarealert.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.medicarealert.R
import com.google.android.material.button.MaterialButton
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val swDark = view.findViewById<SwitchCompat>(R.id.swDark)
        val btnLogout = view.findViewById<MaterialButton>(R.id.btnLogout)
        val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        val isDark = prefs.getBoolean("dark_mode", false)
        swDark.isChecked = isDark

        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        swDark.setOnCheckedChangeListener { _, checked ->
            prefs.edit().putBoolean("dark_mode", checked).apply()

            val mode = if (checked)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO

            AppCompatDelegate.setDefaultNightMode(mode)

            requireActivity().recreate()
        }

        btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("ออกจากระบบ")
                .setMessage("ยืนยันออกจากระบบหรือไม่?")
                .setNegativeButton("ยกเลิก", null)
                .setPositiveButton("ออกจากระบบ") { _, _ ->
                    val session = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
                    session.edit().clear().apply()
                    findNavController().navigate(R.id.welcomeFragment)
                }
                .show()
        }
    }
}
