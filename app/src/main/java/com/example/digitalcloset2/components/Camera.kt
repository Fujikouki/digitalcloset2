package com.example.digitalcloset2.components

import android.content.ContentValues
import android.content.Context

import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.Toast
import androidx.camera.core.CameraSelector

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.digitalcloset2.mainviewmodel

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionTest(Mainviewmodel:mainviewmodel){
    val permissionState: PermissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)
    MainContent(
        Mainviewmodel = Mainviewmodel,
        hasPermission = permissionState.status.isGranted,
        onRequestPermission = permissionState::launchPermissionRequest )
}

@Composable
fun MainContent(Mainviewmodel: mainviewmodel,hasPermission:Boolean,onRequestPermission:()-> Unit){
    if(hasPermission){
        CameraStert(Mainviewmodel = Mainviewmodel)
    }else{
        Log.d("カメラ",onRequestPermission.toString())
        NoPermission(onRequestPermission)
    }
}


@Composable
fun NoPermission(onRequestPermission: () -> Unit){
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Button(onClick =  onRequestPermission ) {
            Text(text = "カメラの許可を与えてください")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraStert(Mainviewmodel: mainviewmodel){
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember {
        LifecycleCameraController(context)
    }
    cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(text = "写真を取る")
                },
                icon = { Icon(imageVector = Icons.Default.Create, contentDescription = "写真を取る") },
                onClick = {
                    tackCatcher(
                        Mainviewmodel = Mainviewmodel,
                        context = context,
                        cameraController = cameraController
                    )
                }
            )
        }
    ) { paddingValues:PaddingValues ->
        AndroidView(
            modifier = Modifier.padding(paddingValues),
            factory = { context ->
                PreviewView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT)
                    scaleType = PreviewView.ScaleType.FILL_START
                }.also {previewView ->
                    previewView.controller = cameraController
                    cameraController.bindToLifecycle(lifecycleOwner)

                }
            })
    }
}

fun tackCatcher(Mainviewmodel: mainviewmodel,context: Context,cameraController: LifecycleCameraController){
    val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    val mainExecutorService = ContextCompat.getMainExecutor(context)
    val name = SimpleDateFormat(FILENAME_FORMAT, Locale.JAPAN)
        .format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }
    }
    val outputFileOptions = ImageCapture.OutputFileOptions
        .Builder(context.contentResolver,MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues).build()
    cameraController.takePicture(outputFileOptions,mainExecutorService,object :ImageCapture.OnImageSavedCallback{
        override fun onError(exception: ImageCaptureException) {
            Toast.makeText(context,exception.toString(),Toast.LENGTH_SHORT).show()
        }
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val savedUri = outputFileResults.savedUri
            savedUri?.let { uri ->
                Toast.makeText(context, "Image saved: $savedUri", Toast.LENGTH_SHORT).show()
                Mainviewmodel.ClothesImage = uri.toString()
            }
        }
    })
}