package com.example.digitalcloset2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClothesDao {
    @Insert
    suspend fun insertClothesData(clothes: ClothesData)

    @Query("SELECT * FROM ClothesData")
    fun loadAllClothes(): Flow<List<ClothesData>>

    @Query("SELECT name,image FROM ClothesData")
    fun loadNameClothes(): Flow<List<CardData>>

    @Update
    suspend fun updateClothes(clothes: ClothesData)

    @Delete
    suspend fun deleteClothes(clothes: ClothesData)


}