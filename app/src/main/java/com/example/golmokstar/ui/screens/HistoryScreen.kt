package com.example.golmokstar.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.golmokstar.R
import com.example.golmokstar.ui.theme.AppTypography
import com.example.golmokstar.ui.theme.BlurBackgroundGray
import com.example.golmokstar.ui.theme.IconGray
import com.example.golmokstar.ui.theme.MainNavy
import com.example.golmokstar.ui.theme.StarYellow
import com.example.golmokstar.ui.theme.TextBlack
import com.example.golmokstar.ui.theme.TextDarkGray
import com.example.golmokstar.ui.theme.TextLightGray
import com.example.golmokstar.ui.theme.White

data class Sampledata(
    val name: String,
    val address: String,
    val imageUrl: String,
    val rating: Double?,
    val title: String,
    val content: String,
    val date: String,
)

val samplehistorydata = listOf(
    Sampledata(
        name = "스컹크웍스",
        address = "경상북도 경주시",
        imageUrl = "",
        rating = 4.7,
        title = "1박 2일 경쥬",
        content = "느낌이 좋았다. 인테리어가 이쁘고 음료도 맛있었다. 들어가고 바로 단체 손님 와서 식겁했다!! ",
        date = "2025.03.10"
    ), Sampledata(
        name = "석굴암",
        address = "경상북도 경주시",
        imageUrl = "",
        rating = 4.2,
        title = "1박 2일 경쥬",
        content = "엄청 힘들게 올라갔는데 생각보다 떨어져서 석굴함을 봐서 너무 아쉬웠다. 위엄이 느껴지지 않았음",
        date = "2025.04.05"
    ), Sampledata(
        name = "첨성대",
        address = "경상북도 경주시",
        imageUrl = "",
        rating = 4.5,
        title = "1박 2일 경쥬",
        content = "아제발 너무추웠다... 바람 너무 많이 불고 생각보다 작고 뭐 .. 뭐지 싶었음 너무 작았음 여기서 어케 별을 본걸까",
        date = "2025.02.25"
    ), Sampledata(
        name = "국립경주박물관",
        address = "경상북도 경주시",
        imageUrl = "",
        rating = 4.3,
        title = "1박 2일 경쥬",
        content = "",
        date = "2025.02.25"
    ), Sampledata(
        name = "동리",
        address = "",
        imageUrl = "",
        rating = 4.8,
        title = "",
        content = "",
        date = ""
    )
)

@Preview
@Composable
fun HistoryScreen() {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("전체") }

    // Report 다이얼로그 상태 추가
    var showDialog by remember { mutableStateOf(false) }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp) // 좌우 20dp 패딩, 상단 16dp 패딩
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

        items(samplehistorydata) { sampledata ->
            if (sampledata.content.isEmpty()) {
                // NavyBox 컴포저블
                NavyBox(
                    address = sampledata.address,
                    onBoxClick = { }, // 클릭 시 showDialog 상태를 true로 변경
                    date = sampledata.date,
                    name = sampledata.name,
                    topLeftText = sampledata.title,
                    modifier = Modifier.fillMaxWidth(),
                    onButtonClick = { showDialog = true }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        items(samplehistorydata) { sampledata ->
            // LazyColumn의 항목들을 추가
            if (sampledata.content.isNotEmpty()) {
                RecordataCard(sampledata)
            } else {
                RecorNodataCard(sampledata)
            }
        }
    }

// Report 다이얼로그 표시
    if (showDialog) {
        Report(showDialog = showDialog, onDismiss = { showDialog = false })
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
fun RecorNodataCard(sampledata: Sampledata) {
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
            Text(text = sampledata.name, color = TextBlack, style = AppTypography.labelMedium)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.star_icon), contentDescription = "별 아이콘", tint = TextBlack)

                Text(text = sampledata.rating.toString(), color = TextBlack, style = AppTypography.labelMedium)
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun RecordataCard(sampledata: Sampledata) {
    Card(
        modifier = Modifier.fillMaxWidth().height(160.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize().background(BlurBackgroundGray))

            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                    Text(text = sampledata.name, color = White, style = AppTypography.bodyLarge)
                    Text(text = sampledata.address, color = TextLightGray, style = AppTypography.labelMedium)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier.width(200.dp).wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = sampledata.content, color = White, style = AppTypography.labelSmall)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = sampledata.title, color = TextLightGray, style = AppTypography.labelMedium)
                    Text(text = sampledata.date, color = TextLightGray, style = AppTypography.labelMedium)
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(5.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = sampledata.name, color = TextBlack, style = AppTypography.labelMedium)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.star_icon), contentDescription = "별 아이콘", tint = TextBlack)

            Text(text = sampledata.rating.toString(), color = TextBlack, style = AppTypography.labelMedium)
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

