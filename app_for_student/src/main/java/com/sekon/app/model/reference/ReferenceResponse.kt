package com.sekon.app.model.reference

data class ReferenceResponse(
    val result: List<ReferenceResponseItem>,
    val status: String
)