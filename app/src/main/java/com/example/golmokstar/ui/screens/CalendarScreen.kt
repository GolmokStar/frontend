package com.example.golmokstar.ui.screens


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.*
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.golmokstar.ui.theme.AppTypography
import com.example.golmokstar.ui.theme.TextBlack
import com.example.golmokstar.ui.theme.TextDarkGray
import com.example.golmokstar.ui.theme.White
import java.time.*
import java.time.format.TextStyle
import java.util.Locale
import com.example.golmokstar.R
import com.example.golmokstar.ui.theme.*


@Composable
fun CalendarScreen(navController: NavController) {
    var showNotTravelingModal by remember { mutableStateOf(false) }
    var showUnrecordedEntriesModal by remember { mutableStateOf(false) }
    var showNoRecordsModal by remember { mutableStateOf(false) }

    var showCreateDiaryModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        //show 변수에 따라 보여지는 팝업이 다름
        CustomCalendar(onClick = { showUnrecordedEntriesModal = true })
    }

    NotTravelingModal(
        navController,
        showDialog = showNotTravelingModal,
        onDismiss = { showNotTravelingModal = false })
    UnrecordedEntriesModal(
        navController,
        showDialog = showUnrecordedEntriesModal,
        onDismiss = { showUnrecordedEntriesModal = false },
        onDiary = { showCreateDiaryModal = true })
    NoRecordsModal(
        navController,
        showDialog = showNoRecordsModal,
        onDismiss = { showNoRecordsModal = false })
    CreateDiaryModal(showDialog = showCreateDiaryModal, onDismiss = { showCreateDiaryModal = false })

}

@Composable
fun CustomCalendar(onClick: () -> Unit) {
    val today = LocalDate.now()

    var visibleMonth by remember() { mutableStateOf(YearMonth.now()) }

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        CalendarHeader(visibleMonth,
            onPreviousClick = { visibleMonth = visibleMonth.minusMonths(1) },
            onNextClick = { visibleMonth = visibleMonth.plusMonths(1) })

        CalendarBody(visibleMonth, today, selectedDate, onDateClick = { clickedDate ->
            selectedDate = clickedDate
            println("선택한 날짜: $clickedDate")
            onClick()
        })


    }
}


@Composable
fun CalendarHeader(
    visibleMonth: YearMonth, onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.previous_icon),
            contentDescription = "Previous",
            tint = IconGray,
            modifier = Modifier.clickable { onPreviousClick() }
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            Text(
                text = visibleMonth.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                style = AppTypography.titleMedium,
                color = TextBlack
            )
            // 월 헤더 (연도 및 월 표시)
            Text(
                text = "${visibleMonth.year}",
                style = AppTypography.labelMedium,
                color = TextDarkGray,
            )
        }
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.next_icon),
            contentDescription = "Next",
            tint = IconGray,
            modifier = Modifier.clickable { onNextClick() })
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Mon", color = TextDarkGray, style = AppTypography.bodyMedium)
        Text(text = "Tue", color = TextDarkGray, style = AppTypography.bodyMedium)
        Text(text = "Wed", color = TextDarkGray, style = AppTypography.bodyMedium)
        Text(text = "Thu", color = TextDarkGray, style = AppTypography.bodyMedium)
        Text(text = "Fri", color = TextDarkGray, style = AppTypography.bodyMedium)
        Text(text = "Sat", color = TextDarkGray, style = AppTypography.bodyMedium)
        Text(text = "Sun", color = TextDarkGray, style = AppTypography.bodyMedium)
    }
}


