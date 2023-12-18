package com.tzh.database_module

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tzh.database_module.data.dao.TestDao
import com.tzh.database_module.data.entity.TestEntity

@Database(
    entities = [TestEntity::class], version = 1, exportSchema = true
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun appDao(): TestDao


    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(applicationContext: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    applicationContext, AppDataBase::class.java, applicationContext.packageName
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}