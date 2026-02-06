package com.example.colegiomilitargo.data

data class UserModel(
    val id: String = "",
    val nome: String,
    val email: String,
    val senha: String,
    val dataNascimento: String
)
