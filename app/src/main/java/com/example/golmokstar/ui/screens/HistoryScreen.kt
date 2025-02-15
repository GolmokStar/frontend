package com.example.golmokstar.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.golmokstar.R
import com.example.golmokstar.ui.theme.AppTypography
import com.example.golmokstar.ui.theme.BlurBackgroundGray
import com.example.golmokstar.ui.theme.IconGray
import com.example.golmokstar.ui.theme.TextBlack
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
        content = "아제발 너무추웠다... 바람 너무 많이 불고 생각보다 작고 뭐 .. 뭐지 싶었음 너무 작았음 여기서 어케 별을 본걸까",
        date = "2025.02.25"
    ), History(
        name = "국립경주박물관",
        address = "경상북도 경주시",
        imageUrl = "",
        rating = 4.3,
        title = "1박 2일 경쥬",
        content = "",
        date = "2025.02.25"
    ), History(
        name = "동리",
        address = "",
        imageUrl = "",
        rating = 4.8,
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

    // Box로 감싸서 LazyColumn을 가운데 정렬
    Box(
        modifier = Modifier.fillMaxSize(), // 전체 화면을 채우고
        contentAlignment = Alignment.Center // 중앙에 정렬
    ) {
        // LazyColumn
        LazyColumn(
            modifier = Modifier.width(350.dp).padding(16.dp)
        ) {
            item {
                // 드롭다운 메뉴
                DropdownMenuSection(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    selectedItem = selectedItem,
                    onItemSelect = { selectedItem = it },
                    modifier = Modifier.width(180.dp).height(50.dp)
                )
                Spacer(modifier = Modifier.height(25.dp))
            }

            items(sampleHistories) { history ->
                if (history.content.isEmpty()) {
                    // NavyBox 컴포저블
                    NavyBox(
                        address = history.address,
                        onBoxClick = { },
                        date = history.date,
                        name = history.name,
                        topLeftText = history.title,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                } else { }
            }

            items(sampleHistories) { history ->
                // LazyColumn의 항목들을 추가
                if (history.content.isNotEmpty()) {
                    RecordataCard(history)
                } else {
                    RecorNodataCard(history)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuSection(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedItem: String,
    onItemSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf("전체", "옵션 1", "옵션 2", "옵션 3")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                IconButton(onClick = { onExpandedChange(!expanded) }) {
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
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.background(White)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item, style = AppTypography.bodyMedium, color = TextDarkGray) },
                    onClick = { onItemSelect(item)
                        onExpandedChange(false) }
                )
            }
        }
    }
}


@Composable
fun RecorNodataCard(history: History) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier.fillMaxWidth().height(160.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Gray),
        ) {}

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = history.name, color = TextBlack, style = AppTypography.labelMedium)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.star_icon), contentDescription = "별 아이콘", tint = TextBlack)

                Text(text = history.rating.toString(), color = TextBlack, style = AppTypography.labelMedium)
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun RecordataCard(history: History) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize().background(BlurBackgroundGray))

            // Column을 수직으로 중앙 정렬하도록 설정
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.Center,  // 수직 중앙 정렬
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                    Text(text = history.name, color = White, style = AppTypography.bodyLarge)
                    Text(text = history.address, color = TextLightGray, style = AppTypography.labelMedium)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier.width(200.dp).wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = history.content,
                        color = White,
                        style = AppTypography.labelSmall,
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

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
    }

    Spacer(modifier = Modifier.height(5.dp))

    Row(
        modifier = Modifier.width(330.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = history.name, color = TextBlack, style = AppTypography.labelMedium)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.star_icon), contentDescription = "별 아이콘", tint = TextBlack)

            Text(text = history.rating.toString(), color = TextBlack, style = AppTypography.labelMedium)
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
}

