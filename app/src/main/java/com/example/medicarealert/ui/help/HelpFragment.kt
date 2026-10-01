package com.example.medicarealert.ui.help

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.example.medicarealert.R

class HelpFragment : Fragment(R.layout.fragment_help) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rv = view.findViewById<RecyclerView>(R.id.rvHelp)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = HelpAdapter(sample())
    }

    private fun sample() = listOf(
        "ทำไมแจ้งเตือนไม่ดัง?" to "ตรวจสิทธิ์การแจ้งเตือน, เปิด Exact Alarm (Android 12+), และปิดการประหยัดพลังงานสำหรับแอป",
        "เพิ่มยาอย่างไร?" to "ที่หน้า Medications กดปุ่ม + แล้วกรอกชื่อยา เวลากิน และบันทึก",
        "แชร์ให้ผู้ดูแลช่วยจัดการยา?" to "ไปที่ Manage → Medfriends → กด + เพื่อสร้างลิงก์เชิญ แล้วให้ผู้ดูแลกดยอมรับ",
        "ลืมรหัสผ่าน?" to "ในหน้าล็อกอินกด 'ลืมรหัสผ่าน' เพื่อรับลิงก์รีเซ็ตทางอีเมล",
        "เปลี่ยนธีมมืดได้ไหม?" to "ไปที่ Manage → Settings แล้วเปิด Dark theme"
    )

    class HelpAdapter(private val items: List<Pair<String,String>>) :
        RecyclerView.Adapter<HelpAdapter.VH>() {

        class VH(v: View): RecyclerView.ViewHolder(v) {
            val q: TextView = v.findViewById(R.id.tvQ)
            val a: TextView = v.findViewById(R.id.tvA)
        }

        override fun onCreateViewHolder(p: ViewGroup, vt: Int): VH {
            val v = LayoutInflater.from(p.context).inflate(R.layout.row_help, p, false)
            return VH(v)
        }

        override fun onBindViewHolder(h: VH, pos: Int) {
            val (qq, aa) = items[pos]
            h.q.text = qq
            h.a.text = aa
        }

        override fun getItemCount() = items.size
    }
}
