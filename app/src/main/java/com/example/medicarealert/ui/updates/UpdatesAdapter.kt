package com.example.medicarealert.ui.updates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.medicarealert.R
import com.example.medicarealert.data.logs.DoseLog
import java.text.SimpleDateFormat
import java.util.*

class UpdatesAdapter :
    ListAdapter<DoseLog, UpdatesAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<DoseLog>() {
            override fun areItemsTheSame(old: DoseLog, new: DoseLog) = old.logId == new.logId
            override fun areContentsTheSame(old: DoseLog, new: DoseLog) = old == new
        }

        private val fmtDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        private val fmtTime = SimpleDateFormat("HH:mm", Locale.getDefault())
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
        val txtStatus: TextView = view.findViewById(R.id.txtStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_update, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val item = getItem(pos)

        h.txtTitle.text = item.medName ?: "ไม่ทราบชื่อยา"
        h.txtTime.text = fmtTime.format(Date(item.timestamp))
        h.txtStatus.text = when (item.status.lowercase(Locale.ROOT)) {
            "taken", "success" -> "กินแล้ว"
            "missed" -> "พลาด"
            else -> item.status
        }
    }
}
