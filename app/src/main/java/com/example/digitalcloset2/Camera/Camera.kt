package com.example.digitalcloset2.Camera

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.digitalcloset2.MainViewmodel
import java.util.Locale


class CameraManager() {

    private var imageCapture: ImageCapture? = ImageCapture.Builder()
        .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
        .setTargetAspectRatio(AspectRatio.RATIO_4_3)
        .build()

    fun bootCamera(context: Context, lifecycleOwner: LifecycleOwner): PreviewView {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        val previewView = PreviewView(context).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }

        val cameraProvider = cameraProviderFuture.get()


        val preview = Preview.Builder().build()

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        cameraProvider.unbindAll()
        preview.setSurfaceProvider(previewView.surfaceProvider)

        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
        return previewView
    }

    //cameraControllerを使う場合
    fun starCamera(context: Context, lifecycleOwner: LifecycleOwner): LifecycleCameraController {
        val cameraController = LifecycleCameraController(context)
        Log.d("CameraManager", context.toString())
        cameraController.bindToLifecycle(lifecycleOwner)
        cameraController.setCameraSelector(CameraSelector.DEFAULT_BACK_CAMERA)
        return cameraController
    }

    fun takePhoto(context: Context, mainViewmodel: MainViewmodel) {

        val imageCapture = imageCapture ?: return

        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.JAPAN)
            .format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ).build()

        try {
            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                        Log.e(TAG, "保存失敗")
                        mainViewmodel.clearImage()
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val msg = "Photo capture succeeded: ${output.savedUri}"
                        mainViewmodel.categoryImage(output.savedUri.toString())
                        mainViewmodel.categorySucceededShooting()
                        Log.d(TAG, msg)
                    }
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Photo capture failed: ${e.message}")
        }
    }


    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
            }.toTypedArray()
    }
}

