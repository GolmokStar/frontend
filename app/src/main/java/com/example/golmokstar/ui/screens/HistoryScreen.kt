package com.example.golmokstar.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
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
    val history: Boolean
)

val samplehistorydata = listOf(
    Sampledata(
        name = "스컹크웍스",
        address = "경상북도 경주시",
        imageUrl = "",
        rating = 4.7,
        title = "1박 2일 경쥬",
        content = "느낌이 좋았다. 인테리어가 이쁘고 음료도 맛있었다. 들어가고 바로 단체 손님 와서 식겁했다!! ",
        date = "2025.02.25",
        history = true
    ), Sampledata(
        name = "석굴암",
        address = "경상북도 경주시",
        imageUrl = "",
        rating = 4.2,
        title = "1박 2일 경쥬",
        content = "엄청 힘들게 올라갔는데 생각보다 떨어져서 석굴함을 봐서 너무 아쉬웠다. 위엄이 느껴지지 않았음",
        date = "2025.02.25",
        history = true
    ), Sampledata(
        name = "첨성대",
        address = "경상북도 경주시",
        imageUrl = "",
        rating = 4.5,
        title = "1박 2일 경쥬",
        content = "아제발 너무추웠다... 바람 너무 많이 불고 생각보다 작고 뭐 .. 뭐지 싶었음 너무 작았음 여기서 어케 별을 본걸까",
        date = "2025.02.25",
        history = true
    ), Sampledata(
        name = "국립경주박물관",
        address = "경상북도 경주시",
        imageUrl = "",
        rating = 4.3,
        title = "1박 2일 경쥬",
        content = "",
        date = "2025.02.25",
        history = false
    ), Sampledata(
        name = "동리",
        address = "경상북도 경주시",
        imageUrl = "",
        rating = 4.8,
        title = "1박 2일 경쥬",
        content = "",
        date = "2025.02.25",
        history = false
    )
)

@Preview
@Composable
fun HistoryScreen() {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("전체") }

    var showDialog by remember { mutableStateOf(false) }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
            .padding(horizontal = 20.dp, vertical = 16.dp)
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

        // samplehistorydata가 비어 있는지 확인
        if (samplehistorydata.isEmpty()) {
            item {
                NoRecord() // 데이터가 없으면 NoRecord 표시
            }
        } else {
            // 데이터가 있을 경우 LazyColumn의 항목들을 표시
            items(samplehistorydata) { sampledata ->
                if (sampledata.content.isEmpty()) {
                    NavyBox(
                        address = sampledata.address,
                        onBoxClick = { },
                        date = sampledata.date,
                        name = sampledata.name,
                        topLeftText = sampledata.title,
                        modifier = Modifier.fillMaxWidth(),
                        onButtonClick = { showDialog = true },
                        icon = { YellowMarkerIcon(Modifier.size(15.dp)) }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            items(samplehistorydata.toList()) { sampledata ->
                OptionCard(sampledata)
                Spacer(Modifier.height(5.dp))
                CommonRow(sampledata)
            }
        }
    }

    // Report 다이얼로그 표시
    if (showDialog) {
        Report(onDismiss = { showDialog = false })
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
                IconButton(
                    onClick = {
                        onExpandedChange(!expanded)
                    }
                ) {
                    Icon(
                        imageVector = if (expanded)
                            ImageVector.vectorResource(id = R.drawable.arrow_drop_up_icon)
                        else
                            ImageVector.vectorResource(id = R.drawable.arrow_drop_down_icon),
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

        // ExposedDropdownMenu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                // 외부 클릭 시 드롭다운을 닫기
                onExpandedChange(false)
            },
            modifier = Modifier.background(White)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item, style = AppTypography.bodyMedium, color = TextDarkGray) },
                    onClick = {
                        onItemSelect(item)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

@Composable
fun OptionCard(sampledata: Sampledata) {
    var isClicked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { isClicked = !isClicked },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray),
    ) {
        if (isClicked) {
            ClickCard(sampledata)
        }
    }
}

@Composable
fun ClickCard(sampledata: Sampledata) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally // 이 부분은 Column에 적용
    ){
        Card(
            modifier = Modifier.fillMaxSize(), colors = CardDefaults.cardColors(BlurBackgroundGray)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
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
}

// 공통 Row 부분을 분리한 함수
@Composable
fun CommonRow(sampledata: Sampledata) {
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

    Spacer(Modifier.height(20.dp))
}


// 홈스크린 팝업 참고
@Composable
fun Report(onDismiss: () -> Unit) {
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

                Spacer(Modifier.height(5.dp))

                // 별점을 위한 상태 관리
                var rating by remember { mutableFloatStateOf(0f) } // 현재 별점 상태

                // 별점 표시
                StarRatingBar(
                    onRatingChange = { newRating -> rating = newRating }, // 별점 변경 시 업데이트
                    fullStar = R.drawable.fullstar_icon, // 가득 찬 별 아이콘
                    halfStar = R.drawable.halfstar_icon, // 반쪽 별 아이콘
                    emptyStar = R.drawable.emptystar_icon // 빈 별 아이콘
                )

                Spacer(Modifier.height(5.dp))

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
            modifier = Modifier.fillMaxWidth().height(153.dp).border(1.dp, MainNavy, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
            ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) { // 세로 정렬
                Text(
                    text = "클릭해서 사진 추가하기",
                    color = TextDarkGray,
                    style = AppTypography.bodyMedium
                )

                Spacer(Modifier.height(5.dp)) // 줄 간격 조정

                Text(
                    text = "사진을 추가하지 않으면\n기본이미지가 등록됩니다.",
                    color = TextDarkGray,
                    style = AppTypography.labelMedium,
                    textAlign = TextAlign.Center // 텍스트 중앙 정렬
                )
            }
        }
    }
}

