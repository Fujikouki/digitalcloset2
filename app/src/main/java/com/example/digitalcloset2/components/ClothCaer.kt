package com.example.digitalcloset2.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.digitalcloset2.ClothesData
import com.example.digitalcloset2.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ClothsRow(
    cloth:ClothesData,
    onClickRow:(ClothesData) -> Unit,
    onClickDelete:(ClothesData) -> Unit
){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clickable { onClickRow(cloth) }
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.closet),)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .background(color = colorResource(id = R.color.teal_700)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = cloth.ClothesName,
                fontSize = 20.sp)
        }
        Row(modifier = Modifier) {
            GlideImage(model = Uri.parse(cloth.ClothesImage), contentDescription = "服の写真")
            Column {
                Text(text = "服のタイプ")
                Text(text = cloth.ClothesType)
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "服のカラー")
                Text(text = cloth.ClothesColor)
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "シーズン")
                Text(text = cloth.ClothesScene)

            }
        }
    }
}

@Preview
@Composable
fun ClothsRowTest(){
    ClothsRow(
        cloth = ClothesData(
            ClothesName = "青色の服",
            ClothesType = "Tシャツ",
            ClothesColor = "青",
            ClothesScene = "夏",
            ClothesImage = ""
        ),
        onClickRow = {}, onClickDelete = {})
}

/*
IconButton(
onClick = { onClickDelete(cloth)
})
{
    Icon(imageVector = Icons.Default.Delete, contentDescription = "DeleteButton")
    GlideImage(model = Uri.parse(cloth.ClothesImage), contentDescription = "服の写真")
}*/
