package com.example.digitalcloset2


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ClothesData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var ClothesName: String = "",
    var ClothesType: String = "",
    var ClothesColor: String = "",
    var ClothesScene: String = "",
    var ClothesImage: String = "",
)
