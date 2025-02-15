package com.example.golmokstar.ui.screens


import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import com.example.golmokstar.R
import com.example.golmokstar.ui.theme.AppTypography
import com.example.golmokstar.ui.theme.BackgroundSky
import com.example.golmokstar.ui.theme.BlurBackgroundGray
import com.example.golmokstar.ui.theme.ErrorRed
import com.example.golmokstar.ui.theme.IconGray
import com.example.golmokstar.ui.theme.MainNavy
import com.example.golmokstar.ui.theme.TextBlack
import com.example.golmokstar.ui.theme.TextDarkGray
import com.example.golmokstar.ui.theme.TextLightGray
import com.example.golmokstar.ui.theme.White

@Composable
fun MyPageScreen() {
    var showBellPopup by remember { mutableStateOf(false) } // 팝업 상태
    val friendRequests = listOf("문희삼사오육", "어쩌구", "저쩌", "구우", "블라블라") // 친구 요청 목록

    Column(modifier = Modifier.fillMaxSize().background(White)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            // 상단 바 (마이페이지 + 알림 버튼)
            MyPageTopBar(onBellClick = { showBellPopup = !showBellPopup })

            // 친구 요청 팝업
            if (showBellPopup) {
                FriendRequestPopup(
                    friendRequests = friendRequests,
                    onDismissRequest = { showBellPopup = false }
                )
            }
        }
        ProfileBox()
        FriendsListTitle()
        FriendsListScreen()
    }
}

@Composable
fun MyPageTopBar(onBellClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "마이페이지",
            style = AppTypography.titleMedium,
            modifier = Modifier.weight(1f) // 텍스트가 가로로 남은 공간 차지
        )

        Icon(
            painter = painterResource(id = R.drawable.bell), // 알림 아이콘
            contentDescription = "알림",
            modifier = Modifier
                .size(24.dp)
                .clickable { onBellClick() }
        )
    }
}

@Composable
fun FriendRequestPopup(
    friendRequests: List<String>,
    onDismissRequest: () -> Unit
) {
    Popup(
        onDismissRequest = onDismissRequest,
        offset = IntOffset(x = 360, y = 130)
    ) {
        Box(
            modifier = Modifier
                .border(1.dp, MainNavy)
                .background(White)
        ) {
            FriendRequestPopupContent(friendRequests = friendRequests)
        }
    }
}

@Composable
fun FriendRequestPopupContent(friendRequests: List<String>) {
    Box(
        modifier = Modifier
            .background(color = White)
            .width(220.dp)
            .padding(15.dp)
    ) {
        Column {
            Text(
                text = "친구 요청(${friendRequests.size})",
                style = AppTypography.labelLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Divider(
                color = IconGray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            friendRequests.forEach { friendRequest ->
                FriendRequestItem(friendRequest)
            }
        }
    }
}

@Composable
fun FriendRequestItem(friendRequest: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = friendRequest,
            style = AppTypography.labelMedium,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        FriendRequestButtons(friendRequest = friendRequest)
    }
}

@Composable
fun FriendRequestButtons(friendRequest: String) {
    Row {
        // 수락 버튼
        Button(
            onClick = {
                // 수락 동작
                println("$friendRequest 수락됨")
            },
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .padding(end = 4.dp)
                .width(50.dp)
                .height(25.dp),
            colors = ButtonDefaults.buttonColors(MainNavy),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "수락",
                color = White,
                fontSize = 12.sp,
                fontFamily = pretendardRegular
            )
        }

        // 거절 버튼
        Button(
            onClick = {
                // 거절 동작
                println("$friendRequest 거절됨")
            },
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MainNavy),
            border = BorderStroke(2.dp, MainNavy),
            modifier = Modifier
                .width(50.dp)
                .height(25.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "거절",
                color = MainNavy,
                fontSize = 12.sp,
                fontFamily = pretendardRegular
            )
        }
    }
}

