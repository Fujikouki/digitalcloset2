package com.example.domain.usecase

import com.example.domain.model.clothes.ClothesData
import com.example.domain.repository.ClothesDataRepository

class UpdateClothesUseCase(
    private val clothesDataRepository: ClothesDataRepository
) {
    suspend operator fun invoke(
        clothesData: ClothesData
    ) {
        clothesDataRepository.updateClothes(
            clothesData
        )
    }
}
