package com.example.data.di

import com.example.data.localdatasource.ClothesDataRepositoryImpl
import com.example.domain.repository.ClothesDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class ClothesDataRepositoryModule {
    @Binds
    abstract fun bindClothesDataRepository(clothesDataRepositoryImpl: ClothesDataRepositoryImpl): ClothesDataRepository
}
