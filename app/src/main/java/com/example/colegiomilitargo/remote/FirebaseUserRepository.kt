package com.example.colegiomilitargo.remote

import com.example.colegiomilitargo.data.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("usuarios")

    suspend fun insertUser(user: UserModel): String {
        val doc = collection.add(user).await()
        return doc.id
    }

    suspend fun getUserByEmail(email: String): UserModel? {
        val query = collection.whereEqualTo("email", email).get().await()
        return query.documents.firstOrNull()?.toObject(UserModel::class.java)
    }
}
