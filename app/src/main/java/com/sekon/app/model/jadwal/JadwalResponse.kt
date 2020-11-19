package com.sekon.app.model.jadwal

data class JadwalResponse(
    val result: List<JadwalResponseDetail>,
    val status: String
)