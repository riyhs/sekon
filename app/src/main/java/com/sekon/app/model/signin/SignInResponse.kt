package com.sekon.app.model.signin

data class SignInResponse(
    val message: String,
    val siswa: Siswa,
    val status: String,
    val token: String
)