package com.sekon.app.model.saran

data class SaranResponse(
    val post: List<SaranResponseDetail>,
    val status: String
)