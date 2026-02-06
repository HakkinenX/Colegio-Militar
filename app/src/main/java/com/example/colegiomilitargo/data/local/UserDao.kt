package com.seuprojeto.colegiomilitargo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserRoomEntity): Long

    @Query("SELECT * FROM usuarios WHERE email = :email")
    suspend fun getByEmail(email: String): UserRoomEntity?
}
