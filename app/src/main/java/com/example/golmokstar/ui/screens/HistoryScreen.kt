package com.example.golmokstar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.golmokstar.ui.theme.AppTypography
import com.example.golmokstar.ui.theme.BlurBackgroundGray
import com.example.golmokstar.ui.theme.IconGray
import com.example.golmokstar.ui.theme.TextDarkGray
import com.example.golmokstar.ui.theme.TextLightGray
import com.example.golmokstar.ui.theme.White

data class History(
    val name: String,
    val address: String,
    val imageUrl: String,
    val rating: Double?,
    val title: String,
    val content: String,
    val date: String,
)

val sampleHistories = listOf(
    History(
        name = "스컹크웍스",
        address = "경상북도 경주시",
        imageUrl = "",
        rating = 4.7,
        title = "1박 2일 경쥬",
        content = "느낌이 좋았다. 인테리어가 이쁘고 음료도 맛있었다. 들어가고 바로 단체 손님 와서 식겁했다!! ",
        date = "2025.03.10"
    ), History(
        name = "석굴암",
        address = "경상북도 경주시",
        imageUrl = "",
        rating = 4.2,
        title = "1박 2일 경쥬",
        content = "엄청 힘들게 올라갔는데 생각보다 떨어져서 석굴함을 봐서 너무 아쉬웠다. 위엄이 느껴지지 않았음",
        date = "2025.04.05"
    ), History(
        name = "첨성대",
        address = "경상북도 경주시",
        imageUrl = "",
        rating = 4.5,
        title = "1박 2일 경쥬",
        content = "아제발 너무추웠다... 바람 너무 많이 불고 생각보다 작고 뭐 .. 뭐지 싶었음",
        date = "2025.02.25"
    ), History(
        name = "",
        address = "",
        imageUrl = "",
        rating = null,
        title = "",
        content = "",
        date = ""
    ), History(
        name = "",
        address = "",
        imageUrl = "",
        rating = null,
        title = "",
        content = "",
        date = ""
    )
)



@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HistoryScreen() {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("전체") }
    val items = listOf("옵션 1", "옵션 2", "옵션 3")

    var selectedAddress by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 드롭다운 메뉴
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier
                .width(180.dp)
                .height(50.dp)
                .padding(start = 30.dp)
        ) {
            OutlinedTextField(
                value = selectedItem,
                onValueChange = {},
                readOnly = true,
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
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

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(White)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item,
                                style = AppTypography.bodyMedium,
                                color = TextDarkGray
                            )
                        },
                        onClick = {
                            selectedItem = item
                            expanded = false
                        }
                    )
                }
            }
        }

            // 드롭다운과 RedBox 사이에 20dp 간격 추가
            Spacer(modifier = Modifier.height(20.dp))

        NavyBox(
            selectedAddress = selectedAddress,
            onBoxClick = { } ,
            modifier = Modifier.width(330.dp)// 클릭 시 처리
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(sampleHistories) { history ->
                HistoryCard(history)
            }
        }
    }
}


@Composable
fun HistoryCard(history: History) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally // 수평 중앙 정렬
    ) {
        if (history.content.isNotEmpty()) {
            OverCard(history) // 데이터가 있으면 OverCard 기본 표시
        } else {
            // 데이터가 없으면 기본 카드 표시
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .height(170.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Gray
                ),
            ) {}

            // 카드 아래에 기본 메시지 표시
            Spacer(modifier = Modifier.height(10.dp)) // 카드와 텍스트 사이 간격 추가
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = history.title,
                    color = Color.Black,
                    style = AppTypography.labelMedium
                )

                Spacer(modifier = Modifier.height(5.dp)) // 텍스트와 아이콘 사이 간격

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "별점",
                        tint = Color.Black // 별 아이콘 색상 변경
                    )

                    Text(
                        text = history.rating.toString(), // Double 값을 String으로 변환
                        color = Color.Black, // 수정된 부분
                        style = AppTypography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
fun OverCard(history: History) {
    Card(
        modifier = Modifier.fillMaxSize(), colors = CardDefaults.cardColors(BlurBackgroundGray)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = history.name, color = White, style = AppTypography.bodyMedium)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Text(
                        text = history.address,
                        color = TextLightGray,
                        style = AppTypography.labelMedium
                    )
                }
            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = history.content,
                    color = White,
                    style = AppTypography.labelMedium,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = history.title, color = TextLightGray, style = AppTypography.labelMedium)
                Text(text = history.date, color = TextLightGray, style = AppTypography.labelMedium)
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = history.name, color = Color.Black, style = AppTypography.labelMedium)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Icon(Icons.Default.Star)

            Text(
                text = history.rating.toString(),
                color = Color.Black,
                style = AppTypography.labelMedium
            )
        }
    }
}