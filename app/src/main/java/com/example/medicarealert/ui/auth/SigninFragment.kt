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

class SigninFragment : Fragment(R.layout.fragment_signin) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val edtUser = view.findViewById<TextInputEditText>(R.id.edtUsername)
        val edtPass = view.findViewById<TextInputEditText>(R.id.edtPassword)
        val btnLogin = view.findViewById<MaterialButton>(R.id.btnLogin)
        val btnGoSignup = view.findViewById<MaterialButton>(R.id.btnGoSignup)

        val repo = AuthRepository.getInstance(requireContext())

        btnLogin.setOnClickListener {
            val username = edtUser.text?.toString()?.trim().orEmpty()
            val password = edtPass.text?.toString()?.trim().orEmpty()

            if (username.isEmpty() || password.isEmpty()) {
                Snackbar.make(view, "กรอกชื่อผู้ใช้และรหัสผ่าน", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                val acc = repo.login(username, password)
                if (acc != null) {
                    // บันทึก session
                    val prefs = requireContext()
                        .getSharedPreferences("session", AppCompatActivity.MODE_PRIVATE)
                    prefs.edit()
                        .putLong("user_id", acc.userId)
                        .putString("full_name", acc.displayName)
                        .apply()

                    Snackbar.make(view, "เข้าสู่ระบบสำเร็จ", Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.homeFragment)
                } else {
                    Snackbar.make(view, "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        btnGoSignup.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }
    }
}
