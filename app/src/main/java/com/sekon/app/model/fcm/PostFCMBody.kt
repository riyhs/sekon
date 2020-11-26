package com.sekon.app.model.fcm

data class PostFCMBody(
    val notification: PostFCMBodyDetail,
    val to: String
)