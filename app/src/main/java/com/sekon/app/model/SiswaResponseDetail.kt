package com.sekon.app.model

data class SiswaResponseDetail(
    val __v: Int,
    val _id: String,
    val kelas: String,
    val nama: String,
    val nis: Int,
    val password: String,
    val photo: String,
    val tagline: String,
    val saran_url: String,
    val absen_url: String,
    val pelajaran_url: String,
    val jadwal_url: String
)