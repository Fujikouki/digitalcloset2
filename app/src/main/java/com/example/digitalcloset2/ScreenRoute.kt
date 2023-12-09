package com.example.digitalcloset2

import java.lang.StringBuilder

sealed class ScreenRoute( val root:String){
    object MainScreen: ScreenRoute("main_ScreenRoute")
    object ShootingScreen:ScreenRoute("shooting_screen")
}

