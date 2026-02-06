package com.example.colegiomilitargo.data.local

data class UserModel(
    val id: String = "",
    val nome: String = "",
    val email: String = "",
    val dataNascimento: String = "",
    val criadoEm: Long = System.currentTimeMillis()
)