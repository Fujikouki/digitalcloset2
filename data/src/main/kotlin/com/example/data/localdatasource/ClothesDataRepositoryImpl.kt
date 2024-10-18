package com.example.data.localdatasource

import com.example.domain.model.clothes.ClothesData
import com.example.domain.repository.ClothesDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClothesDataRepositoryImpl @Inject constructor(
    private val dao: ClothesDao
) : ClothesDataRepository {
    override suspend fun insertClothesData(clothes: ClothesData) {
        dao.insertClothesData(
            ClothesDateModel(
                name = clothes.name,
                category = clothes.category,
                color = clothes.color,
                size = clothes.size,
                brand = clothes.brand,
                like = clothes.like,
                image = clothes.image
            )
        )
    }

    override fun loadAllClothes(): Flow<List<ClothesData>> {
        return dao.loadAllClothes()
    }

    override suspend fun updateClothes(clothes: ClothesData) {
        dao.updateClothes(
            ClothesDateModel(
                id = clothes.id,
                name = clothes.name,
                category = clothes.category,
                color = clothes.color,
                size = clothes.size,
                brand = clothes.brand,
                like = clothes.like,
                image = clothes.image
            )
        )
    }

    override suspend fun deleteClothes(clothes: ClothesData) {
        dao.deleteClothes(
            ClothesDateModel(
                id = clothes.id,
                name = clothes.name,
                category = clothes.category,
                color = clothes.color,
                size = clothes.size,
                brand = clothes.brand,
                like = clothes.like,
                image = clothes.image
            )
        )
    }
}
