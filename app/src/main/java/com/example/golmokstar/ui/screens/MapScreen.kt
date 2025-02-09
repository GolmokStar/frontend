package com.example.golmokstar.ui.screens

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.golmokstar.R
import com.example.golmokstar.ui.theme.AppTypography
import com.example.golmokstar.ui.theme.BlurBackgroundGray
import com.example.golmokstar.ui.theme.IconGray
import com.example.golmokstar.ui.theme.MarkerRed
import com.example.golmokstar.ui.theme.TextDarkGray
import com.example.golmokstar.ui.theme.White
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("선택하세요") }
    val items = listOf("옵션 1", "옵션 2", "옵션 3")


    val context = LocalContext.current
    val dataStore = DataStoreModule(context)
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    var currentLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var savedLatitude by remember { mutableDoubleStateOf(0.0) }
    var savedLongitude by remember { mutableDoubleStateOf(0.0) }
    var selectedMarkerLocation by remember { mutableStateOf<LatLng?>(null) }
    var selectedAddress by remember { mutableStateOf("") }

    fun getAddressFromLatLng(latLng: LatLng) {
        val geocoder = Geocoder(context)
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            // addresses가 null이 아니고, 비어있지 않은 경우에만 처리
            if (!addresses.isNullOrEmpty()) {
                selectedAddress = addresses[0]?.getAddressLine(0) ?: "주소를 가져올 수 없습니다."
            } else {
                selectedAddress = "주소를 가져올 수 없습니다."
            }
        } catch (e: Exception) {
            selectedAddress = "주소를 가져오는 데 실패했습니다."
        }
    }


    // 실시간 위치 업데이트를 위한 LocationCallback 설정
    val locationRequest = LocationRequest.create().apply {
        interval = 5000 // 5초마다 위치 업데이트
        fastestInterval = 2000 // 2초 간격으로 가장 빠른 위치 업데이트
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    currentLocation = LatLng(location.latitude, location.longitude)
                }
            }
        }
    }

    // 권한 체크 후 실시간 위치 가져오기
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper() // 메인 스레드에서 처리
            )
        } else {
            // 권한 요청
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            Toast.makeText(context, "위치 권한을 허용해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    // DataStore에서 저장된 위도, 경도 가져오기
    LaunchedEffect(Unit) {
        dataStore.getLatitude.collect {
            savedLatitude = it
        }
    }

    LaunchedEffect(Unit) {
        dataStore.getLongitude.collect {
            savedLongitude = it
        }
    }

    // 카메라 위치 설정
    val cameraPositionState = rememberCameraPositionState {
        // currentLocation이 갱신되면 카메라 위치를 자동으로 업데이트
        if (currentLocation.latitude != 0.0 && currentLocation.longitude != 0.0) {
            position = CameraPosition.fromLatLngZoom(currentLocation, 17f)
        }
    }

    // 위치가 업데이트 될 때마다 카메라 이동
    LaunchedEffect(currentLocation) {
        if (currentLocation.latitude != 0.0 && currentLocation.longitude != 0.0) {
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(currentLocation, 17f)
            )
        }
    }

    val uiSettings = remember {
        MapUiSettings(
            myLocationButtonEnabled = false, // 기본 내 위치 버튼을 사용하지 않음
            zoomControlsEnabled = false,
            rotationGesturesEnabled = false
        )
    }

    val properties by remember { mutableStateOf(
        MapProperties(
            isMyLocationEnabled = true
        )) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings,
            onMapClick = { latLng ->
                selectedMarkerLocation = latLng // 클릭한 위치에 마커 설정
                getAddressFromLatLng(latLng)
            }
        ) {
            // 저장된 위도, 경도가 있으면 마커 표시
            if (savedLatitude != 0.0 && savedLongitude != 0.0) {
                Marker(
                    state = MarkerState(position = LatLng(savedLatitude, savedLongitude)),
                    title = "찜한 위치",
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.markedred)
                )
            }

            // 지도 클릭 후 추가된 마커
            selectedMarkerLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "선택한 위치",
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.markedred)
                )
            }
        }

        // 내 위치 버튼 → 오른쪽 하단에 배치
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
        ) {
            IconButton(
                onClick = {
                    cameraPositionState.move(
                        CameraUpdateFactory.newLatLngZoom(currentLocation, 17f)
                    )
                },
                modifier = Modifier
                    .size(40.dp) // 버튼 크기를 키움
                    .background(Color.White, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "내 위치 이동",
                    modifier = Modifier
                        .size(25.dp) // 이미지 크기를 적절히 키움
                )
            }
        }

        // 선택한 위치 정보 박스
        selectedMarkerLocation?.let { location ->
            Box(
                modifier = Modifier
                    .height(140.dp)
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .border(1.dp, MarkerRed, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                // Column을 사용하여 세로로 정렬
                Column(
                    modifier = Modifier
                        .fillMaxHeight() // 박스의 높이를 가득 채우도록 수정
                        .fillMaxWidth(), // 가로 크기도 채우도록
                    verticalArrangement = Arrangement.Center, // 세로 중앙 정렬
                ) {
                    // 첫 번째 줄 (아이콘 + 주소 | 아이콘 + 텍스트)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.markedred),
                            contentDescription = "위치",
                            modifier = Modifier.size(15.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = selectedAddress,
                            style = AppTypography.labelMedium,
                            modifier = Modifier.weight(1f) // 나머지 공간 차지
                        )

                        Icon(
                            imageVector = Icons.Default.Update,
                            tint = MarkerRed,
                            contentDescription = "남은 시간",
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "남은 시간",
                            style = AppTypography.bodyMedium,
                            color = MarkerRed
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 두 번째 줄 (이름 + 날짜 + 버튼)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(
                            modifier = Modifier.weight(1f), // 왼쪽 정렬
                        ) {
                            Text("이름", style = AppTypography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("2025.02.08", style = AppTypography.labelSmall, color = TextDarkGray)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { /* TODO: 버튼 클릭 동작 */ },
                            modifier = Modifier
                                .height(35.dp)
                                .width(90.dp),
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MarkerRed,
                                contentColor = White
                            )
                        ) {
                            Text("방문하기", style = AppTypography.labelMedium, color = White)
                        }
                    }
                }
            }
        }


        Box(
            modifier = Modifier
                .padding(end = 16.dp, top = 16.dp)
                .size(100.dp, 110.dp)
                .background(BlurBackgroundGray, shape = RoundedCornerShape(20.dp))
                .align(Alignment.TopEnd) // 자식 Box를 오른쪽 상단에 배치
        ) {
            Column(
                modifier = Modifier.fillMaxSize(), // Column이 부모 Box에 맞게 채워지도록 설정
                verticalArrangement = Arrangement.Center, // 수직 정렬 중앙
                horizontalAlignment = Alignment.CenterHorizontally // 수평 정렬 중앙
            ) {
                // "찜한 장소" 항목
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.markedred),
                        contentDescription = "찜한 장소 아이콘",
                        modifier = Modifier
                            .size(25.dp)
                            .padding(start = 8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "찜한 장소",
                        color = White,
                        style = AppTypography.bodyMedium
                    )
                }

                // "방문 장소" 항목
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.markedyellow),
                        contentDescription = "방문 장소 아이콘",
                        modifier = Modifier
                            .size(25.dp)
                            .padding(start = 8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "방문 장소",
                        color = White,
                        style = AppTypography.bodyMedium

                    )
                }

                // "기록 장소" 항목
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.markedblue),
                        contentDescription = "기록 장소 아이콘",
                        modifier = Modifier
                            .size(25.dp)
                            .padding(start = 8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "기록 장소",
                        color = White,
                        style = AppTypography.bodyMedium
                    )
                }
            }
        }

        // 상단 왼쪽에 드롭다운 배치
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }, // 드롭다운을 클릭했을 때 열리고 닫히게 함
            modifier = Modifier
                .padding(16.dp)
                .width(180.dp)
                .height(50.dp)
                .align(Alignment.TopStart)
        ) {
            OutlinedTextField(
                value = selectedItem,
                onValueChange = {}, // 값 변경 불가능하게 설정
                readOnly = true, // 읽기 전용으로 설정
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    // 아이콘 클릭 시 드롭다운 열리거나 닫히게 함
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
                textStyle = AppTypography.bodyMedium
            )

            // 드롭다운 메뉴
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }, // 드롭다운 외부 클릭 시 닫히게 함
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
                            expanded = false // 아이템 클릭 시 드롭다운 닫히게 함
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMapScreen() {
    MapScreen()
}