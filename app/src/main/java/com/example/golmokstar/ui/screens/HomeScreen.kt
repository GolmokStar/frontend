package com.example.golmokstar.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.golmokstar.R
import com.example.golmokstar.ui.theme.*
import kotlin.reflect.typeOf


data class Place(
    val name: String,
    val address: String,
    val imageUrl: String
)

data class History(
    val name: String,
    val address: String,
    val imageUrl: String,
    val rating: Double,
    val title: String,
    val content: String,
    val date: String,
)

val samplePlaces = listOf(
    Place(
        name = "서울 남산타워",
        address = "서울특별시 용산구",
        imageUrl = "https://source.unsplash.com/400x300/?seoul,tower"
    ),
    Place(
        name = "부산 해운대 해수욕장",
        address = "부산광역시 해운대구",
        imageUrl = "https://source.unsplash.com/400x300/?beach,ocean"
    ),
    Place(
        name = "제주 성산일출봉",
        address = "제주도 서귀포시",
        imageUrl = "https://source.unsplash.com/400x300/?jeju,mountain"
    ),
    Place(
        name = "강릉 안목해변",
        address = "강원도 강릉시",
        imageUrl = "https://source.unsplash.com/400x300/?cafe,beach"
    ),
    Place(
        name = "전주 한옥마을",
        address = "전라북도 전주시 완산구",
        imageUrl = "https://source.unsplash.com/400x300/?hanok,village"
    )
)

val sampleHistories = listOf(
    History(
        name = "서울 올림픽공원",
        address = "서울특별시 송파구",
        imageUrl = "https://source.unsplash.com/400x300/?seoul,park",
        rating = 4.7,
        title = "올림픽공원 산책",
        content = "서울 올림픽공원에서 자전거를 타며 여유롭게 산책을 즐겼다. 공원 내의 평화로운 분위기가 인상적이었다.",
        date = "2025.03.10"
    ),
    History(
        name = "경주 불국사",
        address = "경상북도 경주시",
        imageUrl = "https://source.unsplash.com/400x300/?temple,kyongju",
        rating = 4.9,
        title = "경주 불국사 탐방",
        content = "불국사의 아름다운 건축과 고요한 분위기에 감동했다. 역사적인 의미를 되새기며 조용히 산책했다.",
        date = "2025.04.05"
    ),
    History(
        name = "강원도 평창",
        address = "강원도 평창군",
        imageUrl = "https://source.unsplash.com/400x300/?mountain,pyeongchang",
        rating = 4.8,
        title = "평창 스키 여행",
        content = "평창에서 스키를 타며 겨울을 만끽했다. 눈 덮인 산의 아름다움과 스키장의 즐거움이 인상 깊었다.",
        date = "2025.02.25"
    ),
    History(
        name = "서울 북촌 한옥마을",
        address = "서울특별시 종로구",
        imageUrl = "https://source.unsplash.com/400x300/?hanok,seoul",
        rating = 4.6,
        title = "북촌 한옥마을 탐방",
        content = "북촌 한옥마을에서 전통적인 분위기를 느꼈다. 한옥의 아름다움과 조용한 골목이 마음에 들었다.",
        date = "2025.05.12"
    ),
    History(
        name = "전주 덕진공원",
        address = "전라북도 전주시 덕진구",
        imageUrl = "https://source.unsplash.com/400x300/?park,jeonju",
        rating = 4.4,
        title = "전주 덕진공원 산책",
        content = "덕진공원에서 피크닉을 즐기며 봄날의 따뜻한 햇살을 만끽했다. 공원의 아름다움에 감탄했다.",
        date = "2025.04.20"
    )
)


@Preview
@Composable
fun HomeScreen() {
    val (text, setValue) = remember {
        mutableStateOf("")
    }

    val scrollState = rememberScrollState()

    val count = 0

    Column(
        verticalArrangement = Arrangement.spacedBy(35.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
            .verticalScroll(scrollState)
            .padding(top = 20.dp, bottom = 50.dp),
    ) {
        Section(
            firstComponent = {
                Text(
                    text = "여행 등록하기",
                    style = AppTypography.titleMedium
                )
            },
            secondComponent = { CheckIconTextField(value = text, onValueChange = setValue) },
            padding = true
        )

        Section(
            firstComponent = {
                Text(
                    text = "최근 기록한 장소",
                    style = AppTypography.titleMedium,
                    modifier = Modifier.then(
                        if (count != 0) Modifier.padding(horizontal = 20.dp) else Modifier
                    )
                )
            },
            secondComponent = { RecentHistoryPlace(count = count) }, padding = (count == 0)
        )

        Section(
            firstComponent = {
                Text(
                    text = "AI 여행지 추천",
                    style = AppTypography.titleMedium,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            },
            secondComponent = { RecommendPlace() }, padding = false
        )


    }
}

@Composable
fun Section(
    firstComponent: @Composable () -> Unit,
    secondComponent: @Composable () -> Unit,
    padding: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (padding) Modifier.padding(horizontal = 20.dp) else Modifier
            ),
        verticalArrangement = Arrangement.spacedBy(10.dp) // 두 컴포넌트 사이 간격 설정
    ) {
        firstComponent()
        secondComponent()
    }
}

@Composable
fun CheckIconTextField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                "이번 여행의 제목은 무엇인가요?",
                color = TextDarkGray,
                style = AppTypography.bodyMedium
            )
        },
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                VerticalDivider(
                    color = IconGray, modifier = Modifier
                        .height(32.dp) // 선 높이
                        .width(1.dp) // 선 너비
                )
                Spacer(modifier = Modifier.width(8.dp)) // 선과 아이콘 사이 간격
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check Icon",
                    tint = TextDarkGray
                )
            }
        },
        textStyle = TextStyle(color = TextBlack, fontSize = 16.sp),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = White,
            unfocusedContainerColor = White,
            focusedIndicatorColor = MainNavy,
            unfocusedIndicatorColor = MainNavy,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    )
}

@Composable
fun RecentHistoryPlace(count: Int = 0) {
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
                text = "아직 기록한 곳이 없습니다.",
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
            items(sampleHistories.toList()) { history ->
                HistoryCard(history, false)
            }
        }

    }
}

@Composable
fun RecommendPlace() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)

    ) {
        items(samplePlaces.toList()) { place ->
            PlaceCard(place)
        }
    }
}

@Composable
fun PlaceCard(place: Place) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(270.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray
        )
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            NameCard(place)
        }

    }
}

@Composable
fun HistoryCard(history: History, click: Boolean) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(270.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray
        )
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            NameCard(history)
        }

    }
}


@Composable
fun NameCard(data: Any) {
    when (data) {
        is Place -> {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(67.dp),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BlurBackgroundGray
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = data.name,
                        color = White,
                        style = AppTypography.bodyMedium
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //ImageVector.vectorResource(id = R.drawable.home_icon)
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.location_icon),
                            contentDescription = "Location",
                            tint = TextLightGray
                        )
                        Text(
                            text = data.address,
                            color = TextLightGray,
                            style = AppTypography.labelMedium
                        )
                    }
                }
            }
        }

        is History -> {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(67.dp),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BlurBackgroundGray
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = data.name,
                            color = White,
                            style = AppTypography.labelMedium
                        )
                        Text(text = data.title, color = White, style = AppTypography.labelLarge)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //ImageVector.vectorResource(id = R.drawable.home_icon)
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.location_icon),
                            contentDescription = "Location",
                            tint = TextLightGray
                        )
                        Text(
                            text = data.address,
                            color = TextLightGray,
                            style = AppTypography.labelMedium
                        )
                    }
                }
            }
        }
    }

}
