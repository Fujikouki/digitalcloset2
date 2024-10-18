package com.example.domain.usecase

import com.example.domain.repository.ClothesDataRepository

class GetAllClothesUseCase(
    private val clothesDataRepository: ClothesDataRepository
) {
    operator fun invoke() = clothesDataRepository.loadAllClothes()
}
