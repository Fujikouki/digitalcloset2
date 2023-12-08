package com.example.digitalcloset2

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale


class Camera {


    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun PermissionTest(){

        val permissionState: PermissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)
        MainContent(
            hasPermission = permissionState.status.isGranted,
            onRequestPermission = permissionState::launchPermissionRequest )
    }

    @Composable
    fun MainContent(hasPermission:Boolean,onRequestPermission:()-> Unit){
        if(hasPermission){
            CameraStert()
        }else{
            Log.d("カメラ",onRequestPermission.toString())
            noPermission(onRequestPermission)
        }
    }


    @Composable
    fun noPermission(onRequestPermission: () -> Unit){
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
            Text(text = "カメラをONにしてください")
            Button(onClick =  onRequestPermission ) {
                Text(text = "0N")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CameraStert(){
        val context = LocalContext.current
        val lifecycleOwer = LocalLifecycleOwner.current
        val cameraController = remember {
            LifecycleCameraController(context)
        }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = {
                           Text(text = "写真を取る")
                    },
                    icon = { Icon(imageVector = Icons.Default.Create, contentDescription = "写真を取る") },
                    onClick = {
                        val mainExecutorService = ContextCompat.getMainExecutor(context)
                        cameraController.takePicture(mainExecutorService, object : ImageCapture.OnImageCapturedCallback(){
                            override fun onCaptureSuccess(image: ImageProxy) {

                                val resolver = context.contentResolver

                                val contentValues = ContentValues().apply {
                                    put(MediaStore.Images.Media.DISPLAY_NAME, "photo.jpg")
                                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                                    // 必要に応じて、他のメタデータを追加できます
                                }

                                val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                                val buffer = image.planes[0].buffer
                                val bytes = ByteArray(buffer.remaining())
                                buffer.get(bytes)

                                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                                Log.d("image34",image.toString())

                                imageUri?.let { uri ->
                                    resolver.openOutputStream(uri)?.use { outputStream ->
                                        // bitmapはあなたが保存したい画像です
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                                    }
                                }

                                image.close()

                            }
                        })
                    })
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
                    cameraController.bindToLifecycle(lifecycleOwer)

                    }
            })
        }
    }

    fun saveIamge(){

       val  imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()

    }



    @Preview
    @Composable
    fun test(){
        noPermission(onRequestPermission = {})
    }
    @Preview
    @Composable
    fun test1(){
        CameraStert()
    }
}



