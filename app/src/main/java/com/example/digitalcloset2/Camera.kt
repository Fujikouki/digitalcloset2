package com.example.digitalcloset2

import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.math.MathContext

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
        Scaffold(modifier = Modifier.fillMaxSize()) {paddingValues:PaddingValues ->
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



    @Preview
    @Composable
    fun test(){
        noPermission(onRequestPermission = {})
    }
    
}
