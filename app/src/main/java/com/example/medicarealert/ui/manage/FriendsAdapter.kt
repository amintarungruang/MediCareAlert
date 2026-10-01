package com.example.medicarealert.ui.manage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.medicarealert.R
import com.example.medicarealert.data.manage.CareLink

class FriendsAdapter(
    private val onToggleManage: (CareLink, Boolean) -> Unit = { _, _ -> },
    private val onClick: (CareLink) -> Unit = { }
) : ListAdapter<CareLink, FriendsAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CareLink>() {
            override fun areItemsTheSame(old: CareLink, new: CareLink) =
                old.carelinkId == new.carelinkId

            override fun areContentsTheSame(old: CareLink, new: CareLink) = old == new
        }
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val root: View = itemView
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvSubtitle: TextView = itemView.findViewById(R.id.tvSubtitle)
        private val swManage: SwitchCompat = itemView.findViewById(R.id.switchManage)

        fun bind(item: CareLink, onToggle: (CareLink, Boolean) -> Unit, onClick: (CareLink) -> Unit) {
            tvTitle.text = "#${item.caregiverId} ↔ #${item.patientId}"
            tvSubtitle.text = "${item.relation} • ${item.status}"

            swManage.setOnCheckedChangeListener(null)
            swManage.isChecked = item.canManageMeds
            swManage.setOnCheckedChangeListener { _, checked ->
                onToggle(item, checked)
                root.setOnClickListener { onClick(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_friend, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position), onToggleManage, onClick)
    }
}
