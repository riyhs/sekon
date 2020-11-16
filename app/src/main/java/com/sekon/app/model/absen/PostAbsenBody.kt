package com.sekon.app.model.absen

data class PostAbsenBody(
    val kelas: String,
    val nama: String,
    val deskripsi: String,
    val status: Int
)