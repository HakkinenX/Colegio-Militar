package com.example.colegiomilitargo.data.local

import android.content.Context
import com.example.colegiomilitargo.data.local.UserModel
import com.seuprojeto.colegiomilitargo.data.local.AppDatabase
import com.seuprojeto.colegiomilitargo.data.local.UserRoomEntity

class UserRepository(context: Context) {

    private val userDao = AppDatabase.getInstance(context).userDao()

    // Salvar usuário localmente
    suspend fun salvarLocal(user: UserModel) {
        userDao.insert(user.toRoomEntity())
    }

    // Buscar usuário por email localmente
    suspend fun buscarPorEmailLocal(email: String): UserModel? {
        return userDao.getByEmail(email)?.toUserModel()
    }
}

// Extensões de conversão
fun UserModel.toRoomEntity() = UserRoomEntity(
    firebaseId = id,
    nome = nome,
    email = email,
    dataNascimento = dataNascimento,
    criadoEm = criadoEm
)

fun UserRoomEntity.toUserModel() = UserModel(
    id = firebaseId,
    nome = nome,
    email = email,
    dataNascimento = dataNascimento,
    criadoEm = criadoEm
)
