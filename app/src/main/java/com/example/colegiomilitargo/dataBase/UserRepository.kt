package com.example.logincompose.data

import android.content.Context
import com.example.colegiomilitargo.data.UserModel
import com.example.logincompose.data.local.AppDatabase
import com.example.logincompose.data.local.UserRoomEntity
import com.example.colegiomilitargo.remote.FirebaseUserRepository

class UserRepository(context: Context) {

    private val userDao = AppDatabase.getInstance(context).userDao()
    private val remoteRepository = FirebaseUserRepository()

    suspend fun cadastrar(
        user: UserModel,
        salvarLocal: Boolean = true,
        salvarRemoto: Boolean = false
    ) {
        if (salvarLocal) userDao.insert(user.toRoomEntity())
        if (salvarRemoto) remoteRepository.insertUser(user)
    }

    suspend fun getUsuarioPorEmail(
        email: String,
        fonte: FonteDados = FonteDados.LOCAL
    ): UserModel? {
        return when (fonte) {
            FonteDados.LOCAL -> userDao.getByEmail(email)?.toUser()
            FonteDados.REMOTO -> remoteRepository.getUserByEmail(email)
        }
    }
}

enum class FonteDados { LOCAL, REMOTO }

// Conversões
fun UserModel.toRoomEntity() = UserRoomEntity(
    nome = nome,
    email = email,
    senha = senha,
    dataNascimento = dataNascimento
)

fun UserRoomEntity.toUser() = UserModel(
    id = firebaseId ?: "",
    nome = nome,
    email = email,
    senha = senha,
    dataNascimento = dataNascimento
)
