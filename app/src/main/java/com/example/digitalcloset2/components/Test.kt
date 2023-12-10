package com.example.digitalcloset2.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun DropdownMenuSample(options: List<String>, onItemSelected: (String) -> Unit) {
    var expanded = remember { mutableStateOf(false) }
    var selectedOption = remember { mutableStateOf(options.firstOrNull()) }

    Column {
        Text(
            text = "Selected item: ${selectedOption ?: "None"}",
            modifier = Modifier.padding(16.dp)
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.padding(16.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption.value = option
                        expanded.value = false
                        onItemSelected(option)
                    },
                    text = { Text(text = option) }
                )
            }
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Button(onClick = { expanded.value = !expanded.value }) {
            Text(text = "Toggle Dropdown")
        }
    }
}

@Preview
@Composable
fun Test2(){
    DropdownMenuSample(options = listOf("Top", "Bottom", "Dress", "Jacket",), onItemSelected = { print(it) })
}

