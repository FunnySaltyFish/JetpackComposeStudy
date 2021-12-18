package com.funny.compose.study.ui.videod

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource

@Composable
fun ScreenD() {
    Scaffold(
        topBar = {
            AppBar()
        }
    ) {

    }
}

@Composable
fun AppBar() {
    var expanded by remember {
        mutableStateOf(false)
    }

    var isChildExpanded by remember {
        mutableStateOf(false)
    }
    TopAppBar(
        title = { Text(text = "App") },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "More"
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(onClick = {

                }) {
//                    DropdownMenu(expanded = isChildExpanded, onDismissRequest = { isChildExpanded = false }) {
//                        DropdownMenuItem(onClick = {}) {
//                            Text(text = "展开项1")
//                        }
//                        DropdownMenuItem(onClick = {}) {
//                            Text(text = "展开项2")
//                        }
//                    }
                    Text("Normal Item")
                }
                ExpandableDropdownItem(text = "Expandable") {
                    DropdownMenuItem(onClick = {}) {
                        Text(text = "Nested 1")
                    }
                    DropdownMenuItem(onClick = {}) {
                        Text(text = "Nested 2")
                    }
                }

            }
        }
    )
}

@Composable
fun ExpandableDropdownItem(
    text: String,
    dropDownItems: @Composable () -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    DropdownMenuItem(onClick = {
        expanded = true
    }) {
        Text(text = text)
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        dropDownItems()
    }
}