@Composable
fun StarRatingBar(
    onRatingChange: (Float) -> Unit,
    fullStar: Int,
    halfStar: Int,
    emptyStar: Int,
    modifier: Modifier = Modifier,
    isEditable: Boolean = true
) {
    var rating by remember { mutableStateOf(0f) } // 전체 별점 상태

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..5) { // 5개의 별 생성
            val starValue = i * 1f // 1, 2, 3, 4, 5 별 값

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val clickPosition = offset.x / 32.dp.toPx() // 클릭 위치 계산

                            // 각 별에 대한 범위와 점수 설정
                            val starRating = when {
                                clickPosition <= 0.2f -> i - 1f // 0.0 ~ 0.2 범위 -> 0.0, 1.0, 2.0...
                                clickPosition <= 0.7f -> i - 0.5f // 0.2 ~ 0.7 범위 -> 0.5, 1.5, 2.5...
                                else -> i.toFloat() // 0.7 ~ 1.0 범위 -> 1.0, 2.0, 3.0...
                            }

                            rating = starRating // 점수 설정
                            onRatingChange(rating) // 별점 변경 콜백

                            Log.d("StarRating", "Clicked Position: $clickPosition, Rating: $rating")

                        }
                    }
            ) {
                // 별을 표시하는 이미지 (빈 별, 반 별, 가득 찬 별 등)
                val iconRes = when {
                    rating >= i -> fullStar // 가득 찬 별
                    rating >= i - 0.5f -> halfStar // 반쪽 별
                    else -> emptyStar // 빈 별
                }

                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = "별점 $starValue",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(Modifier.width(10.dp))
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
        Text("내용 작성", color = TextBlack, style = AppTypography.bodyMedium)
    }

    OutlinedTextField(
        value = content,
        onValueChange = { newText -> content = newText },
        modifier = Modifier
            .fillMaxWidth()
            .height(173.dp),
        shape = RoundedCornerShape(10.dp),
        textStyle = AppTypography.labelMedium,
        placeholder = {
            Text(
                "방문한 장소는 어떠셨나요? 후기를 입력해주세요!",
                color = TextDarkGray,
                style = AppTypography.labelMedium
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MainNavy,
            unfocusedBorderColor = MainNavy
        )
    )
}

@Composable
fun NoRecord() {
    Box(
        modifier = Modifier.fillMaxWidth().height(92.dp)
            .background(Color.White, shape = RoundedCornerShape(10.dp)) // 배경색 추가
            .border(width = 1.dp, color = MainNavy, shape = RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center

    ){
        Text(
            text = "아직 방문한 기록이 없습니다.\n골목별과 여행을 시작해보세요!",
            style = AppTypography.bodyMedium,
            color = TextDarkGray,
            textAlign = TextAlign.Center,
        )
    }
}
