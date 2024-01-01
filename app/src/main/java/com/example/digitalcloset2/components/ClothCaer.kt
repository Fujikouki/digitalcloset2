package com.example.digitalcloset2.components

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.digitalcloset2.ClothesData

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
    ) {
        Column(modifier = Modifier) {
            Row(modifier = Modifier
                .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically)
            {
                Column(modifier = Modifier.padding(5.dp)) {
                    Text(text = cloth.ClothesName)
                    Text(text = cloth.ClothesColor)
                }
                Spacer(modifier = Modifier.weight(1f))
                Column(modifier = Modifier.padding(5.dp)) {
                    Text(text = cloth.ClothesScene)
                    Text(text = cloth.ClothesType)
                }
                Spacer(modifier = Modifier.weight(2f))
                Column(modifier = Modifier.padding(5.dp)) {
                    IconButton(
                        onClick = { onClickDelete(cloth)
                            Log.d("1234567",cloth.toString())
                        })
                    {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "DeleteButton")
                    }
                }
            }
            GlideImage(model = Uri.parse(cloth.ClothesImage), contentDescription = "服の写真")
            Log.d("cloth.Image",cloth.ClothesImage)
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