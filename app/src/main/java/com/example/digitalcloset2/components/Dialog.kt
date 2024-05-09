package com.example.digitalcloset2.components


import android.widget.Toast
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.digitalcloset2.MainViewmodel
import com.example.digitalcloset2.R
import com.example.digitalcloset2.ScreenRoute


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClothesDialog(mainViewmodel: MainViewmodel, navController: NavController) {

    val mainUiState by mainViewmodel.mainUiState.collectAsState()

    val clothesDialogUiState by mainViewmodel.clothesDialog.collectAsState()

    val categories = stringArrayResource(id = R.array.category)
    val colors = stringArrayResource(id = R.array.color)
    val sizes = stringArrayResource(id = R.array.size)

    val context = LocalContext.current

    DisposableEffect(Unit) {
        onDispose {
            if (!mainUiState.onCamera) {
                mainViewmodel.resetCloth()
            }
        }
    }


    AlertDialog(
        onDismissRequest = {
            mainViewmodel.changeClothesDialogFlag(false)
        },
        title = {
            Text(
                text = if (mainViewmodel.isEditing) stringResource(id = R.string.editCloth) else stringResource(
                    id = R.string.newAdd
                )
            )
        },
        text = {
            Column {
                Text(text = stringResource(id = R.string.clothName))
                TextField(
                    value = clothesDialogUiState.name,
                    onValueChange = { mainViewmodel.changeName(it) })
                DropdownMenu(
                    mainViewModel = mainViewmodel,
                    title = stringResource(id = R.string.clothCategory),
                    item = categories,
                    containsValue = clothesDialogUiState.category
                ) { selectionOption ->
                    mainViewmodel.changeCategory(selectionOption)
                }
                DropdownMenu(
                    mainViewModel = mainViewmodel,
                    title = stringResource(id = R.string.clothColor),
                    item = colors,
                    containsValue = clothesDialogUiState.color
                ) { selectionOption ->
                    mainViewmodel.changeColor(selectionOption)
                }
                DropdownMenu(
                    mainViewModel = mainViewmodel,
                    title = stringResource(id = R.string.clothSize),
                    item = sizes,
                    containsValue = clothesDialogUiState.size
                ) { selectionOption ->
                    mainViewmodel.changeSize(selectionOption)
                }
                TextButton(
                    onClick = {
                        mainViewmodel.categoryUpdata(true)
                        mainViewmodel.categoryOnCamera(true)
                        navController.navigate(ScreenRoute.ShootingScreen.root)
                    }
                ) {
                    Text(text = stringResource(id = R.string.addingPhoto))
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (clothesDialogUiState.name == "") {
                    Toast.makeText(context, R.string.pleaseInputName, Toast.LENGTH_SHORT).show()
                } else {
                    mainViewmodel.changeClothesDialogFlag(false)
                    mainViewmodel.categoryOnCamera(false)
                    mainViewmodel.categoryUpdata(false)
                    if (mainViewmodel.isEditing) {
                        mainViewmodel.updateCloth()
                    } else {
                        mainViewmodel.createCloth()
                    }
                }
            }) {
                Text(text = stringResource(id = R.string.save))
            }
        },
        dismissButton = {
            Button(onClick = {
                mainViewmodel.categoryUpdata(false)
                mainViewmodel.categoryOnCamera(false)
                mainViewmodel.changeClothesDialogFlag(false)
            }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(
    mainViewModel: MainViewmodel,
    title: String,
    item: Array<String>,
    containsValue: String,
    onItemSelected: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(item[0]) }
    if (mainViewModel.isEditing) {
        selectedOptionText = containsValue
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
            item.forEach { selectionOption ->
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


