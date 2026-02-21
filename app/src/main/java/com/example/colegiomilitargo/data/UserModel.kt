package com.example.colegiomilitargo.data

// ✅ CORRIGIDO: Senha removida (Firebase Auth cuida disso)
data class UserModel(
    val id: String = "",
    val nome: String = "",
    val email: String = "",
    val dataNascimento: String = "",
    val criadoEm: Long = System.currentTimeMillis(),
    val tipo: String = "", // aluno | professor | coordenador | diretor
)