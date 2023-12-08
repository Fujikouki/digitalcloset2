package com.example.digitalcloset2

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class CapturedImageState(initialBitmap: Bitmap? = null) {
    lateinit var value: Bitmap
    private val _bitmap = mutableStateOf(initialBitmap)
    val bitmap: State<Bitmap?> = _bitmap

    fun updateBitmap(newBitmap: Bitmap?) {
        _bitmap.value = newBitmap
    }
}
