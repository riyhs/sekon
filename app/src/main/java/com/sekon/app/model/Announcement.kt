package com.sekon.app.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Announcement(
    val title: String?,
    val desc: String?
) : Parcelable