package com.example.digitalcloset2.components

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.digitalcloset2.Camera.CameraManager
import com.example.digitalcloset2.MainViewmodel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionTest(mainViewmodel: MainViewmodel, navController: NavController) {

    val permissionState: PermissionState =
        rememberPermissionState(permission = android.Manifest.permission.CAMERA)

    MainContent(
        mainViewmodel = mainViewmodel,
        hasPermission = permissionState.status.isGranted,
        onRequestPermission = permissionState::launchPermissionRequest,
        navController = navController
    )
}

@Composable
fun MainContent(
    mainViewmodel: MainViewmodel,
    navController: NavController,
    hasPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    if (hasPermission) {
        if (mainViewmodel.succeededShooting) {
            PhotoConfirmationScreen(mainViewmodel = mainViewmodel)
        } else {
            TestScreen(mainViewmodel = mainViewmodel)
        }
    } else {
        NoPermission(onRequestPermission)
    }
}


@Composable
fun NoPermission(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Button(onClick = onRequestPermission) {
            Text(text = "カメラの許可を与えてください")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(mainViewmodel: MainViewmodel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val camera = remember {
        CameraManager()
    }

    val previewView = camera.bootCamera(context = context, lifecycleOwner = lifecycleOwner)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { camera.takePhoto(context, mainViewmodel) })
            {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Clothes add")
            }
        }) { innerPadding ->
        AndroidView(factory = { previewView }, modifier = Modifier.padding(innerPadding))
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoConfirmationScreen(mainViewmodel: MainViewmodel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Text(text = "写真を撮りました")
        GlideImage(model = Uri.parse(mainViewmodel.clothesImage), contentDescription = "服の写真")
    }
}



