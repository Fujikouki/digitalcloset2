package com.example.digitalcloset2

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

class Camera {

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun PermissionTest():Boolean{
        val permissionState: PermissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)

        return permissionState.status.isGranted
    }

    fun startCamera(context: Context) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            /*val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }*/

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                /*cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview)*/

            } catch(exc: Exception) {
                Log.e("エラー", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }







}