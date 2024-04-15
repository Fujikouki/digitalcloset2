package com.example.digitalcloset2


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewmodel @Inject constructor(private val clothesDao: ClothesDao) : ViewModel() {

    private val _mainUiState = MutableStateFlow(MainUiState())

    val mainUiState: StateFlow<MainUiState> = _mainUiState.asStateFlow()

    private val _clothesDialog = MutableStateFlow(ClothesData())
    val clothesDialog: StateFlow<ClothesData> = _clothesDialog.asStateFlow()


    fun chengeDialogFlag(flag: Boolean) {
        _mainUiState.value = _mainUiState.value.copy(dialogFlag = flag)
    }

    fun chengeClothesName(name: String) {
        _clothesDialog.value = _clothesDialog.value.copy(ClothesName = name)
    }

    fun chengeClothesType(type: String) {
        _clothesDialog.value = _clothesDialog.value.copy(ClothesType = type)
    }

    fun chengeClothesColor(color: String) {
        _clothesDialog.value = _clothesDialog.value.copy(ClothesColor = color)
    }

    fun chengeClothesScene(scene: String) {
        _clothesDialog.value = _clothesDialog.value.copy(ClothesScene = scene)
    }

    fun chengeClothesImage(image: String) {
        _clothesDialog.value = _clothesDialog.value.copy(ClothesImage = image)
    }

    fun chengeUpdata(flag: Boolean) {
        _mainUiState.value = _mainUiState.value.copy(isUpdata = flag)
    }

    fun chengeOnCamera(flag: Boolean) {
        _mainUiState.value = _mainUiState.value.copy(onCamera = flag)
    }


    var SucceededShooting by mutableStateOf(false)

    var ClothesImage: String by mutableStateOf("")

    var editingClothe: ClothesData? by mutableStateOf(null)

    val isEditing: Boolean
        get() = editingClothe != null

    val cloths = clothesDao.loadAllClothes().distinctUntilChanged()


    fun createCloth() {
        viewModelScope.launch {
            val newClothe = clothesDialog.value
            clothesDao.insertClothesData(newClothe)
            Log.d("main", "success newCloth")
        }
    }

    fun deleteCloth(cloth: ClothesData) {
        viewModelScope.launch {
            clothesDao.deleteClothes(cloth)
        }
    }

    fun setEditing(cloth: ClothesData) {
        editingClothe = cloth
        _clothesDialog.value = _clothesDialog.value.copy(
            ClothesName = cloth.ClothesName,
            ClothesType = cloth.ClothesType,
            ClothesColor = cloth.ClothesColor,
            ClothesScene = cloth.ClothesScene,
            ClothesImage = cloth.ClothesImage
        )
    }

    fun updateCloth() {
        editingClothe?.let { cloth ->
            viewModelScope.launch {
                cloth.ClothesName = _clothesDialog.value.ClothesName
                cloth.ClothesType = _clothesDialog.value.ClothesType
                cloth.ClothesColor = _clothesDialog.value.ClothesColor
                cloth.ClothesScene = _clothesDialog.value.ClothesScene
                cloth.ClothesImage = ClothesImage
                clothesDao.updateClothes(clothes = cloth)
            }
        }
    }

    fun resetCloth() {
        editingClothe = null
        _clothesDialog.value = _clothesDialog.value.copy(
            ClothesName = "",
            ClothesType = "",
            ClothesColor = "",
            ClothesScene = "",
            ClothesImage = ""
        )

    }
}

data class DialogUisate(
    var ClothesName: String = "",
    var ClothesType: String = "",
    var ClothesColor: String = "",
    var ClothesScene: String = "",
    var ClothesImage: String = ""
)

data class MainUiState(
    val dialogFlag: Boolean = false,
    val isUpdata: Boolean = false,
    val onCamera: Boolean = false,
)
