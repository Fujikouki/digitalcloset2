package com.example.digitalcloset2


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class mainviewmodel @Inject constructor(private val clothesDao: ClothesDao):ViewModel() {

    var flag by mutableStateOf(false)

    var ClothesName:String by mutableStateOf("")
    var ClothesType:String by mutableStateOf("")
    var ClothesColor: String by mutableStateOf("")
    var ClothesScene:String by mutableStateOf("")
    var ClothesImage:String by mutableStateOf("")

    var isUpdate:Boolean = false

    var editingClothe:ClothesData? = null
    val isEditing:Boolean
        get() = editingClothe != null

    val cloths = clothesDao.loadAllClothes().distinctUntilChanged()
    val clothsName = clothesDao.loadNameClothes().distinctUntilChanged()


    fun createCloth(){
        viewModelScope.launch {
            val newClothe = ClothesData(
                ClothesName = ClothesName,
                ClothesType = ClothesType,
                ClothesColor = ClothesColor,
                ClothesScene = ClothesScene,
                ClothesImage = ClothesImage)
            clothesDao.insertClothesData(newClothe)
            Log.d("main","success newCloth")
        }
    }

    fun deleteCloth(cloth:ClothesData){
        viewModelScope.launch {
            clothesDao.deleteClothes(cloth)
        }
    }

    fun setEditing(cloth:ClothesData){
        editingClothe = cloth
        ClothesName = cloth.ClothesName
        ClothesType = cloth.ClothesType
        ClothesColor = cloth.ClothesColor
        ClothesScene = cloth.ClothesScene
        ClothesImage = cloth.ClothesImage

    }

    fun updateCloth(){
        editingClothe?.let { cloth ->
            viewModelScope.launch {
                cloth.ClothesName = ClothesName
                cloth.ClothesType = ClothesType
                cloth.ClothesColor = ClothesColor
                cloth.ClothesScene = ClothesScene
                cloth.ClothesImage = ClothesImage
                clothesDao.updateClothes(clothes = cloth)
            }
        }
    }
}