// 홈스크린 팝업 참고
@Composable
fun Report(showDialog: Boolean, onDismiss: () -> Unit) {
    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(580.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TitleArea(onDismiss)

                    PhotoAdd()

                    var ratings = remember { mutableStateOf(List(5) { 0f }) } // List<Float>로 수정

                    // 별점 표시
                    StarRatingBar(
                        rating = ratings.value.average().toFloat(), // 리스트 값의 평균을 별점으로 표시
                        onRatingChange = { newRating ->
                            // 새로운 별점을 설정할 때
                            ratings.value = List(ratings.value.size) { newRating }  // 모든 별점을 새로운 값으로 업데이트
                        },
                        fullStar = R.drawable.star_icon, // 가득 찬 별 아이콘
                        halfStar = R.drawable.star_icon, // 반쪽 별 아이콘
                        emptyStar = R.drawable.star_icon // 빈 별 아이콘
                    )

                    RecordContent()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .width(88.dp)
                                .height(35.dp),
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MainNavy,
                            )
                        ) {
                            Text("완료", color = White, style = AppTypography.bodyMedium)
                        }
                    }


                }

            }
        }
    }
}

@Composable
fun TitleArea(onDismiss: () -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.width(18.dp)) {}
            Text(text = "기록 작성", color = TextBlack, style = AppTypography.bodyLarge)
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.close_icon),
                contentDescription = "Close",
                tint = IconGray,
                modifier = Modifier.clickable { onDismiss() }
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 10.dp),
            thickness = 1.dp,
            color = IconGray
        )
    }
}


@Composable
fun PhotoAdd() {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("사진 선택", color = TextBlack, style = AppTypography.bodyMedium)
        Box(
            modifier = Modifier.fillMaxWidth().height(170.dp).border(1.dp, MainNavy, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
            ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) { // 세로 정렬
                Text(
                    text = "클릭해서 사진 추가하기",
                    color = TextLightGray,
                    style = AppTypography.bodyLarge
                )

                Spacer(Modifier.height(5.dp)) // 줄 간격 조정

                Text(
                    text = "사진을 추가하지 않으면\n기본이미지가 등록됩니다.",
                    color = TextLightGray,
                    style = AppTypography.labelMedium,
                    textAlign = TextAlign.Center // 텍스트 중앙 정렬
                )
            }
        }
    }
}

@Composable
fun StarRatingBar(
    rating: Float, // 현재 별점
    onRatingChange: (Float) -> Unit, // 별점 변경 콜백
    fullStar: Int, // 가득 찬 별 아이콘 (리소스 ID)
    halfStar: Int, // 반쪽 별 아이콘 (리소스 ID)
    emptyStar: Int, // 빈 별 아이콘 (리소스 ID)
    modifier: Modifier = Modifier,
    isEditable: Boolean = true // 읽기 전용 여부
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..5) { // 5개의 별 생성
            val starValue = i.toFloat()
            val iconRes  = when {
                rating >= starValue -> fullStar // 가득 찬 별
                rating >= starValue - 0.5f -> halfStar // 반쪽 별
                else -> emptyStar // 빈 별
            }

            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "별점 $starValue",
                modifier = Modifier
                    .size(32.dp)
                    .clickable(enabled = isEditable) { onRatingChange(starValue) },
                colorFilter = ColorFilter.tint(StarYellow)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordContent() {
    var content by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("내용 작성", color = TextBlack, style = AppTypography.bodyMedium,)
    }

    OutlinedTextField(
        value = content,
        onValueChange = { newText -> content = newText },
        modifier = Modifier
            .fillMaxWidth()
            .height(173.dp)
            .border(1.dp, MainNavy, RoundedCornerShape(20.dp)),
        textStyle = AppTypography.labelMedium,
        placeholder = {
            Text(
                "방문한 장소는 어떠셨나요? 후기를 입력해주세요!",
                color = TextLightGray,
                style = AppTypography.labelMedium
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MainNavy,
            unfocusedBorderColor = MainNavy,
        )
    )
}