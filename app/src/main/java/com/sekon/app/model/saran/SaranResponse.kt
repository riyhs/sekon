package com.sekon.app.model.saran

data class SaranResponse(
    val result: List<SaranResponseDetail>,
    val status: String
)