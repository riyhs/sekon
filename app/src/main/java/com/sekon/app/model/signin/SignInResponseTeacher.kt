package com.sekon.app.model.signin

data class SignInResponseTeacher(
    val guru: Guru,
    val message: String,
    val status: String,
    val token: String
)