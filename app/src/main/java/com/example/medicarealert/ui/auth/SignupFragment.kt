package com.example.medicarealert.ui.auth

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.medicarealert.R
import com.example.medicarealert.data.auth.AuthRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class SignupFragment : Fragment(R.layout.fragment_signup) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val edtName = view.findViewById<TextInputEditText>(R.id.edtDisplayName)
        val edtUser = view.findViewById<TextInputEditText>(R.id.edtUsername)
        val edtPass = view.findViewById<TextInputEditText>(R.id.edtPassword)
        val btnCreate = view.findViewById<MaterialButton>(R.id.btnCreate)
        val btnGoSignin = view.findViewById<MaterialButton>(R.id.btnGoSignin)

        val repo = AuthRepository.getInstance(requireContext())

        btnCreate.setOnClickListener {
            val name = edtName.text?.toString()?.trim().orEmpty()
            val user = edtUser.text?.toString()?.trim().orEmpty()
            val pass = edtPass.text?.toString()?.trim().orEmpty()

            if (name.isEmpty() || user.isEmpty() || pass.length < 6) {
                Snackbar.make(view, "กรอกข้อมูลให้ครบ (รหัสผ่านอย่างน้อย 6 ตัว)", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val id = repo.register(name, user, pass)

                    val prefs = requireContext()
                        .getSharedPreferences("session", AppCompatActivity.MODE_PRIVATE)
                    prefs.edit()
                        .putLong("user_id", id)
                        .putString("full_name", name)
                        .apply()

                    Snackbar.make(view, "สมัครสำเร็จ", Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.homeFragment)
                } catch (e: Exception) {
                    Snackbar.make(view, e.message ?: "สมัครไม่สำเร็จ", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        btnGoSignin.setOnClickListener {
            findNavController().navigate(R.id.signinFragment)
        }
    }
}
