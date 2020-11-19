package com.sekon.app.model.saran

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SaranResponseDetail(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val deskripsi: String,
    val kelas: Kelas,
    val nama: Nama,
    val photo: String,
    val saran: String,
    val updatedAt: String
) : Parcelable