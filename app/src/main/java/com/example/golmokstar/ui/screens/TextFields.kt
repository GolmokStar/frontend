package com.example.golmokstar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.golmokstar.R
import com.example.golmokstar.ui.theme.AppTypography
import com.example.golmokstar.ui.theme.IconGray
import com.example.golmokstar.ui.theme.MainNavy
import com.example.golmokstar.ui.theme.TextDarkGray
import com.example.golmokstar.ui.theme.White

enum class TravelState {
    NONE, // 아무것도 아님
    SETTING, // 설정 중
    TRAVELING // 여행 중
}

data class TravelPlan(
    val title: String, // 여행 제목 (필수)
    val startDate: String, // 시작 날짜 (필수, 예: "2025/06/01")
    val endDate: String, // 종료 날짜 (필수, 예: "2025/06/07")
    val friends: List<String> = emptyList() // 선택 요소: 친구 리스트 (기본값: 빈 리스트)
)

enum class Error {
    NONE, // 에러 없음
    Title, // 제목 없음
    Date, // 날짜 없음
    Both, // 둘 다 없음
}

@Preview
@Composable
fun Screen() {
    var nickname by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }

    var travelPlan by remember {
        mutableStateOf(
            TravelPlan(title = "", startDate = "", endDate = "")
        )
    }

    var currentState by remember { mutableStateOf(TravelState.NONE) }

    var currentError by remember { mutableStateOf(Error.NONE)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        NickNameTextField(nickname, onChange = { nickname = it })
        BirthdateField(birthdate, onChange = { birthdate = it })
        TravelTitleField(
            travelPlan.title,
            onChange = { travelPlan = travelPlan.copy(title = it) },
            currentState,
            changeTravelState = { state ->
                when (state) {
                    TravelState.SETTING -> currentState = state
                    TravelState.TRAVELING ->
                        if (travelPlan.title.isNotEmpty() && travelPlan.startDate.isNotEmpty() && travelPlan.endDate.isNotEmpty()) {
                            currentError = Error.NONE
                            currentState = state
                        } else if (travelPlan.title.isEmpty() && (travelPlan.startDate.isEmpty() || travelPlan.endDate.isEmpty())) {
                            currentError = Error.Both
                        } else if(travelPlan.title.isEmpty()) {
                            currentError = Error.Title
                        } else if (travelPlan.startDate.isEmpty() || travelPlan.endDate.isEmpty()) {
                            currentError = Error.Date
                        }

                    else -> {}
                }
            },
        )
        TravelDateField(travelPlan.startDate, onChange = {travelPlan = travelPlan.copy(startDate = it)})
        TravelDateField(travelPlan.endDate, onChange = {travelPlan = travelPlan.copy(endDate = it)})

        Text(travelPlan.title)
        Text(travelPlan.startDate)
        Text(travelPlan.endDate)
        Text(currentState.toString())
        Text(currentError.toString())
    }
}


@Composable
fun NickNameTextField(nickname: String, onChange: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = nickname,
        onValueChange = {
            if (it.matches(Regex("^[a-zA-Z가-힣]{0,8}$"))) {  // 영어, 한글만 허용
                onChange(it)
            }
        }, keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }
        ),
        textStyle = AppTypography.bodyMedium,
        decorationBox = { innerTextField ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(White, RoundedCornerShape(10.dp))
                    .border(1.dp, MainNavy, RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (nickname.isEmpty()) {
                    Text("닉네임을 입력해주세요", color = TextDarkGray, style = AppTypography.bodyMedium)
                }
                innerTextField() // 실제 텍스트 입력 필드
            }
        }
    )
}


