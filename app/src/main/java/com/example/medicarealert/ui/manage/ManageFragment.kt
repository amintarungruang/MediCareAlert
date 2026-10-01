package com.example.medicarealert.ui.manage

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.medicarealert.R

class ManageFragment : Fragment(R.layout.fragment_manage) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnInvite = view.findViewById<Button>(R.id.btnInviteMedfriend)
        btnInvite.setOnClickListener {
            findNavController().navigate(R.id.friendsListFragment)
        }

        view.findViewById<View>(R.id.btnTrackers).setOnClickListener {
            findNavController().navigate(R.id.trackersFragment)
        }
        view.findViewById<View>(R.id.btnDiary).setOnClickListener {
            findNavController().navigate(R.id.diaryFragment)
        }
        view.findViewById<View>(R.id.btnSettings).setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }
        view.findViewById<View>(R.id.btnHelp).setOnClickListener {
            findNavController().navigate(R.id.helpFragment)
        }
    }
}
