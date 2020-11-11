package com.sekon.app.model.announcement

data class AnnouncementPostResponse(
    val message: String,
    val result: AnnouncementResponseDetail,
    val status: String
)