@Composable
fun BirthdateField(birthdate: String, onChange: (String) -> Unit) {
    //String을 받아서 String으로 내보냄, 이후 상황에 따라 OnChange에서 자료형 변환을 해야할 수도 있음

    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = birthdate,
        onValueChange = { newValue ->
            val digitsOnly = newValue.filter { it.isDigit() }
            if (digitsOnly.length <= 8) {
                onChange(digitsOnly)
            }
        },
        textStyle = AppTypography.bodyMedium,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ), // 숫자 키보드
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        visualTransformation = DateTransformation(),
        decorationBox = { innerTextField ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(White, RoundedCornerShape(10.dp))
                    .border(1.dp, MainNavy, RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.date_icon),
                    contentDescription = "Date",
                    tint = MainNavy,
                )

                Spacer(modifier = Modifier.width(4.dp))

                Box(
                    modifier = Modifier.weight(1f), // 입력 필드 확장
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (birthdate.isEmpty()) {
                        Text(
                            "YYYY / MM / DD",
                            color = TextDarkGray,
                            style = AppTypography.bodyMedium
                        )
                    }
                    innerTextField() // 실제 텍스트 입력 필드
                }
            }
        }
    )
}


@Composable
fun TravelTitleField(
    travelTitle: String,
    onChange: (String) -> Unit,
    currentState: TravelState,
    changeTravelState: (TravelState) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    BasicTextField(
        value = travelTitle,
        onValueChange = { new ->
            if (new.length <= 8) {
                onChange(new)
            }
        },
        textStyle = AppTypography.bodyMedium,
        modifier = Modifier
            .focusRequester(focusRequester) // 포커스를 감지하기 위한 FocusRequester 적용
            .onFocusChanged { focusState ->
                if (focusState.isFocused && currentState == TravelState.NONE) { // 포커스 된 경우 true
                    changeTravelState(TravelState.SETTING)
                }
            },
        decorationBox = { innerTextField ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(White, RoundedCornerShape(10.dp))
                    .border(1.dp, MainNavy, RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier.weight(1f), // 입력 필드 확장
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (travelTitle.isEmpty()) {
                        Text(
                            "이번 여행의 제목은 무엇인가요?",
                            color = TextDarkGray,
                            style = AppTypography.bodyMedium
                        )
                    }
                    innerTextField() // 실제 텍스트 입력 필드
                }
                when (currentState) {
                    TravelState.SETTING -> Text(
                        "설정중",
                        color = TextDarkGray,
                        style = AppTypography.labelSmall
                    )

                    TravelState.TRAVELING -> Text(
                        "여행중",
                        color = TextDarkGray,
                        style = AppTypography.labelSmall
                    )

                    else -> {}
                }
                VerticalDivider(
                    modifier = Modifier.height(32.dp),
                    thickness = 1.dp,
                    color = TextDarkGray
                )
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Date",
                    tint = IconGray,

                    modifier = Modifier.clickable {
                        if (currentState == TravelState.SETTING) {
                            changeTravelState(TravelState.TRAVELING) // 상태가 SETTING일 때만 여행 시작 가능
                        }
                    }
                )
            }
        }
    )
}


@Composable
fun TravelDateField(date: String, onChange: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = date,
        onValueChange = { newValue ->
            val digitsOnly = newValue.filter { it.isDigit() }
            if (digitsOnly.length <= 8) {
                onChange(digitsOnly)
            }
        },
        textStyle = AppTypography.bodyMedium,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ), // 숫자 키보드
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        visualTransformation = DateTransformation(),
        decorationBox = { innerTextField ->
            Row(
                Modifier
                    .width(150.dp)
                    .height(44.dp)
                    .background(White, RoundedCornerShape(10.dp))
                    .border(1.dp, MainNavy, RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f), // 입력 필드 확장
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (date.isEmpty()) {
                        Text(
                            "YYYY / MM / DD",
                            color = TextDarkGray,
                            style = AppTypography.bodyMedium
                        )
                    }
                    innerTextField() // 실제 텍스트 입력 필드
                }
            }
        }
    )
}


// YYYY/MM/DD 포맷팅
private class DateTransformation() : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return dateFilter(text)
    }

    private fun dateFilter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 3 || i == 5) out += "/"
        }

        val numberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 5) return offset + 1
                if (offset <= 8) return offset + 2
                return 10
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                if (offset <= 10) return offset - 2
                return 8
            }
        }

        return TransformedText(AnnotatedString(out), numberOffsetTranslator)
    }
}