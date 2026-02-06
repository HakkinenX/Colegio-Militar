package com.example.logincompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.logincompose.data.local.UserRoomEntity

// Configuração do Room Database usando KSP
@Database(
    entities = [UserRoomEntity::class],
    version = 1,
    exportSchema = false // Evita warnings do KSP sobre exportação de schema
)
abstract class AppDatabase : RoomDatabase() {

    // DAO que vai gerenciar os usuários
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: android.content.Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db"
                ).build().also { INSTANCE = it }
            }
    }
}
