package com.example.logincompose.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UserRoomEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,
    val nome: String,
    val email: String,
    val senha: String,
    val dataNascimento: String,
    val firebaseId: String? = null
)
