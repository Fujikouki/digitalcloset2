package com.example.digitalcloset2.components

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.digitalcloset2.ClothesData

@Composable
fun ClothsList(
    Cloths:List<ClothesData>,
    onClickRow:(ClothesData) -> Unit,
    onClickDelete:(ClothesData) -> Unit,
){
    LazyColumn {
        items(Cloths) { Cloth ->
            ClothsRow(
                cloth = Cloth,
                onClickRow = onClickRow ,
                onClickDelete = onClickDelete)
        }
    }
}