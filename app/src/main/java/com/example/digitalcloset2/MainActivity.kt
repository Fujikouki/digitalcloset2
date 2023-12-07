package com.example.digitalcloset2

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.digitalcloset2.components.ClothsList
import com.example.digitalcloset2.components.Dialog
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

                    mainui()

                }
            }
        }
    }
}



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mainui(mainviemodel: mainviewmodel = hiltViewModel(),permission: Camera = Camera()){

    val camera = permission

    if(camera.PermissionTest()){
        val context = LocalContext.current
        camera.startCamera(context)
        Log.d("permission",camera.PermissionTest().toString())
    }else{
        Log.d("permission","使えない")
    }



    if (mainviemodel.flag){
        Dialog()
    }
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { mainviemodel.flag = true }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Clothesadd")
        }
    }) {
        val cloths by mainviemodel.cloths.collectAsState(initial = emptyList())
        val clothName by mainviemodel.clothsName.collectAsState(initial = emptyList())

        ClothsList(
            Cloths = cloths,
            onClickRow = { 
                mainviemodel.setEditing(it)
                mainviemodel.flag = true
                mainviemodel.isUpdate = true
                         },
            onClickDelete = { mainviemodel.deleteCloth(it)}
        )
    }
}



