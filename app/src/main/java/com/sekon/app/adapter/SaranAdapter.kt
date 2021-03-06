package com.sekon.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sekon.app.R
import com.sekon.app.model.saran.SaranResponseDetail
import com.sekon.app.ui.fragment.dashboard.DashboardSaranFragmentDirections
import com.sekon.app.ui.fragment.saran.SaranFragmentDirections
import kotlinx.android.synthetic.main.card_feature.view.*

class SaranAdapter(private val saran: List<SaranResponseDetail>, private val isTeacher: Boolean) : RecyclerView.Adapter<SaranAdapter.SaranViewHolder>() {
    inner class SaranViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(saran: SaranResponseDetail) {
            with(itemView) {
                tv_feature_title.text = saran.saran
                tv_feature_desc.text = saran.deskripsi

                if (saran.photo != "" && saran.photo.isNotEmpty()) {
                    Glide
                        .with(itemView.context)
                        .load(saran.photo)
                        .into(iv_feature_card)
                }

                itemView.setOnClickListener {
                    if (isTeacher) {
                        val action = DashboardSaranFragmentDirections.actionDashboardSaranFragmentToSaranClickedFragment2(saran)
                        itemView.findNavController().navigate(action)
                    } else {
                        val action = SaranFragmentDirections.actionSaranFragmentToSaranClickedFragment(saran)
                        itemView.findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaranViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_feature, parent, false)
        return SaranViewHolder(view)
    }

    override fun onBindViewHolder(holder: SaranViewHolder, position: Int) {
        holder.bind(saran[position])
    }

    override fun getItemCount(): Int = saran.size
}