@Composable
fun CalendarBody(
    visibleMonth: YearMonth,
    today: LocalDate,
    selectedDate: LocalDate?,
    onDateClick: (LocalDate) -> Unit
) {
    val firstDayOfMonth = visibleMonth.atDay(1) // 해당 월의 첫 번째 날
    val daysInMonth = visibleMonth.lengthOfMonth() // 해당 월의 총 날짜 개수
    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.value + 6) % 7 // 월요일(0) ~ 일요일(6)로 변환

    val totalCells = firstDayOfWeek + daysInMonth // 요일 포함한 전체 셀 개수
    val weeks = (totalCells + 6) / 7 // 총 주(week) 개수

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
    ) {
        // 📅 주 단위로 행을 만듦
        for (week in 0 until weeks) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (day in 0..6) { // 7일씩 표시
                    val dayIndex = week * 7 + day - firstDayOfWeek + 1

                    if (dayIndex in 1..daysInMonth) {
                        val date = visibleMonth.atDay(dayIndex)
                        val isToday = date == today

                        Column(
                            modifier = Modifier
                                .size(35.dp, 30.dp)
                                .then(if (isToday) Modifier.background(BackgroundSky) else Modifier)
                                .clickable {
                                    onDateClick(date) // 클릭 이벤트 호출
                                },
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = dayIndex.toString(),
                                color = TextBlack,
                                style = AppTypography.bodyMedium
                            )

                            // 일기를 작성한 날을 구분해야하지만 현재 데이터가 없기 때문에 임시로 오늘을 기준으로 dot 찍음
                            if (isToday) {
                                EventDot(MainNavy)
                            }
                        }
                    } else {
                        // 빈 칸을 위한 Spacer (첫 주의 공백을 맞추기 위해)
                        Spacer(modifier = Modifier.size(35.dp, 30.dp))
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp)
            ) {
                //Text("text area") 여행 title이 들어갈 예정
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EventDot(MainNavy)
                Text(text = "일기를 작성한 날", color = TextDarkGray, style = AppTypography.labelSmall)
            }
            Spacer(modifier = Modifier.size(20.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EventDot(StarYellow)
                Text(text = "방문기록이 있는 날", color = TextDarkGray, style = AppTypography.labelSmall)
            }
        }
    }
}

@Composable
fun EventDot(color: Color) {
    Box(
        modifier = Modifier
            .size(4.dp)
            .clip(CircleShape)
            .background(color)
    )
}


@Composable
fun NotTravelingModal(navController: NavController, showDialog: Boolean, onDismiss: () -> Unit) {
    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // text, buttons 배치를 위한 빈 컴포넌트
                    Box() {}

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "여행을 한 날짜가 아닙니다!",
                            style = AppTypography.bodyMedium,
                            color = TextBlack
                        )
                        Text(
                            text = "여행을 시작하러 가시겠습니까?",
                            style = AppTypography.bodyMedium,
                            color = TextBlack
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .drawBehind { // Row 상단에만 Border 추가
                                drawLine(
                                    color = TextBlack, // 선 색상
                                    start = Offset(0f, 0f), // 시작 위치 (왼쪽 상단)
                                    end = Offset(size.width, 0f), // 끝 위치 (오른쪽 상단)
                                    strokeWidth = 1.dp.toPx() // 선 두께
                                )
                            },
                    ) {
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            shape = RectangleShape,
                            onClick = {
                                onDismiss()
                                navController.navigate("home") {
                                    popUpTo("calendar") { inclusive = true }
                                    launchSingleTop = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = White)
                        ) {
                            Text(text = "이동하기", color = TextBlack, style = AppTypography.bodyMedium)
                        }
                        VerticalDivider(
                            color = TextBlack,
                            modifier = Modifier
                                .width(1.dp) // 세로선 너비
                                .fillMaxHeight() // 버튼 높이만큼 차지
                        )
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            shape = RectangleShape,
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(containerColor = White)
                        ) {
                            Text("머무르기", color = TextBlack, style = AppTypography.bodyMedium)
                        }

                    }
                }

            }
        }
    }
}


