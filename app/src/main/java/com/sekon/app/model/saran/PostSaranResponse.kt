package com.sekon.app.model.saran

data class PostSaranResponse(
    val message: String,
    val result: PostSaranResponseDetail,
    val status: String
)