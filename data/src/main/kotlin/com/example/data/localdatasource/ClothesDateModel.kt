package com.example.data.localdatasource

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ClothesDateModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var category: String,
    var color: String,
    var size: String,
    var brand: String,
    var like: Boolean,
    var image: String,
)