@Composable
fun UnrecordedEntriesModal(
    navController: NavController,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onDiary: () -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // text, buttons 배치를 위한 빈 컴포넌트
                    Box() {}

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "기록하지 않은 방문내역이 있습니다!",
                            style = AppTypography.bodyMedium,
                            color = TextBlack
                        )
                        Text(
                            text = "기록하러 이동하시겠습니까?",
                            style = AppTypography.bodyMedium,
                            color = TextBlack
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .drawBehind {
                                drawLine(
                                    color = TextBlack, // 선 색상
                                    start = Offset(0f, 0f), // 시작 위치 (왼쪽 상단)
                                    end = Offset(size.width, 0f), // 끝 위치 (오른쪽 상단)
                                    strokeWidth = 1.dp.toPx() // 선 두께
                                )
                            },
                    ) {
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            shape = RectangleShape,
                            onClick = {
                                onDismiss()
                                navController.navigate("history") {
                                    popUpTo("calendar") { inclusive = true }
                                    launchSingleTop = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = White)
                        ) {
                            Text("이동하기", color = TextBlack, style = AppTypography.bodyMedium)
                        }
                        VerticalDivider(
                            color = TextBlack,
                            modifier = Modifier
                                .width(1.dp) // 세로선 너비
                                .fillMaxHeight() // 버튼 높이만큼 차지
                        )
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            shape = RectangleShape,
                            onClick = { onDismiss(); onDiary() },
                            colors = ButtonDefaults.buttonColors(containerColor = White)
                        ) {
                            Text("일기쓰기", color = TextBlack, style = AppTypography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoRecordsModal(navController: NavController, showDialog: Boolean, onDismiss: () -> Unit) {
    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // text, buttons 배치를 위한 빈 컴포넌트
                    Box() {}

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "기록한 방문내역이 하나도 없습니다!",
                            style = AppTypography.bodyMedium,
                            color = TextBlack
                        )
                        Text(
                            text = "기록하러 이동하시겠습니까?",
                            style = AppTypography.bodyMedium,
                            color = TextBlack
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .drawBehind {
                                drawLine(
                                    color = TextBlack, // 선 색상
                                    start = Offset(0f, 0f), // 시작 위치 (왼쪽 상단)
                                    end = Offset(size.width, 0f), // 끝 위치 (오른쪽 상단)
                                    strokeWidth = 1.dp.toPx() // 선 두께
                                )
                            },
                    ) {
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            shape = RectangleShape,
                            onClick = {
                                onDismiss()
                                navController.navigate("history") {
                                    popUpTo("calendar") { inclusive = true }
                                    launchSingleTop = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = White)
                        ) {
                            Text("이동하기", color = TextBlack, style = AppTypography.bodyMedium)
                        }
                        VerticalDivider(
                            color = TextBlack,
                            modifier = Modifier
                                .width(1.dp) // 세로선 너비
                                .fillMaxHeight() // 버튼 높이만큼 차지
                        )
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            shape = RectangleShape,
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(containerColor = White)
                        ) {
                            Text("머무르기", color = TextBlack, style = AppTypography.bodyMedium)
                        }

                    }
                }

            }
        }
    }
}

@Composable
fun CreateDiaryModal(showDialog: Boolean, onDismiss: () -> Unit) {
    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(580.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ModalTitle(onDismiss)

                    SelectPhoto()

                    ModalContent()


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
fun ModalTitle(onDismiss: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {}
            Text(text = "일기 작성", color = TextBlack, style = AppTypography.bodyLarge)
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
fun SelectPhoto() {
    var selectedIndex by remember { mutableStateOf(-1) }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            "사진 선택",
            color = TextBlack,
            style = AppTypography.bodyMedium,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            items(3) { index ->
                RectanglePhoto(isSelected = selectedIndex == index,
                    onClick = { selectedIndex = index })
            }
        }
    }
}

@Composable
fun RectanglePhoto(isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .background(TextLightGray, RoundedCornerShape(20.dp))
            .then(
                if (isSelected) Modifier.border(
                    2.dp,
                    MainNavy,
                    RoundedCornerShape(20.dp)
                ) else Modifier
            )
            .clickable { onClick() }
    ) {
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalContent() {
    var content by remember { mutableStateOf("") }

    var showCaption by remember { mutableStateOf(true) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "내용 작성",
            color = TextBlack,
            style = AppTypography.bodyMedium,
        )
        Button(
            onClick = {},
            modifier = Modifier
                .width(75.dp)
                .height(25.dp),
            shape = RoundedCornerShape(5.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MainNavy,
            )
        ) { Text(text = "AI 일기쓰기", color = White, style = AppTypography.labelMedium) }
    }

    OutlinedTextField(
        value = content,
        onValueChange = { newText ->
            content = newText
            if (content.isNotEmpty()) {
                showCaption = false
            } else {
                showCaption = true
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(173.dp)
            .padding(horizontal = 10.dp),
        textStyle = AppTypography.labelMedium,
        placeholder = {
            Text(
                "일기를 입력해주세요!",
                color = TextDarkGray,
                style = AppTypography.labelMedium
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MainNavy,
            unfocusedBorderColor = MainNavy,
        )
    )

    if (showCaption) {
        Text(
            text = "* 내용을 입력해주세요",
            color = ErrorRed,
            style = AppTypography.labelSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            textAlign = TextAlign.Start
        )
    }

}