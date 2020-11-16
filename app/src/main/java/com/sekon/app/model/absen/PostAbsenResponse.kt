package com.sekon.app.model.absen

data class PostAbsenResponse(
    val message: String,
    val result: PostAbsenResponseDetail,
    val status: String
)