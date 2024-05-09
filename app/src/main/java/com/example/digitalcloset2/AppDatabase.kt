package com.example.digitalcloset2

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ClothesData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ClothesDao(): ClothesDao

}