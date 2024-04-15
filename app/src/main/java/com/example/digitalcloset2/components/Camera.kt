package com.example.digitalcloset2.components

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.digitalcloset2.MainViewmodel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionTest(Mainviewmodel: MainViewmodel, navController: NavController) {
    val permissionState: PermissionState =
        rememberPermissionState(permission = android.Manifest.permission.CAMERA)
    MainContent(
        Mainviewmodel = Mainviewmodel,
        hasPermission = permissionState.status.isGranted,
        onRequestPermission = permissionState::launchPermissionRequest,
        navController = navController
    )
}

@Composable
fun MainContent(
    Mainviewmodel: MainViewmodel,
    navController: NavController,
    hasPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    if (hasPermission) {
        CameraStert(Mainviewmodel = Mainviewmodel, navController = navController)
    } else {
        Log.d("カメラ", onRequestPermission.toString())
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

@Composable
fun cameraScreen(Mainviewmodel: MainViewmodel): Triple<ImageCapture, Context, PreviewView> {
    val imageCapture = remember {
        ImageCapture.Builder()
            .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .build()
    }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember {
        LifecycleCameraController(context)
    }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val previewView = PreviewView(context).apply {
        //layoutParams = LinearLayout.LayoutParams(600,600)
        scaleType = PreviewView.ScaleType.FILL_CENTER
    }
    DisposableEffect(Unit) {
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build()

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(previewView.surfaceProvider)

        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
        Log.d("imageCapture6", imageCapture.toString())
        onDispose {
            cameraProvider.unbindAll()
            Log.d("imageCapture7", imageCapture.toString())
            Mainviewmodel.SucceededShooting = false
        }
    }
    return Triple(imageCapture, context, previewView)
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun CameraStert(Mainviewmodel: MainViewmodel, navController: NavController) {

    val dialogClothesUiState by Mainviewmodel.clothesDialog.collectAsState()

    val _cameraScreen = cameraScreen(Mainviewmodel = Mainviewmodel)
    val imageCapture = _cameraScreen.first
    val context = _cameraScreen.second
    val previewView = _cameraScreen.third

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    if (Mainviewmodel.SucceededShooting) {
                        Text(text = "もう一度撮影する")
                    } else {
                        Text(text = "写真を取る")
                    }
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "写真を取る"
                    )
                },
                onClick = {
                    if (Mainviewmodel.SucceededShooting) {
                        Mainviewmodel.SucceededShooting
                    } else {
                        Log.d("image", _cameraScreen.toString())
                        imageCapture?.let {
                            tackCatcher(
                                Mainviewmodel = Mainviewmodel,
                                context = context,
                                imageCapture = it
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            if (Mainviewmodel.SucceededShooting) {
                Column(modifier = Modifier) {
                    GlideImage(
                        model = Uri.parse(dialogClothesUiState.ClothesImage),
                        contentDescription = "撮影した写真"
                    )
                    Button(onClick = {
                        navController.popBackStack()
                    }) {
                        Text(text = "この写真を使う")
                    }
                }
            } else {

                Column(modifier = Modifier) {
                    AndroidView(
                        modifier = Modifier,
                        factory = {
                            previewView
                        },
                    )
                }
            }
        }
    }
}

fun tackCatcher(Mainviewmodel: MainViewmodel, context: Context, imageCapture: ImageCapture) {

    val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    val mainExecutorService = ContextCompat.getMainExecutor(context)
    val name = SimpleDateFormat(FILENAME_FORMAT, Locale.JAPAN)
        .format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
    }
    val outputFileOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()
    imageCapture.takePicture(
        outputFileOptions,
        mainExecutorService,
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exception: ImageCaptureException) {
                Mainviewmodel.SucceededShooting = false
                Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show()
                Log.d("captcher", exception.toString())
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Mainviewmodel.SucceededShooting = true
                val savedUri = outputFileResults.savedUri
                savedUri?.let { uri ->
                    Toast.makeText(context, "Image saved: $savedUri", Toast.LENGTH_SHORT).show()
                    Mainviewmodel.chengeClothesImage(uri.toString())
                }
            }
        })
}