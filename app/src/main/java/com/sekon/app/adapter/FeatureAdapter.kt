package com.sekon.app.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sekon.app.*
import com.sekon.app.model.Feature
import com.sekon.app.ui.activity.AbsenActivity
import com.sekon.app.ui.activity.JadwalActivity
import com.sekon.app.ui.activity.MainActivity
import com.sekon.app.ui.activity.SaranActivity
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
                    2 -> Intent(context, SaranActivity::class.java)
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