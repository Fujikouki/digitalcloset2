package com.example.domain.model.clothes

data class ClothesData(
    val id: Int = 0,
    var name: String = "未設定",
    var category: String = "未設定",
    var color: String = "未設定",
    var size: String = "未設定",
    var brand: String = "未設定",
    var like: Boolean = false,
    var image: String = "",
)
