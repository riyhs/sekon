package com.sekon.app.model.saran

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Kelas(
    val _id: String,
    val kelas: String
) : Parcelable