@Composable
fun ProfileBox(travelCount: Int = 0) {
    var isEditingProfile by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("민지") }
    var selectedStyles by remember { mutableStateOf(listOf("힐링", "음식")) }
    var selectedProfileIndex by remember { mutableStateOf(0) }

    val context = LocalContext.current

    val unlockThresholds = listOf(0, 3, 6, 9, 12, 15)
    val unlockedProfiles = unlockThresholds.map { travelCount >= it }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BackgroundSky)
                .animateContentSize(animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing))
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(5.dp)
                            .clip(CircleShape)
                            .background(BlurBackgroundGray)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (isEditingProfile) {
                            BasicTextField(
                                value = userName,
                                onValueChange = { newValue ->
                                    val filteredText = newValue.take(6) // 6글자 제한
                                    userName = filteredText
                                },
                                textStyle = AppTypography.bodyMedium,
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .border(1.dp, BlurBackgroundGray, RoundedCornerShape(5.dp))
                                            .padding(10.dp)
                                    ){
                                        innerTextField()
                                    }
                                }
                            )
                            Text(
                                text = "여행 스타일",
                                fontSize = 12.sp,
                                fontFamily = pretendardRegular,
                                modifier = Modifier.padding(top = 10.dp)
                            )

                            // 수정 모드 : 여행 스타일 선택
                            Column(modifier = Modifier.padding(top = 15.dp)) {
                                val travelStyles = listOf("음식", "액티비티", "문화예술", "힐링", "자연", "쇼핑")

                                travelStyles.chunked(3).forEachIndexed { index, chunk ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(bottom = if (index < travelStyles.chunked(3).size - 1) 8.dp else 0.dp),
                                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                                    ) {
                                        chunk.forEach { style ->
                                            Button(
                                                onClick = {
                                                    if (selectedStyles.contains(style)) {
                                                        selectedStyles = selectedStyles.filterNot { it == style }
                                                    } else if (selectedStyles.size < 2) {
                                                        selectedStyles = selectedStyles + style
                                                    }
                                                },
                                                modifier = Modifier
                                                    .size(50.dp, 20.dp) // 스타일 버튼 크기 조정
                                                    .clip(RoundedCornerShape(30.dp))
                                                    .border(
                                                        width = 1.dp,
                                                        color = if (selectedStyles.contains(style)) MainNavy else Color.Transparent, // 선택되지 않은 버튼에만 테두리
                                                        shape = RoundedCornerShape(30.dp) // 버튼에 둥근 모서리 추가
                                                    )
                                                    .padding(0.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    if (selectedStyles.contains(style)) Color.White else MainNavy, // 선택된 버튼은 흰색, 아니면 MainNavy
                                                    contentColor = if (selectedStyles.contains(style)) MainNavy else Color.White // 선택된 버튼은 MainNavy, 아니면 흰색
                                                ),
                                                contentPadding = PaddingValues(0.dp)
                                            ) {
                                                Text(
                                                    text = style,
                                                    fontSize = 12.sp,
                                                    fontFamily = pretendardRegular
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = userName,
                                    style = AppTypography.bodyMedium,
                                    modifier = Modifier.weight(1f)
                                )

                                Text(
                                    text = "#1234",
                                    style = AppTypography.labelMedium,
                                    color = TextDarkGray,
                                    modifier = Modifier.padding(end = 30.dp)
                                )
                            }

                            Text(
                                text = "여행 스타일",
                                fontSize = 12.sp,
                                fontFamily = pretendardRegular,
                                modifier = Modifier.padding(top = 10.dp)
                            )

                            Row(
                                modifier = Modifier.padding(top = 12.dp)
                            ) {
                                selectedStyles.forEach { style ->
                                    Box(
                                        modifier = Modifier
                                            .size(70.dp, 25.dp)
                                            .padding(end = 8.dp)
                                            .clip(RoundedCornerShape(25.dp))
                                            .background(MainNavy),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = style,
                                            style = AppTypography.labelMedium,
                                            color = White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (isEditingProfile) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        (0 until 2).forEach { rowIndex ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                (0 until 3).forEach { colIndex ->
                                    val index = rowIndex * 3 + colIndex
                                    if(index < unlockedProfiles.size){
                                        Box(
                                            modifier = Modifier
                                                .size(85.dp)
                                                .clip(CircleShape)
                                                .background(if (unlockedProfiles[index]) BlurBackgroundGray
                                                else Color.Gray.copy(alpha = 0.3f))
                                                .border(
                                                    width = if (selectedProfileIndex == index) 1.dp else 0.dp,
                                                    color = if (selectedProfileIndex == index) MainNavy else (Color.Transparent),
                                                    shape = CircleShape
                                                )
                                                .clickable(enabled = unlockedProfiles[index]){
                                                    selectedProfileIndex = index
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (!unlockedProfiles[index]) {
                                                Text("🔒", fontSize = 20.sp, color = White)
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }

            IconButton(
                onClick = {
                    if (isEditingProfile) {
                        val nameLength = userName.length
                        if(nameLength in 2..6){
                            if (selectedStyles.size == 2) {
                                println("수정된 닉네임: ${userName}")
                                println("선택된 여행 스타일: $selectedStyles")
                                println("선택한 프로필 인덱스: $selectedProfileIndex")
                                isEditingProfile = false
                            } else {
                                Toast.makeText(context, "두 개의 스타일을 선택해야 합니다.", Toast.LENGTH_SHORT).show()
                            }
                        } else{
                            Toast.makeText(context, "닉네임은 2~6글자로 설정 가능합니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        isEditingProfile = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    painter = painterResource(id = if (isEditingProfile) R.drawable.profile_check else R.drawable.profile_edit),
                    contentDescription = "수정 버튼",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        // 🔴 닉네임 안내 메시지
        Spacer(modifier = Modifier.height(8.dp)) // 간격 조정
        if (isEditingProfile) {
            Text(
                text = "* 닉네임을 입력해주세요. 2~6글자로 설정 가능합니다.",
                style = AppTypography.labelSmall,
                color = ErrorRed,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 2.dp)
            )
        }
    }
}

@Composable
fun FriendsListTitle() {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // 좌우 끝으로 배치
            verticalAlignment = Alignment.CenterVertically // 세로 중앙 정렬
        ) {
            Text(
                text = "친구 목록",
                style = AppTypography.titleMedium,
                modifier = Modifier.weight(1f) //텍스트가 가로로 남은 공간 차지
            )

            Row(
                modifier = Modifier.clickable {
                    try {
                        clipboardManager.setText(AnnotatedString("http://google.com"))
                        Toast.makeText(context, "친구초대 링크가 복사되었습니다.", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(
                    text = "친구초대 링크",
                    style = AppTypography.labelMedium,
                    color = TextDarkGray
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.invite_link),
                    contentDescription = "친구초대",
                    tint = TextDarkGray,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun FriendsListScreen() {
    val friends: List<Friend> = listOf(
        Friend("여섯글자이름", listOf("음식", "액티비티", "문화예술", "힐링", "자연", "쇼핑"), 2),
        Friend("연주", listOf("음식", "문화예술"), 10),
        Friend("문희", listOf("문화예술"), 3),
        Friend("승민", listOf("음식", "액티비티", "쇼핑"), 0),
        Friend("어쩌구다", listOf("액티비티", "힐링", "자연", "쇼핑", "문화예술"), 331)
    )

    var showDialog by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
        items(items = friends) { friend ->
            FriendItem(friend.name, friend.styles, friend.travelCount)
        }

        // Add Friend Button at the end
        item {
            FriendsAddButton(onClick = { showDialog = true })
        }
    }

    // Show dialog if showDialog is true
    if (showDialog) {
        FriendsAddDialog(
            onDismiss = { showDialog = false },
            onFriendRequest = { membershipNumber ->
                // 친구 신청 로직 처리
                println("회원번호 ${membershipNumber}으로 친구 신청")
            }
        )
    }
}

data class Friend(
    val name: String,
    val styles: List<String>,
    val travelCount: Int
)

@Composable
fun FriendItem(name: String, styles: List<String>, travelCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .border(1.dp, TextLightGray, CircleShape)
                .background(Color.White)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .width(48.dp) // Fixed width for name
                .wrapContentHeight(), // Adjust height to text size
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = name,
                    style = AppTypography.labelMedium,
                    maxLines = 2,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Travel Styles
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(5.dp)
        ) {
            // First row (max 3 styles)
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                styles.take(3).forEach { style ->
                    Box(
                        modifier = Modifier
                            .size(50.dp, 20.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .background(MainNavy),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = style,
                            fontSize = 12.sp,
                            fontFamily = pretendardRegular,
                            color = White
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }

            // Second row (if more than 3 styles)
            if (styles.size > 3) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    styles.drop(3).forEach { style ->
                        Box(
                            modifier = Modifier
                                .size(50.dp, 20.dp)
                                .clip(RoundedCornerShape(30.dp))
                                .background(MainNavy),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = style,
                                fontSize = 12.sp,
                                fontFamily = pretendardRegular,
                                color = White
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Travel Count
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.width(60.dp)
        ) {
            Text(
                text = "$travelCount",
                style = AppTypography.labelLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Friends Add Button Composable
@Composable
fun FriendsAddButton(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(top = 10.dp)
    ) {
        // Circle Icon Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(60.dp)
                .border(1.dp, MainNavy, CircleShape)
                .background(Color.White)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "친구 추가",
                tint = MainNavy,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // "Add Friend" Text
        Text(
            text = "친구추가하기",
            style = AppTypography.labelMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsAddDialog(
    onDismiss: () -> Unit,
    onFriendRequest: (String) -> Unit
) {
    // 팝업 내부에 사용할 상태 변수
    var membershipNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(10.dp))
                .background(White)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                // 제목
                Text(
                    text = "친구 추가",
                    style = AppTypography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Divider(
                    color = IconGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "회원번호",
                        style = AppTypography.bodyMedium,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = "*",
                        style = AppTypography.bodyMedium,
                        color = ErrorRed
                    )
                }

                // 회원 번호 입력칸
                OutlinedTextField(
                    value = membershipNumber,
                    onValueChange = { membershipNumber = it },
                    label = {
                        Text(
                            text = "회원번호를 입력해주세요. ex) #0000",
                            style = AppTypography.labelMedium,
                            color = TextDarkGray
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MainNavy, // 포커스 상태일 때 아웃라인 색상
                        unfocusedBorderColor = MainNavy // 포커스가 아닐 때 아웃라인 색상
                    )
                )

                // 오류 메시지
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        style = AppTypography.labelSmall,
                        modifier = Modifier.padding(top = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 버튼들
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 취소 버튼
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50.dp),
                        border = BorderStroke(1.dp, MainNavy)
                    ) {
                        Text(
                            text = "취소",
                            color = MainNavy,
                            fontSize = 14.sp,
                            fontFamily = pretendardRegular
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // 친구 신청 버튼
                    Button(
                        onClick = {
                            // 회원 번호가 유효한지 확인
                            if (membershipNumber.isBlank()) {
                                errorMessage = "* 회원 번호를 입력해 주세요."
                            } else {
                                // 여기서 친구 신청 로직
                                onFriendRequest(membershipNumber)
                                onDismiss() // 다 끝나면 팝업 닫기
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(MainNavy)
                    ) {
                        Text(
                            text = "친구 신청",
                            color = White,
                            fontSize = 14.sp,
                            fontFamily = pretendardRegular
                        )
                    }
                }
            }
        }
    }
}


@Preview(name = "Pixel 5", device = "id:pixel_5",
    showBackground = true,
    showSystemUi = true)
@Composable
fun MyPageScreenPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        MyPageScreen()
    }
}

val pretendardRegular = FontFamily(
    Font(R.font.pretendard_regular)
)