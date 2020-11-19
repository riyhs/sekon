package com.sekon.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sekon.app.R
import com.sekon.app.model.jadwal.JadwalResponseDetail
import kotlinx.android.synthetic.main.card_jadwal.view.*

class JadwalAdapter(private val listJadwal: List<JadwalResponseDetail>) : RecyclerView.Adapter<JadwalAdapter.JadwalViewHolder>() {
    inner class JadwalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(jadwal: JadwalResponseDetail) {
            with(itemView) {
                tv_jadwal_jam.text = jadwal.jam
                tv_jadwal_mapel.text = jadwal.mapel
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JadwalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_jadwal, parent, false)
        return JadwalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JadwalViewHolder, position: Int) {
        holder.bind(listJadwal[position])
    }

    override fun getItemCount(): Int = listJadwal.size
}