package com.example.digitalcloset2.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.digitalcloset2.ClothesData


@Composable
fun ClothsList(
    modifier: Modifier,
    cloths: List<ClothesData>,
    onClickRow: (ClothesData) -> Unit,
    onClickDelete: (ClothesData) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
    ) {
        items(cloths) { cloth ->
            ClothCard(
                cloth = cloth, onClickRow = onClickRow, onClickDelete = onClickDelete
            )
        }
    }
}
