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

    private val _cameraUiState = MutableStateFlow(CameraUisate())
    val cameraUiState: StateFlow<CameraUisate> = _cameraUiState.asStateFlow()

    private val _deletingData = MutableStateFlow(ClothesData())


    fun changeClothesDialogFlag(flag: Boolean) {
        _mainUiState.value = _mainUiState.value.copy(clothesDialogFlag = flag)
    }

    fun changeDeletingDialogFlag(flag: Boolean) {
        _mainUiState.value = _mainUiState.value.copy(deletingDialogFlag = flag)
    }

    fun changeName(name: String) {
        _clothesDialog.value = _clothesDialog.value.copy(name = name)
    }


    fun changeColor(color: String) {
        _clothesDialog.value = _clothesDialog.value.copy(color = color)
    }

    fun changeCategory(category: String) {
        _clothesDialog.value = _clothesDialog.value.copy(category = category)
    }

    fun changeSize(size: String) {
        _clothesDialog.value = _clothesDialog.value.copy(size = size)
    }

    fun changeBrand(brand: String) {
        _clothesDialog.value = _clothesDialog.value.copy(brand = brand)
    }

    fun changeLike(like: Boolean) {
        _clothesDialog.value = _clothesDialog.value.copy(like = like)
    }


    fun categoryImage(image: String) {
        _clothesDialog.value = _clothesDialog.value.copy(image = image)
    }

    fun categoryUpdata(flag: Boolean) {
        _mainUiState.value = _mainUiState.value.copy(isUpdata = flag)
    }

    fun categoryOnCamera(flag: Boolean) {
        _mainUiState.value = _mainUiState.value.copy(onCamera = flag)
    }

    fun categorySucceededShooting() {

        _cameraUiState.value = _cameraUiState.value.copy(
            succeededShooting = true,
            imagePath = _clothesDialog.value.image
        )

    }


    fun clearImage() {
        _cameraUiState.value = _cameraUiState.value.copy(
            succeededShooting = false, imagePath = ""
        )
    }


    var clothesImage: String by mutableStateOf("")
        private set

    private var editingClothe: ClothesData? by mutableStateOf(null)

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

    fun deleteCloth() {
        viewModelScope.launch {
            clothesDao.deleteClothes(_deletingData.value)
        }
    }

    fun cleanDeleteDate() {
        _deletingData.value = ClothesData()
    }

    fun setDeletingDate(cloth: ClothesData) {
        _deletingData.value = cloth
    }

    fun setEditing(cloth: ClothesData) {
        editingClothe = cloth
        _clothesDialog.value = _clothesDialog.value.copy(
            name = cloth.name,
            category = cloth.category,
            color = cloth.color,
            size = cloth.size,
            brand = cloth.brand,
            like = cloth.like,
            image = cloth.image,
        )
    }

    fun updateCloth() {
        editingClothe?.let { cloth ->
            viewModelScope.launch {
                cloth.name = clothesDialog.value.name
                cloth.category = clothesDialog.value.category
                cloth.color = clothesDialog.value.color
                cloth.size = clothesDialog.value.size
                cloth.brand = clothesDialog.value.brand
                cloth.like = clothesDialog.value.like
                cloth.image = clothesImage
                clothesDao.updateClothes(clothes = cloth)
            }
        }
    }

    fun resetCloth() {
        editingClothe = null
        _clothesDialog.value = _clothesDialog.value.copy(
            name = "",
            category = "",
            color = "",
            size = "",
            brand = "",
            like = false,
            image = ""
        )

    }
}

data class MainUiState(
    val clothesDialogFlag: Boolean = false,
    val isUpdata: Boolean = false,
    val onCamera: Boolean = false,
    val deletingDialogFlag: Boolean = false
)

data class CameraUisate(
    val succeededShooting: Boolean = false, val imagePath: String = ""
)
