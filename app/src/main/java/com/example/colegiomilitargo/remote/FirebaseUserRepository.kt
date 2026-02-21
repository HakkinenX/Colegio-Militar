package com.example.colegiomilitargo.remote

import com.example.colegiomilitargo.data.UserModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("usuarios")

    // =========================
    // CADASTRO
    // =========================
    suspend fun cadastrarUsuario(
        nome: String,
        email: String,
        senha: String,
        dataNascimento: String
    ): Result<UserModel> {
        return try {
            val authResult = auth
                .createUserWithEmailAndPassword(email, senha)
                .await()

            val firebaseUser = authResult.user
                ?: throw Exception("Falha ao obter usuário autenticado")

            val user = UserModel(
                id = firebaseUser.uid,
                nome = nome,
                email = email,
                dataNascimento = dataNascimento,
                criadoEm = System.currentTimeMillis()
            )

            usersCollection
                .document(firebaseUser.uid)
                .set(user)
                .await()

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // =========================
    // NOVO — LINKAR CONTA (anonima → email/senha)
    // =========================
    suspend fun linkAccount(email: String, senha: String) {
        val user = auth.currentUser
            ?: throw IllegalStateException("Usuário não autenticado")

        val credential = EmailAuthProvider.getCredential(email, senha)
        user.linkWithCredential(credential).await()
    }

    // =========================
    // LOGIN
    // =========================
    suspend fun fazerLogin(
        email: String,
        senha: String
    ): Result<UserModel> {
        return try {
            val authResult = auth
                .signInWithEmailAndPassword(email, senha)
                .await()

            val firebaseUser = authResult.user
                ?: throw Exception("Usuário não autenticado")

            val user = getUserById(firebaseUser.uid)
                ?: throw Exception("Documento do usuário não encontrado")

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    suspend fun getUserById(userId: String): UserModel? {
        return try {
            val snapshot = usersCollection
                .document(userId)
                .get()
                .await()

            if (!snapshot.exists()) null
            else snapshot.toObject(UserModel::class.java)

        } catch (e: Exception) {
            null
        }
    }

    // =========================
    // BUSCAR POR EMAIL
    // =========================
    suspend fun getUserByEmail(email: String): UserModel? {
        return try {
            val query = usersCollection
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .await()

            query.documents.firstOrNull()
                ?.toObject(UserModel::class.java)

        } catch (e: Exception) {
            null
        }
    }

    // =========================
    // AUTH HELPERS
    // =========================
    fun logout() {
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun isUserLoggedIn(): Boolean = auth.currentUser != null
}