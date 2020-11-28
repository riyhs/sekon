package com.sekon.app.model.announcement

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnnouncementResponseDetail(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val deskripsi: String,
    val judul: String,
    val photo: String,
    val updatedAt: String
) : Parcelable