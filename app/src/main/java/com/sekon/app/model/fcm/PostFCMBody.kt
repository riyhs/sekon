package com.sekon.app.model.fcm

data class PostFCMBody(
    val data: PostFCMBodyDetail,
    val to: String
)