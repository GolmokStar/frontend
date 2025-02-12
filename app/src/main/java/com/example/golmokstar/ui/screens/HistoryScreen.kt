package com.example.golmokstar.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.golmokstar.ui.theme.AppTypography
import com.example.golmokstar.ui.theme.IconGray
import com.example.golmokstar.ui.theme.TextDarkGray
import com.example.golmokstar.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen() {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("전체") }
    val items = listOf("옵션 1", "옵션 2", "옵션 3")

    Box(modifier = Modifier.fillMaxSize()) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }, // 드롭다운을 클릭했을 때 열리고 닫히게 함
            modifier = Modifier.padding(16.dp).width(180.dp).height(50.dp).align(Alignment.TopStart)) {
            // OutlinedTextField 설정
            OutlinedTextField(
                value = selectedItem,
                onValueChange = {}, // 읽기 전용이므로 변경 불가
                readOnly = true,
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    // 아이콘 클릭 시 드롭다운 열리거나 닫히게 함
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = "드롭다운 열기",
                            tint = IconGray
                        )
                    }
                },
                modifier = Modifier.menuAnchor(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = White,
                    focusedBorderColor = IconGray,
                    unfocusedBorderColor = IconGray,
                    focusedTextColor = TextDarkGray,
                    unfocusedTextColor = TextDarkGray
                ),
                textStyle = AppTypography.bodyMedium,
            )

            // 드롭다운 메뉴
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }, // 드롭다운 외부 클릭 시 닫히게 함
                modifier = Modifier.background(White)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item, style = AppTypography.bodyMedium, color = TextDarkGray) },
                        onClick = {
                            selectedItem = item
                            expanded = false // 아이템 클릭 시 드롭다운 닫히게 함
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHistoryScreen() {
    HistoryScreen()
}