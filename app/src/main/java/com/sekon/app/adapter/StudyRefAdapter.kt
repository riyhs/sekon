package com.sekon.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sekon.app.R
import com.sekon.app.model.StudyRef
import kotlinx.android.synthetic.main.card_study_ref.view.*

class StudyRefAdapter (private val studyRef: List<StudyRef>) : RecyclerView.Adapter<StudyRefAdapter.StudyRevViewHolder>() {
    inner class StudyRevViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: StudyRef) {
            with(itemView) {
                tv_study_ref_title.text = item.name
                tv_study_ref_desc.text = item.desc

                Glide.with(itemView.context)
                    .load("https://pemmzchannel.com/wp-content/uploads/2018/05/Google-IO-2018.png")
                    .centerCrop()
                    .into(iv_study_ref_card)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyRevViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_study_ref, parent, false)
        return StudyRevViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudyRevViewHolder, position: Int) {
        holder.bind(studyRef[position])
    }

    override fun getItemCount(): Int = studyRef.size
}