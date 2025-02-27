package com.example.golmokstar.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.*
import com.example.golmokstar.R
import com.example.golmokstar.ui.components.TravelTitleField
import com.example.golmokstar.ui.theme.*
import com.example.golmokstar.viewmodel.Error
import com.example.golmokstar.viewmodel.Travel
import com.example.golmokstar.viewmodel.TravelState
import com.example.golmokstar.viewmodel.TravelViewModel


data class Place(
    val name: String, val address: String, val imageUrl: String
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

data class Friend(
    val name: String
)

val sampleFriends = listOf(
    Friend(name = "하늘빛"),
    Friend(name = "별의길"),
    Friend(name = "검은늑대"),
    Friend(name = "빠른발걸음"),
    Friend(name = "달의여행"),
    Friend(name = "화염의검"),
    Friend(name = "눈의여왕")
)

val samplePlaces = listOf(
    Place(
        name = "남산타워",
        address = "서울특별시 용산구",
        imageUrl = "https://source.unsplash.com/400x300/?seoul,tower"
    ), Place(
        name = "해운대 해수욕장",
        address = "부산광역시 해운대구",
        imageUrl = "https://source.unsplash.com/400x300/?beach,ocean"
    ), Place(
        name = "성산일출봉",
        address = "제주도 서귀포시",
        imageUrl = "https://source.unsplash.com/400x300/?jeju,mountain"
    ), Place(
        name = "안목해변",
        address = "강원도 강릉시",
        imageUrl = "https://source.unsplash.com/400x300/?cafe,beach"
    ), Place(
        name = "한옥마을",
        address = "전라북도 전주시 완산구",
        imageUrl = "https://source.unsplash.com/400x300/?hanok,village"
    )
)

val sampleHistories = listOf(
    History(
        name = "올림픽공원",
        address = "서울특별시 송파구",
        imageUrl = "https://source.unsplash.com/400x300/?seoul,park",
        rating = 4.7,
        title = "올림픽공원 산책",
        content = "서울 올림픽공원에서 자전거를 타며 여유롭게 산책을 즐겼다. 공원 내의 평화로운 분위기가 인상적이었다.",
        date = "2025.03.10"
    ), History(
        name = "불국사",
        address = "경상북도 경주시",
        imageUrl = "https://source.unsplash.com/400x300/?temple,kyongju",
        rating = 4.9,
        title = "경주 불국사 탐방",
        content = "불국사의 아름다운 건축과 고요한 분위기에 감동했다. 역사적인 의미를 되새기며 조용히 산책했다.",
        date = "2025.04.05"
    ), History(
        name = "평창",
        address = "강원도 평창군",
        imageUrl = "https://source.unsplash.com/400x300/?mountain,pyeongchang",
        rating = 4.8,
        title = "평창 스키 여행",
        content = "평창에서 스키를 타며 겨울을 만끽했다. 눈 덮인 산의 아름다움과 스키장의 즐거움이 인상 깊었다.",
        date = "2025.02.25"
    ), History(
        name = "북촌 한옥마을",
        address = "서울특별시 종로구",
        imageUrl = "https://source.unsplash.com/400x300/?hanok,seoul",
        rating = 4.6,
        title = "한옥마을 탐방",
        content = "북촌 한옥마을에서 전통적인 분위기를 느꼈다. 한옥의 아름다움과 조용한 골목이 마음에 들었다.",
        date = "2025.05.12"
    ), History(
        name = "덕진공원",
        address = "전라북도 전주시 덕진구",
        imageUrl = "https://source.unsplash.com/400x300/?park,jeonju",
        rating = 4.4,
        title = "전주 덕진공원 산책",
        content = "덕진공원에서 피크닉을 즐기며 봄날의 따뜻한 햇살을 만끽했다. 공원의 아름다움에 감탄했다.",
        date = "2025.04.20"
    )
)

//
//enum class TravelState {
//    NONE, // 아무것도 아님
//    SETTING, // 설정 중
//    TRAVELING // 여행 중
//}
//
//data class TravelPlan(
//    val title: String, // 여행 제목 (필수)
//    val startDate: String, // 시작 날짜 (필수, 예: "2025/06/01")
//    val endDate: String, // 종료 날짜 (필수, 예: "2025/06/07")
//    val friends: List<String> = emptyList() // 선택 요소: 친구 리스트 (기본값: 빈 리스트)
//)



@Composable
fun HomeScreen(travelViewModel: TravelViewModel) {

    // FocusManager 객체 생성
    val focusManager = LocalFocusManager.current

    val scrollState = rememberScrollState()

    val count = 1

    val travelState by travelViewModel.travelState.collectAsState()
    val currentTravel by travelViewModel.currentTravel.collectAsState()
    val currentError by travelViewModel.currentError.collectAsState()

    var travelPlan by remember {
        mutableStateOf(
            Travel(id = "", title = "", startDate = "", endDate = "")
        )
    }

    LaunchedEffect(Unit) {
        travelViewModel.getCurrentTravel()
    }

    LaunchedEffect(currentTravel) {
        currentTravel?.let {
            travelPlan = Travel(
                title = it.title,
                startDate = it.startDate,
                endDate = it.endDate
            )
        }
    }



    Column(
        verticalArrangement = Arrangement.spacedBy(35.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
            .verticalScroll(scrollState)
            .padding(top = 20.dp, bottom = 50.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    // 화면의 빈 곳을 클릭하면 포커스를 해제
                    focusManager.clearFocus()
                    if (travelPlan.title.isEmpty()) {
                        //
                    }
                })
            },
    ) {
        Section(firstComponent = {
            Text(
                text = "여행 등록하기", style = AppTypography.titleMedium
            )
        }, secondComponent = {
            CreateTravelSection(travelPlan,
                onChangeTitle = { travelPlan = travelPlan.copy(title = it) },
                onChangeStartDate = { travelPlan = travelPlan.copy(startDate = it) },
                onChangeEndDate = { travelPlan = travelPlan.copy(endDate = it) },
                travelState,
                changeTravelState = {
                    if(travelState != TravelState.SETTING) {
                        travelViewModel.validateAndSetTravelState()
                    } else {
                        travelViewModel.setTravel(travelPlan)
                    }
                }, currentError
            )
        }, padding = 20
        )

        Section(
            firstComponent = {
                Text(
                    text = "최근 기록한 장소", style = AppTypography.titleMedium, modifier = Modifier.then(
                        if (count != 0) Modifier.padding(horizontal = 20.dp) else Modifier
                    )
                )
            },
            secondComponent = { RecentHistoryPlace(count = count) },
            padding = (if (count == 0) 20 else 0)
        )

        Section(firstComponent = {
            Text(
                text = "AI 여행지 추천",
                style = AppTypography.titleMedium,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }, secondComponent = { RecommendPlace() }, padding = 0
        )


    }
}

@Composable
fun Section(
    firstComponent: @Composable () -> Unit, secondComponent: @Composable () -> Unit, padding: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (padding >= 0) Modifier.padding(horizontal = padding.dp) else Modifier
            ), verticalArrangement = Arrangement.spacedBy(10.dp) // 두 컴포넌트 사이 간격 설정
    ) {
        firstComponent()
        secondComponent()
    }
}


