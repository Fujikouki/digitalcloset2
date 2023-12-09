package com.example.digitalcloset2

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

class Cameraviewmodel:ViewModel() {

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun cameraPermission(){

        val permissionState: PermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
        var hasPermission = permissionState.status.isGranted


    }

}