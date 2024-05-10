package com.example.digitalcloset2.components

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.digitalcloset2.Camera.CameraManager
import com.example.digitalcloset2.MainViewmodel
import com.example.digitalcloset2.R
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

    val cameraUisate by mainViewmodel.cameraUiState.collectAsState()


    if (hasPermission) {
        if (cameraUisate.succeededShooting) {
            PhotoConfirmationScreen(mainViewmodel = mainViewmodel, navController = navController)
        } else {
            ShootingScreen(mainViewmodel = mainViewmodel, navController = navController)
        }
    } else {
        NoPermission(onRequestPermission, navController)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoPermission(onRequestPermission: () -> Unit, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.takePhoto)) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                        }
                    }
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Button(
                    onClick = onRequestPermission
                ) {
                    Text(text = "カメラの許可を与えてください")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShootingScreen(mainViewmodel: MainViewmodel, navController: NavController) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraManager = remember { CameraManager() }

    val previewView = remember {
        cameraManager.bootCamera(context = context, lifecycleOwner = lifecycleOwner)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.takePhoto)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { cameraManager.takePhoto(context, mainViewmodel) }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                    contentDescription = "camera"
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(4f / 3f)
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoConfirmationScreen(mainViewmodel: MainViewmodel, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val uiState by mainViewmodel.cameraUiState.collectAsState()

        Text(text = "写真を撮りました")
        GlideImage(model = Uri.parse(uiState.imagePath), contentDescription = "服の写真")
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "保存")
        }
        Button(onClick = { mainViewmodel.clearImage() }) {
            Text(text = "再撮影")
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            mainViewmodel.clearImage()
        }
    }
}
