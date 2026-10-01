package com.example.medicarealert.ui.meds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.medicarealert.R
import com.example.medicarealert.data.meds.Medication
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import java.util.Locale

class MedAdapter(
    private val onTaken: (Medication) -> Unit,
    private val onEdit: (Medication) -> Unit,
    private val onDelete: (Medication) -> Unit
) : ListAdapter<Medication, MedAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Medication>() {
            override fun areItemsTheSame(old: Medication, new: Medication) = old.id == new.id
            override fun areContentsTheSame(old: Medication, new: Medication) = old == new
        }
    }

    private val takenKeys = mutableSetOf<String>()

    private fun keyOf(m: Medication): String = m.id.toString()

    fun setItems(items: List<Medication>) {
        submitList(items)
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tvName)
        private val tvDose = itemView.findViewById<TextView>(R.id.tvDose)
        private val chipTime = itemView.findViewById<Chip>(R.id.chipTime)
        private val btnTaken = itemView.findViewById<MaterialButton>(R.id.btnTaken)
        private val btnMore  = itemView.findViewById<ImageButton>(R.id.btnMore)

        fun bind(m: Medication) {
            tvName.text = m.name
            tvDose.text = m.dose ?: ""
            chipTime.text = String.format(Locale.getDefault(), "%02d:%02d", m.hour, m.minute)

            val key = keyOf(m)
            applyTakenState(btnTaken, key in takenKeys)

            btnTaken.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "บันทึกว่ากินยา ${m.name} แล้ว ✅",
                    Toast.LENGTH_SHORT
                ).show()

                takenKeys.add(key)
                applyTakenState(btnTaken, true)

                onTaken(m)
            }

            // เมนูแก้ไข/ลบ
            btnMore.setOnClickListener { v ->
                PopupMenu(v.context, v).apply {
                    menu.add(0, 1, 0, "แก้ไข")
                    menu.add(0, 2, 1, "ลบ")
                    setOnMenuItemClickListener { mi ->
                        when (mi.itemId) {
                            1 -> onEdit(m)
                            2 -> onDelete(m)
                        }
                        true
                    }
                }.show()
            }
        }

        private fun applyTakenState(button: MaterialButton, taken: Boolean) {
            if (taken) {
                button.isEnabled = false
                button.alpha = 0.6f
                button.text = "กินแล้ว ✓"
            } else {
                button.isEnabled = true
                button.alpha = 1f
                button.text = "กินแล้ว"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_medication, parent, false)   // ⇦ ให้ตรงกับ layout ของแถว
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}
