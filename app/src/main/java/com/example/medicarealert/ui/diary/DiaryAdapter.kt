package com.example.medicarealert.ui.diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.medicarealert.R
import com.example.medicarealert.data.diary.DiaryEntry
import java.text.SimpleDateFormat
import java.util.*

class DiaryAdapter(
    private val onClick: (DiaryEntry) -> Unit
) : ListAdapter<DiaryEntry, DiaryAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<DiaryEntry>() {
            override fun areItemsTheSame(o: DiaryEntry, n: DiaryEntry) = o.id == n.id
            override fun areContentsTheSame(o: DiaryEntry, n: DiaryEntry) = o == n
        }
        private val df = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val d: TextView = v.findViewById(R.id.tvDay)
        val c: TextView = v.findViewById(R.id.tvContent)
    }

    override fun onCreateViewHolder(p: ViewGroup, vt: Int): VH {
        val v = LayoutInflater.from(p.context).inflate(R.layout.row_diary, p, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val item = getItem(pos)
        h.d.text = df.format(Date(item.day))
        h.c.text = item.content
        h.itemView.setOnClickListener { onClick(item) }
    }
}
