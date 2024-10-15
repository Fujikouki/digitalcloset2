package com.example.data.localdatasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.domain.model.clothes.CardData
import kotlinx.coroutines.flow.Flow

@Dao
interface ClothesDao {
    @Insert
    suspend fun insertClothesData(clothes: ClothesDateModel)

    @Query("SELECT * FROM ClothesDateModel")
    fun loadAllClothes(): Flow<List<ClothesDateModel>>

    @Query("SELECT name,image FROM ClothesDateModel")
    fun loadNameClothes(): Flow<List<CardData>>

    @Update
    suspend fun updateClothes(clothes: ClothesDateModel)

    @Delete
    suspend fun deleteClothes(clothes: ClothesDateModel)

}
