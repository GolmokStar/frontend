package com.example.golmokstar.ui.screens


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.*
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.navigation.compose.rememberNavController
import com.example.golmokstar.ui.theme.AppTypography
import com.example.golmokstar.ui.theme.TextBlack
import com.example.golmokstar.ui.theme.TextDarkGray
import com.example.golmokstar.ui.theme.White
import java.time.*
import java.time.format.TextStyle
import java.util.Locale
import com.example.golmokstar.R
import com.example.golmokstar.ui.theme.*

data class Diary(
    val title: String,
    val content: String,
    val date: String
)

val sampleDiaries = listOf(
    Diary(
        title = "새로운 시작",
        content = "오늘은 오랜만에 공원에 나가 산책을 했다. 바람이 선선하고 하늘도 맑아서 기분이 좋았다. 이어폰을 끼고 좋아하는 음악을 들으며 걷다 보니 복잡했던 생각들이 정리되는 느낌이었다. 벤치에 앉아 책을 읽으며 시간을 보내다가, 근처 카페에서 커피 한 잔을 마셨다. 이렇게 여유로운 시간을 보내는 게 참 오랜만인 것 같다. 앞으로는 바쁜 일상 속에서도 이런 작은 휴식",
        date = "2025.02.18"
    ),
    Diary(
        title = "주말 여행",
        content = "속초로 여행을 다녀왔다. 겨울 바다를 보러 간 건 처음이었는데, 생각보다 분위기가 너무 좋았다. 바람이 조금 차가웠지만 맑은 하늘과 잔잔한 파도를 보니 마음이 편안해졌다. 시장에서 회도 먹고 카페에서 여유롭게 시간을 보내니 재충전이 되는 기분이었다.",
        date = "2025.02.17"
    ),
    Diary(
        title = "운동을 시작하다",
        content = "오랜만에 헬스장을 다시 등록했다. 요즘 계속 앉아있는 시간이 많아서 몸이 무거워지는 느낌이 들었는데, 이제라도 운동을 시작해서 다행이다. 가벼운 웨이트와 러닝부터 시작했는데, 벌써 온몸이 뻐근하다. 꾸준히 해서 체력을 길러야겠다.",
        date = "2025.02.16"
    ),
    Diary(
        title = "책 읽는 하루",
        content = "오늘은 하루 종일 책을 읽으며 시간을 보냈다. ‘이카루스 이야기’라는 책이었는데, 도전과 실패에 대한 깊은 메시지를 담고 있어서 인상적이었다. 목표를 세우고 도전하는 과정이 얼마나 중요한지 다시금 깨닫게 되었다. 앞으로 더 많은 책을 읽고 싶다.",
        date = "2025.02.15"
    ),
    Diary(
        title = "친구와의 만남",
        content = "오랜만에 친한 친구를 만났다. 서로 바빠서 한동안 연락만 주고받다가 드디어 직접 만나게 되었다. 카페에서 커피를 마시면서 최근 있었던 일들에 대해 이야기하다 보니 시간이 훌쩍 지나갔다. 역시 좋은 사람들과 함께하는 시간이 제일 행복하다.",
        date = "2025.02.14"
    )
)


@Preview(showBackground = true)
@Composable
fun MyScreenPreview() {
    CalendarScreen(navController = rememberNavController())
}


@Composable
fun CalendarScreen(navController: NavController) {
    var showNotTravelingModal by remember { mutableStateOf(false) }
    var showUnrecordedEntriesModal by remember { mutableStateOf(false) }
    var showNoRecordsModal by remember { mutableStateOf(false) }

    var showCreateDiaryModal by remember { mutableStateOf(false) }

    var count = 3

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        //show 변수에 따라 보여지는 팝업이 다름
        CustomCalendar(onClick = { showUnrecordedEntriesModal = true })
        Section(
            firstComponent = {
                Text(
                    text = "최근 작성한 일기", style = AppTypography.titleMedium, modifier = Modifier.then(
                        if (count != 0) Modifier.padding(horizontal = 20.dp) else Modifier
                    )
                )
            },
            secondComponent = { RecentDiary(count = count) },
            padding = (if (count == 0) 20 else 0)
        )
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
    CreateDiaryModal(
        showDialog = showCreateDiaryModal,
        onDismiss = { showCreateDiaryModal = false })

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
fun RecentDiary(count: Int = 0) {
    if (count == 0) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .border(1.dp, MainNavy, shape = RoundedCornerShape(10.dp)) // 테두리 추가
                .background(White, shape = RoundedCornerShape(10.dp)) // 배경색 추가
                .fillMaxWidth()
                .height(92.dp)
        ) {
            Text(
                text = "아직 일기를 작성한 날이 없습니다.",
                style = AppTypography.bodyMedium,
                color = TextDarkGray,
            )
            Text(
                text = "골목별과 여행을 시작해보세요!",
                style = AppTypography.bodyMedium,
                color = TextDarkGray,
            )
        }
    } else {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            items(sampleDiaries.toList()) { diary ->
                DiaryCard(diary)
            }
        }

    }
}

@Composable
fun DiaryCard(diary: Diary) {
    var isClicked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(230.dp)
            .height(230.dp)
            .clickable { isClicked = !isClicked },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray
        )
    ) {
        if (isClicked) {
            Card(
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(BlurBackgroundGray)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                ) {
                    Text(diary.date, color = White, style = AppTypography.labelLarge)
                    Text(diary.title, color = White, style = AppTypography.labelLarge)
                    Text(diary.content, color = White, style = AppTypography.labelLarge)
                }

            }
        }


    }
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
            Row(modifier = Modifier.width(18.dp)) {}
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
    } else {
        Text("", style = AppTypography.labelSmall)
    }

}