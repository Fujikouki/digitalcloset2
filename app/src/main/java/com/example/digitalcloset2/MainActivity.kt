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
import com.example.digitalcloset2.Camera.CameraManager
import com.example.digitalcloset2.components.ClothsList
import com.example.digitalcloset2.components.Dialog
import com.example.digitalcloset2.components.PermissionTest
import com.example.digitalcloset2.ui.theme.DigitalCloset2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val camera = CameraManager()
        setContent {
            DigitalCloset2Theme {
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
fun HostScreen(mainViewmodel: MainViewmodel = hiltViewModel()) {

    val navController = rememberNavController()

    val uiState by mainViewmodel.mainUiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = ScreenRoute.MainScreen.root
    ) {
        composable(route = ScreenRoute.MainScreen.root) {
            MainUi(mainViewmodel = mainViewmodel, navController = navController, uiState = uiState)
        }
        composable(route = ScreenRoute.ShootingScreen.root) {
            Column(modifier = Modifier) {
                PermissionTest(mainViewmodel = mainViewmodel, navController = navController)
            }
        }
    }

}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainUi(mainViewmodel: MainViewmodel, uiState: MainUiState, navController: NavController) {

    if (uiState.dialogFlag) {
        Dialog(mainViewmodel = mainViewmodel, navController = navController)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { mainViewmodel.chengeDialogFlag(true) })
            {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Clothes add")
            }
        }) {

        val cloths by mainViewmodel.cloths.collectAsState(initial = emptyList())

        ClothsList(
            Cloths = cloths,
            onClickRow = {
                mainViewmodel.setEditing(it)
                mainViewmodel.chengeDialogFlag(true)
            },
            onClickDelete = { mainViewmodel.deleteCloth(it) }
        )
    }
}


