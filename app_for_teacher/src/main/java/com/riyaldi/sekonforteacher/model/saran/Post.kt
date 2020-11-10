package com.riyaldi.sekonforteacher.model.saran

data class Post(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val deskripsi: String,
    val kelas: Kelas,
    val nama: Nama,
    val photo: String,
    val saran: String,
    val updatedAt: String
)