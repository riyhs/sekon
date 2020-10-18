package com.sekon.app.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SiswaResponse(
    val name: String,
    val nis: String,
    val password: String
) : Parcelable