package com.example.appvendas2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appvendas2.data.dao.ClientDao
import com.example.appvendas2.data.dao.OrderDao
import com.example.appvendas2.data.dao.UserDAO
import com.example.appvendas2.data.models.*

@Database(entities = [Item::class, OrderEntity::class, Client::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDAO
    abstract fun orderDao(): OrderDao
    abstract fun clientDao(): ClientDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "App-database"
        ).build()
    }
}