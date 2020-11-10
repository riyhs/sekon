package com.sekon.app.model.announcement

data class AnnouncementResponse(
    val result: List<AnnouncementResponseDetail>,
    val status: String
)