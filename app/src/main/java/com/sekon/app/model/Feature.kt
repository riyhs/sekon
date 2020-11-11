package com.sekon.app.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Feature(
    val id: Int,
    val title: String?,
    val desc: String?
) : Parcelable