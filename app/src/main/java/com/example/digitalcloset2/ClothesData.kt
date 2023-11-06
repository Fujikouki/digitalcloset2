package com.example.digitalcloset2


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ClothesData(
    @PrimaryKey(autoGenerate = true)val id:Int = 0,
    val ClothesName:String,
    val ClothesType:String,
    val ClothesColor: String,
    val ClothesScene:String,
    val ClothesImage:String,
)
