package com.example.digitalcloset2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.digitalcloset2.components.ClothsList
import com.example.digitalcloset2.components.Dialog
import com.example.digitalcloset2.components.PermissionTest
import com.example.digitalcloset2.ui.theme.DigitalCloset2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DigitalCloset2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HostScreen()
                }
            }
        }
    }
}


@Composable
fun HostScreen(Mainviewmodel: mainviewmodel = hiltViewModel()){

    val navController = rememberNavController()

    NavHost(navController = navController ,
        startDestination =ScreenRoute.MainScreen.root ){

        composable(route = ScreenRoute.MainScreen.root){
            Mainui(Mainviewmodel = Mainviewmodel,navController = navController)
        }
        composable(route = ScreenRoute.ShootingScreen.root){
            Column(modifier = Modifier) {
                PermissionTest(Mainviewmodel = Mainviewmodel, navController = navController)
            }
        }
    }

}





@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mainui(Mainviewmodel: mainviewmodel,navController: NavController){

    if (Mainviewmodel.DialogFlag){
        Dialog(Mainviewmodel = Mainviewmodel,navController = navController )
    }
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { Mainviewmodel.DialogFlag = true }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Clothesadd")
        }
    }) {

        val cloths by Mainviewmodel.cloths.collectAsState(initial = emptyList())

        ClothsList(
            Cloths = cloths,
            onClickRow = { 
                Mainviewmodel.setEditing(it)
                Mainviewmodel.DialogFlag = true
                         },
            onClickDelete = { Mainviewmodel.deleteCloth(it)}
        )
    }
}



