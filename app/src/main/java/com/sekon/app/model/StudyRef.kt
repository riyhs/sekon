package com.sekon.app.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StudyRef(
    val name: String?,
    val desc: String?
) : Parcelable