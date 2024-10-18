package com.example.domain.repository

import com.example.domain.model.clothes.ClothesData
import kotlinx.coroutines.flow.Flow


interface ClothesDataRepository {
    suspend fun insertClothesData(clothes: ClothesData)
    fun loadAllClothes(): Flow<List<ClothesData>>
    suspend fun updateClothes(clothes: ClothesData)
    suspend fun deleteClothes(clothes: ClothesData)
}
