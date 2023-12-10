package com.example.digitalcloset2.components



import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

import androidx.navigation.NavController

import com.example.digitalcloset2.ScreenRoute
import com.example.digitalcloset2.mainviewmodel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dialog(Mainviewmodel: mainviewmodel,navController: NavController){
    DisposableEffect(Unit){
        onDispose {
            Mainviewmodel.resetCloth()
        }
    }
    AlertDialog(
        onDismissRequest = {
            Mainviewmodel.DialogFlag = false
                           },
        title = { Text(text = if(Mainviewmodel.isEditing)"更新" else "新規作成")},
        text = {
               Column {
                   Text(text = "服の名前")
                   TextField(value = Mainviewmodel.ClothesName , onValueChange = { Mainviewmodel.ClothesName = it})
                   ExposedDropdownMenuSample(
                       Mainviewmodel =Mainviewmodel,
                       _title = "服の種類",
                       _list = listOf("Top", "Bottom", "Dress", "Jacket",), v = Mainviewmodel.ClothesType){
                           selectionOption -> Mainviewmodel.ClothesType = selectionOption
                   }
                   ExposedDropdownMenuSample(
                       Mainviewmodel = Mainviewmodel,
                       _title = "服の色", _list = listOf("Red", "Blue", "Green", "Black", "White",),v = Mainviewmodel.ClothesColor){
                           selectionOption -> Mainviewmodel.ClothesColor = selectionOption
                   }
                   ExposedDropdownMenuSample(
                       Mainviewmodel = Mainviewmodel,
                       _title = "シーズン",
                       _list = listOf("Casual","Formal","Party","Workout",),v = Mainviewmodel.ClothesScene){
                           selectionOption -> Mainviewmodel.ClothesScene = selectionOption
                   }
                   TextButton(onClick = {
                       navController.navigate(ScreenRoute.ShootingScreen.root)
                   }) {
                       Text(text = "写真を追加")
                   }
               }
        },
        confirmButton = {
            Button(onClick = {
                Mainviewmodel.DialogFlag = false
                if(Mainviewmodel.isEditing){
                    Mainviewmodel.updateCloth()
                }else{
                    Mainviewmodel.createCloth()
                }

            })
            {
                Text(text = "保存")
            }
                        },

        dismissButton = {
            Button(onClick = {
                Mainviewmodel.DialogFlag = false
            }) {
                Text(text = "キャンセル")
            }
        }
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuSample(Mainviewmodel:mainviewmodel,_title:String,_list:List<String>,v:String,onItemSelected: (String) -> Unit) {
    val options = _list
    val title:String = _title
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    if(Mainviewmodel.isEditing){
        selectedOptionText =  v
    }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text(title) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                        onItemSelected(selectedOptionText)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}


