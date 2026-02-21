package com.example.colegiomilitargo.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    suspend fun linkAccount(email: String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)

        auth.currentUser
            ?.linkWithCredential(credential)
            ?.await()
            ?: throw IllegalStateException("Usuário não autenticado")
    }
}