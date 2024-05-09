package com.example.digitalcloset2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.digitalcloset2.components.ClothesDetailScreen
import com.example.digitalcloset2.components.ClothesDialog
import com.example.digitalcloset2.components.ClothsList
import com.example.digitalcloset2.components.DeletingDialog
import com.example.digitalcloset2.components.PermissionTest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
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
        navController = navController, startDestination = ScreenRoute.MainScreen.root
    ) {
        composable(route = ScreenRoute.MainScreen.root) {
            MainUi(mainViewmodel = mainViewmodel, navController = navController, uiState = uiState)
        }
        composable(route = ScreenRoute.ShootingScreen.root) {
            Column(modifier = Modifier) {
                PermissionTest(mainViewmodel = mainViewmodel, navController = navController)
            }
        }
        composable(route = ScreenRoute.ClothDetailScreen.root) {
            ClothesDetailScreen(mainViewmodel = mainViewmodel)
        }
    }

}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainUi(mainViewmodel: MainViewmodel, uiState: MainUiState, navController: NavController) {


    if (uiState.clothesDialogFlag) {
        ClothesDialog(mainViewmodel = mainViewmodel, navController = navController)
    }

    if (uiState.deletingDialogFlag) {
        DeletingDialog(
            onDismiss = { mainViewmodel.changeDeletingDialogFlag(false); mainViewmodel.cleanDeleteDate() },
            onDelete = { mainViewmodel.deleteCloth() })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Digital Closet") }, actions = {
                    IconButton(onClick = { mainViewmodel.changeClothesDialogFlag(true) }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Clothes add")
                    }
                })
        }

    ) { paddingValues ->

        val cloths by mainViewmodel.cloths.collectAsState(initial = emptyList())

        ClothsList(
            modifier = Modifier.padding(paddingValues = paddingValues),
            cloths = cloths,
            onClickRow = {
                mainViewmodel.setEditing(it)
                navController.navigate(ScreenRoute.ClothDetailScreen.root)
            },
            onClickDelete = {
                mainViewmodel.setDeletingDate(it)
                mainViewmodel.changeDeletingDialogFlag(true)
            }
        )
    }
}


