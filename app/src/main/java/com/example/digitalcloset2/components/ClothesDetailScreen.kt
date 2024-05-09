package com.example.digitalcloset2.components

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.digitalcloset2.MainViewmodel


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ClothesDetailScreen(modifier: Modifier = Modifier, mainViewmodel: MainViewmodel) {

    DisposableEffect(Unit) {
        onDispose {
            mainViewmodel.resetCloth()
        }
    }

    val cloth by mainViewmodel.clothesDialog.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Digital Closet") }, actions = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Create, contentDescription = "Clothes add")
                    }
                })
        }

    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            GlideImage(model = Uri.parse(cloth.image), contentDescription = "")
            Spacer(modifier = modifier.padding(8.dp))
            Text(text = "服の名前: ${cloth.name}")
            Spacer(modifier = modifier.padding(8.dp))
            Text(text = "服の種類: ${cloth.category}")
            Spacer(modifier = modifier.padding(8.dp))
            Text(text = "服の色: ${cloth.color}")
            Spacer(modifier = modifier.padding(8.dp))
            Text(text = "サイズ: ${cloth.size}")
            Spacer(modifier = modifier.padding(8.dp))
            Text(text = "ブランド: ${cloth.brand}")
            Spacer(modifier = modifier.padding(8.dp))
            Text(text = "お気に入り: ${cloth.like}")
        }
    }
}