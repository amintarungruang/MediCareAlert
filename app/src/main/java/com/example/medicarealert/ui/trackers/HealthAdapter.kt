package com.example.medicarealert.ui.trackers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.medicarealert.R
import com.example.medicarealert.data.trackers.HealthRecord
import java.text.SimpleDateFormat
import java.util.*

class HealthAdapter(
    private val onClick: (HealthRecord) -> Unit
) : ListAdapter<HealthRecord, HealthAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<HealthRecord>() {
            override fun areItemsTheSame(o: HealthRecord, n: HealthRecord) = o.id == n.id
            override fun areContentsTheSame(o: HealthRecord, n: HealthRecord) = o == n
        }
        private val df = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val t1: TextView = v.findViewById(R.id.tvTitle)
        val t2: TextView = v.findViewById(R.id.tvSub)
    }

    override fun onCreateViewHolder(p: ViewGroup, vt: Int): VH {
        val v = LayoutInflater.from(p.context).inflate(R.layout.row_health_record, p, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val item = getItem(pos)

        h.t1.text = "${item.type}: ${item.value} ${item.unit ?: ""}".trim()
        h.t2.text = df.format(Date(item.recordedAt)) +
                (if (!item.note.isNullOrBlank()) " • ${item.note}" else "")

        h.itemView.setOnClickListener {
            onClick(item)
        }
    }
}
