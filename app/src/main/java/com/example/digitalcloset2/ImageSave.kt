package com.example.digitalcloset2

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat

@Composable
fun ImageSave(){
    val context = LocalContext.current
    val cameraController = remember {
        LifecycleCameraController(context)
    }
    val mainExecutorService = ContextCompat.getMainExecutor(context)
    cameraController.takePicture(mainExecutorService, object : ImageCapture.OnImageCapturedCallback(){
        override fun onCaptureSuccess(image: ImageProxy) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "photo.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")

            }
            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            val buffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            Log.d("image34",image.toString())
            imageUri?.let { uri ->
                resolver.openOutputStream(uri)?.use { outputStream ->

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
            }
            image.close()
        }
    })
}