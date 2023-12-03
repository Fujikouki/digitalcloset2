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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.digitalcloset2.mainviewmodel
import java.lang.StringBuilder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dialog(mainviewmodel: mainviewmodel = hiltViewModel()){
    AlertDialog(
        onDismissRequest = {
            mainviewmodel.flag = false
            mainviewmodel.isUpdate = false
                           },
        title = { Text(text = "タイトル")},
        text = {
               Column {
                   Text(text = "服の名前")
                   TextField(value = mainviewmodel.ClothesName , onValueChange = { mainviewmodel.ClothesName = it})
                   ExposedDropdownMenuSample(
                       _title = "服の種類",
                       _list = listOf("Top", "Bottom", "Dress", "Jacket",), v = mainviewmodel.ClothesType){
                           selectionOption -> mainviewmodel.ClothesType = selectionOption
                   }
                   ExposedDropdownMenuSample(
                       _title = "服の色", _list = listOf("Red", "Blue", "Green", "Black", "White",),v = mainviewmodel.ClothesColor){
                           selectionOption -> mainviewmodel.ClothesColor = selectionOption
                   }
                   ExposedDropdownMenuSample(
                       _title = "シーズン",
                       _list = listOf("Casual","Formal","Party","Workout",),v = mainviewmodel.ClothesScene){
                           selectionOption -> mainviewmodel.ClothesScene = selectionOption
                   }
                   TextButton(onClick = { mainviewmodel.ClothesImage = "image1" + mainviewmodel.ClothesName}) {
                       Text(text = "写真を追加")
                   }
               }
        },
        confirmButton = {
            Button(onClick = {
                mainviewmodel.flag = false
                mainviewmodel.isUpdate = false
                mainviewmodel.createCloth()
            })
            {
                Text(text = "保存")
            }
                        },

        dismissButton = {
            Button(onClick = {
                mainviewmodel.flag = false
                mainviewmodel.isUpdate = false
            }) {
                Text(text = "キャンセル")
            }
        }
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuSample(_title:String,_list:List<String>,v:String,onItemSelected: (String) -> Unit) {
    val mainviewmodel:mainviewmodel = hiltViewModel()
    val options = _list
    val title:String = _title
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    if(mainviewmodel.isUpdate){
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

/*@Preview
@Composable
fun test(){

    Dialog(flag = true)

}*/
