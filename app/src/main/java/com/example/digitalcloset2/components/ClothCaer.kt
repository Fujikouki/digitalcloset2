package com.example.digitalcloset2.components

import android.net.Uri
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.digitalcloset2.ClothesData
import com.example.digitalcloset2.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ClothCard(
    cloth: ClothesData,
    onClickRow: (ClothesData) -> Unit,
    onClickDelete: (ClothesData) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClickRow(cloth) },
                    onDoubleTap = { onClickDelete(cloth) })
            }
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(5.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                model = Uri.parse(cloth.ClothesImage),
                contentDescription = "服の写真",
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = cloth.ClothesName)
        }
    }
}

@Preview
@Composable
fun ClothsRowTest() {
    ClothCard(
        cloth = ClothesData(
            ClothesName = "青色の服",
            ClothesType = "Tシャツ",
            ClothesColor = "青",
            ClothesScene = "夏",
            ClothesImage = R.drawable.testimage2.toString()
        ),
        onClickRow = {},
        onClickDelete = {})
}

/*
IconButton(
onClick = { onClickDelete(cloth)
})
{
    Icon(imageVector = Icons.Default.Delete, contentDescription = "DeleteButton")
    GlideImage(model = Uri.parse(cloth.ClothesImage), contentDescription = "服の写真")
}*/