@Composable
fun CreateTravelSection(
    travelPlan: Travel,
    onChangeTitle: (String) -> Unit,
    onChangeStartDate: (String) -> Unit,
    onChangeEndDate: (String) -> Unit,
    currentState: TravelState,
    changeTravelState: () -> Unit,
    currentError: Error,

    ) {
    TravelTitleField(travelPlan, onChangeTitle, onChangeStartDate, onChangeEndDate, currentState, changeTravelState ,currentError)
}


@Composable
fun ClickableLink() {
    val context = LocalContext.current  // 컴포저블 함수 안에서만 사용해야 합니다.

    //임시로 구글 연결
    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = "URL", annotation = "https://www.google.com/")
        append("친구초대 링크")
        pop()
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ClickableText(text = annotatedString,
            style = AppTypography.labelMedium.copy(color = TextDarkGray),
            onClick = { offset ->
                annotatedString.getStringAnnotations("URL", offset, offset).firstOrNull()
                    ?.let { annotation ->
                        // 링크를 열기 위해 Intent 사용
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                        context.startActivity(intent)  // 컴포저블 내에서 context를 안전하게 사용
                    }
            })
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.link_icon),
            contentDescription = "Link",
            tint = TextDarkGray
        )
    }
}

@Composable
fun SelectFriends() {
    var selectedFriends by remember { mutableStateOf<List<Friend>>(emptyList()) }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) {
        items(sampleFriends.toList()) { friend ->
            FriendCirCle(friend = friend,
                isSelected = selectedFriends.contains(friend), // 선택된 친구와 비교
                onClick = {
                    // 친구가 선택된 상태라면 리스트에서 제거, 아니면 추가
                    selectedFriends = if (selectedFriends.contains(friend)) {
                        selectedFriends - friend // 이미 선택된 아이템을 클릭하면 제거
                    } else {
                        selectedFriends + friend // 새로 선택된 아이템을 추가
                    }
                })
        }

        item {
            AddFriend()
        }
    }
}

@Composable
fun FriendCirCle(friend: Friend, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .size(50.dp) // 원하는 크기로 설정
            .clip(CircleShape) // 원형 모양으로 자르기
            .background(White) // 배경색 (선택사항)
            .border(1.dp, if (isSelected) MainNavy else TextLightGray, CircleShape)
            .clickable { onClick() }) {
//        Image(
//
//        )
        }
        Text(text = friend.name, style = AppTypography.labelMedium, color = TextBlack)
    }

}

@Composable
fun AddFriend() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(50.dp) // 원하는 크기로 설정
                .clip(CircleShape) // 원형 모양으로 자르기
                .border(1.dp, MainNavy, CircleShape), contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.plus_icon),
                contentDescription = "Plus",
                tint = MainNavy
            )
        }
        Text(text = "친구 추가하기", style = AppTypography.labelMedium, color = TextBlack)
    }
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
                HistoryCard(history)
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
fun HistoryCard(history: History) {
    var isClicked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(200.dp)
            .height(270.dp)
            .clickable { isClicked = !isClicked },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray
        )
    ) {
        if (!isClicked) {
            Column(
                modifier = Modifier.padding(5.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                NameCard(history)
            }
        } else {
            OverCard(history)
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
                        text = data.name, color = White, style = AppTypography.bodyMedium
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = data.name, color = White, style = AppTypography.bodyMedium
                        )
                        Text(
                            text = data.title,
                            color = TextLightGray,
                            style = AppTypography.labelLarge
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(1.dp)
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

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp),

                            ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.star_icon),
                                contentDescription = "Star",
                                tint = TextLightGray

                            )
                            Text(
                                text = data.rating.toString(),
                                color = TextLightGray,
                                style = AppTypography.labelMedium
                            )
                        }
                    }


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
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.star_icon),
                        contentDescription = "Star",
                        tint = White
                    )
                    Text(
                        text = history.rating.toString(),
                        color = White,
                        style = AppTypography.labelMedium
                    )
                }
            }

            Text(text = history.address, color = TextLightGray, style = AppTypography.labelMedium)
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
}
