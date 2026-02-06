package com.seuprojeto.colegiomilitargo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UserRoomEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,
    val firebaseId: String = "",
    val nome: String = "",
    val email: String = "",
    val dataNascimento: String = "",
    val criadoEm: Long = System.currentTimeMillis()
)
