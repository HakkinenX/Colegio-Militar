package com.example.colegiomilitargo.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.example.colegiomilitargo.data.UserModel
import kotlinx.coroutines.tasks.await

// ✅ CORRIGIDO: Repositório seguro com Firebase Authentication
class FirebaseAuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("usuarios")

    // Cadastrar usuário com email e senha (seguro)
    suspend fun cadastrarUsuario(
        nome: String,
        email: String,
        senha: String,
        dataNascimento: String
    ): Result<UserModel> {
        return try {
            // 1. Criar usuário no Firebase Auth (senha fica segura lá)
            val authResult = auth.createUserWithEmailAndPassword(email, senha).await()
            val firebaseUser = authResult.user ?: throw Exception("Erro ao criar usuário")

            // 2. Criar modelo de dados SEM senha
            val user = UserModel(
                id = firebaseUser.uid,
                nome = nome,
                email = email,
                dataNascimento = dataNascimento,
                criadoEm = System.currentTimeMillis()
            )

            // 3. Salvar dados públicos no Firestore (SEM senha!)
            usersCollection.document(firebaseUser.uid)
                .set(user)
                .await()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Login seguro
    suspend fun fazerLogin(email: String, senha: String): Result<UserModel> {
        return try {
            // 1. Autenticar com Firebase Auth
            val authResult = auth.signInWithEmailAndPassword(email, senha).await()
            val firebaseUser = authResult.user ?: throw Exception("Usuário não encontrado")

            // 2. Buscar dados do usuário no Firestore
            val user = getUserById(firebaseUser.uid)
                ?: throw Exception("Dados do usuário não encontrados")

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Buscar usuário por ID
    suspend fun getUserById(userId: String): UserModel? {
        return try {
            val doc = usersCollection.document(userId).get().await()
            doc.toObject(UserModel::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // Buscar usuário por email (somente dados públicos)
    suspend fun getUserByEmail(email: String): UserModel? {
        return try {
            val query = usersCollection.whereEqualTo("email", email).get().await()
            query.documents.firstOrNull()?.toObject(UserModel::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // Logout
    fun logout() {
        auth.signOut()
    }

    // Usuário atualmente logado
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    // Verificar se está logado
    fun isUserLoggedIn(): Boolean = auth.currentUser != null
}