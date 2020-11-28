package com.sekon.app.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sekon.app.R
import com.sekon.app.model.announcement.AnnouncementResponseDetail
import com.sekon.app.ui.activity.AnnouncementDetailActivity
import kotlinx.android.synthetic.main.card_feature.view.*

class AnnouncementAdapter(private val announcement: List<AnnouncementResponseDetail>?) : RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>() {
    inner class AnnouncementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: AnnouncementResponseDetail) {
            with(itemView) {
                tv_feature_title.text = item.judul
                tv_feature_desc.text = item.deskripsi

                Glide
                    .with(itemView.context)
                    .load(item.photo)
                    .into(iv_feature_card)

                itemView.setOnClickListener {
                    val intent = Intent(context, AnnouncementDetailActivity::class.java)
                    intent.putExtra("announcement", item)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_feature, parent, false)
        return AnnouncementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        holder.bind(announcement!![position])
    }

    override fun getItemCount(): Int = announcement?.size!!
}