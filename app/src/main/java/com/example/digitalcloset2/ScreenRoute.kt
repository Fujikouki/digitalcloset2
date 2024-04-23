package com.example.digitalcloset2

sealed class ScreenRoute(val root: String) {
    object MainScreen : ScreenRoute("main_ScreenRoute")
    object ShootingScreen : ScreenRoute("shooting_screen")
    object ClothDetailScreen : ScreenRoute("cloth_detail_screen")
}

