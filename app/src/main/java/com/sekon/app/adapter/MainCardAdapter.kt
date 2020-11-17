package com.sekon.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sekon.app.R
import com.sekon.app.model.reference.ReferenceResponseItem
import kotlinx.android.synthetic.main.card_study_ref.view.*

class MainCardAdapter(private val studyRef: List<ReferenceResponseItem>) : RecyclerView.Adapter<MainCardAdapter.StudyRevViewHolder>() {
    inner class StudyRevViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ReferenceResponseItem) {
            with(itemView) {
                tv_study_ref_title.text = item.judul
                tv_study_ref_desc.text = item.deskripsi

                Glide.with(itemView.context)
                    .load(item.photo)
                    .centerCrop()
                    .into(iv_study_ref_card)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyRevViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.card_study_ref,
            parent,
            false
        )

        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.width = (parent.width * 0.8).toInt()
        view.layoutParams = layoutParams


        return StudyRevViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudyRevViewHolder, position: Int) {
        holder.bind(studyRef[position])
    }

    override fun getItemCount(): Int = studyRef.size
}