package com.sekon.app.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sekon.app.AbsenActivity
import com.sekon.app.JadwalActivity
import com.sekon.app.MainActivity
import com.sekon.app.R
import com.sekon.app.model.Feature
import kotlinx.android.synthetic.main.card_feature.view.*

class FeatureAdapter (private val item: List<Feature>) : RecyclerView.Adapter<FeatureAdapter.FeatureViewHolder>() {
    inner class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item : Feature) {
            with(itemView) {
                tv_feature_title.text = item.title
                tv_feature_desc.text = item.desc

                val intent: Intent = when (item.id) {
                    0 -> Intent(context, AbsenActivity::class.java)
                    1 -> Intent(context, JadwalActivity::class.java)
                    else -> Intent(context, MainActivity::class.java)
                }

                itemView.setOnClickListener{
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_feature, parent, false)
        return FeatureViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        holder.bind(item[position])
    }

    override fun getItemCount(): Int = item.size
}