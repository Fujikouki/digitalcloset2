package com.example.data.localdatasource

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ClothesDateModel::class], version = 1, exportSchema = false)
abstract class ClothesDataBase : RoomDatabase() {
    abstract fun ClothesDao(): ClothesDao
}
