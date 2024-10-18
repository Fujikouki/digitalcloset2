package com.example.digitalcloset2.di

import com.example.domain.repository.ClothesDataRepository
import com.example.domain.usecase.DeleteClothesUseCase
import com.example.domain.usecase.GetAllClothesUseCase
import com.example.domain.usecase.InsertClothesUseCase
import com.example.domain.usecase.UpdateClothesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideInsertClothesUseCase(
        clothesDataRepository: ClothesDataRepository
    ) = InsertClothesUseCase(clothesDataRepository)

    @Provides
    fun provideDeleteClothesUseCase(
        clothesDataRepository: ClothesDataRepository
    ) = DeleteClothesUseCase(clothesDataRepository)

    @Provides
    fun provideGetAllClothesUseCase(
        clothesDataRepository: ClothesDataRepository
    ) = GetAllClothesUseCase(clothesDataRepository)

    @Provides
    fun provideUpdateClothesUseCase(
        clothesDataRepository: ClothesDataRepository
    ) = UpdateClothesUseCase(clothesDataRepository)

}

