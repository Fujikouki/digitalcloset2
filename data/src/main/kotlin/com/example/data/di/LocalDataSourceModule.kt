package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.localdatasource.ClothesDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ClothesDataBase::class.java, "ClothesDatabase").build()

    @Provides
    fun provideDao(db: ClothesDataBase) = db.ClothesDao()
}
