package com.sekon.app.model.saran

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Nama(
    val _id: String,
    val nama: String
) : Parcelable