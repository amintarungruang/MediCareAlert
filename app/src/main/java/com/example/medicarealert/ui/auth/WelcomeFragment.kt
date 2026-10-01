package com.example.medicarealert.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.medicarealert.R
import com.example.medicarealert.data.session.SessionManager
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSignup = view.findViewById<MaterialButton>(R.id.btnSignup)
        val btnSignin = view.findViewById<MaterialButton>(R.id.btnSignin)
        val btnGuest  = view.findViewById<MaterialButton>(R.id.btnGuest)

        btnSignup.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }
        btnSignin.setOnClickListener {
            findNavController().navigate(R.id.signinFragment)
        }
        btnGuest.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                SessionManager.getInstance(requireContext()).setGuest()
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }
}
