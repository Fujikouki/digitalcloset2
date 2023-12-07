package com.example.digitalcloset2

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.util.jar.Manifest

class Camera {

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun PermissionTest():Boolean{
        val permissionState: PermissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)

        return permissionState.status.isGranted
    }







}