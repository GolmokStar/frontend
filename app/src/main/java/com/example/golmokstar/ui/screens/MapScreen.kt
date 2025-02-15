package com.example.golmokstar.ui.screens


import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.golmokstar.ui.theme.AppTypography
import com.example.golmokstar.ui.theme.BlurBackgroundGray
import com.example.golmokstar.ui.theme.IconGray
import com.example.golmokstar.ui.theme.MainNavy
import com.example.golmokstar.ui.theme.MarkerBlue
import com.example.golmokstar.ui.theme.MarkerRed
import com.example.golmokstar.ui.theme.MarkerYellow
import com.example.golmokstar.ui.theme.TextDarkGray
import com.example.golmokstar.ui.theme.White
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val dataStore = DataStoreModule(context)
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val activity = context as? Activity

    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("전체") }
    val items = listOf("옵션 1", "옵션 2", "옵션 3")

    var currentLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var savedLatitude by remember { mutableDoubleStateOf(0.0) }
    var savedLongitude by remember { mutableDoubleStateOf(0.0) }
    var selectedMarkerLocation by remember { mutableStateOf<LatLng?>(null) }
    var selectedAddress by remember { mutableStateOf("") }
    var showLocationBox by remember { mutableStateOf(false) } // 박스 열기/닫기 상태 추가
    var permissionGranted by remember { mutableStateOf(false) } // 권한이 허용되었는지 여부
    var visibleBoxState by remember { mutableStateOf<String?>(null) }
    var markerColor by remember { mutableStateOf(MarkerRed) } // 기본 색상 설정


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

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission())
    { isGranted -> permissionGranted = isGranted // 권한 허용 여부 상태 업데이트
        // 권한이 허용되었을 때 위치 업데이트 요청
        if (isGranted) { fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper()) }
        else { Toast.makeText(context, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show() }
    }

    // 권한 체크 후 실시간 위치 가져오기
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } else { activity?.let { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) } }
        dataStore.getLatitude.collect { savedLatitude = it }
        dataStore.getLongitude.collect { savedLongitude = it }
    }

    // 카메라 위치 설정
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 17f)
    }

    // 위치가 업데이트 될 때마다 카메라 이동
    LaunchedEffect(currentLocation) {
        if (currentLocation.latitude != 0.0 && currentLocation.longitude != 0.0) {
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(currentLocation, 17f))
        }
    }

    val uiSettings = remember { MapUiSettings(myLocationButtonEnabled = false, zoomControlsEnabled = false, rotationGesturesEnabled = false) }
    val properties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }

    Box(modifier = Modifier.fillMaxSize()) {
        // 권한이 허용되지 않았으면 지도 렌더링을 하지 않음
        if (permissionGranted) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = properties,
                uiSettings = uiSettings,
                onMapClick = { latLng ->
                    // 지도 클릭 시: 마커 위치와 박스를 초기화합니다.
                    selectedMarkerLocation = latLng
                    markerColor = MarkerRed // 마커 색상도 빨간색으로 초기화
                    visibleBoxState = "Red" // 박스를 빨간 박스로 초기화
                    showLocationBox = true // 위치 박스 보여주기
                    getAddressFromLatLng(latLng)

                }
            ) {
                // 저장된 위도, 경도가 있으면 마커 표시
                if (savedLatitude != 0.0 && savedLongitude != 0.0) {
                    Marker(
                        state = MarkerState(position = LatLng(savedLatitude, savedLongitude)),
                        title = "찜한 위치",
                        icon = redMarkerPin(context) // 초기 마커는 빨간색
                    )
                }

                // 지도 클릭 후 추가된 마커
                selectedMarkerLocation?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "선택한 위치",
                        icon = when (markerColor) {
                            MarkerRed -> redMarkerPin(context)
                            MarkerYellow -> yellowMarkerPin(context)
                            MarkerBlue -> blueMarkerPin(context)
                            else -> redMarkerPin(context) // 기본 색상으로 처리
                        }
                    )
                }
            }

            // 내 위치 버튼 → 오른쪽 하단에 배치
            FloatingActionButton(onClick = {
                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(currentLocation, 17f))
            }, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp), containerColor = White) {
                Icon(Icons.Default.MyLocation, contentDescription = "내 위치 이동", tint = MainNavy  )
            }


            // 선택된 위치 정보 박스
            AnimatedVisibility(
                visible = selectedMarkerLocation != null,
                enter = fadeIn(tween(300)) + slideInVertically { it / 2 },
                exit = fadeOut(tween(600)) + slideOutVertically { it }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // RedBox 예시
                    if (visibleBoxState == null || visibleBoxState == "Red") {
                        RedBox(
                            address = selectedAddress,
                            date = "2025-02-25",
                            name = "성심당",
                            onBoxClick = { selectedMarkerLocation = null },
                            onButtonClick = {
                                // RedBox 버튼 클릭 시 YellowBox로 전환
                                visibleBoxState = "Yellow"
                                markerColor = MarkerYellow
                            }
                        )
                    }

                    // YellowBox 예시
                    if (visibleBoxState == "Yellow") {
                        YellowBox(
                            address = selectedAddress,
                            date = "2025-02-25",
                            name = "성심당",
                            onBoxClick = { selectedMarkerLocation = null },
                            onButtonClick = {
                                // YellowBox 버튼 클릭 시 BlueBox로 전환
                                visibleBoxState = "Blue"
                                markerColor = MarkerBlue
                            },
                            topLeftText = "입닫고맛잇는빵먹기"
                        )
                    }

                    // BlueBox 예시
                    if (visibleBoxState == "Blue") {
                        BlueBox(
                            address = selectedAddress,
                            date = "2025-02-25",
                            name = "성심당",
                            onButtonClick = { },
                            onBoxClick = { selectedMarkerLocation = null }, // 클릭 시 처리
                            extraText = "입닫고맛잇는빵먹기"
                        )
                    }
                }
            }



            Box(
                modifier = Modifier
                    .padding(end = 16.dp, top = 16.dp)
                    .size(100.dp, 110.dp)
                    .background(BlurBackgroundGray, shape = RoundedCornerShape(20.dp))
                    .align(Alignment.TopEnd) // 오른쪽 상단에 배치
            ) {
                // Box 안의 목록
                LocationItemList()
            }


            // 상단 왼쪽에 드롭다운 배치
            Box(modifier = Modifier.fillMaxSize()) {
                // 드롭다운 컴포넌트 사용
                DropdownMenuWithIcon(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    selectedItem = selectedItem,
                    onItemSelected = { selectedItem = it },
                    items = items,
                    modifier = Modifier.align(Alignment.TopStart) // 드롭다운 위치를 Box 내에서 제어
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuWithIcon(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    items: List<String>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            modifier = Modifier
                .padding(16.dp)
                .width(180.dp)
                .height(50.dp)
        ) {
            OutlinedTextField(
                value = selectedItem,
                onValueChange = {}, // 값 변경 불가능하게 설정
                readOnly = true, // 읽기 전용으로 설정
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    // 아이콘 클릭 시 드롭다운 열리거나 닫히게 함
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
                textStyle = AppTypography.bodyMedium
            )

            // 드롭다운 메뉴
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }, // 드롭다운 외부 클릭 시 닫히게 함
                modifier = Modifier.background(White)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item, style = AppTypography.bodyMedium, color = TextDarkGray) },
                        onClick = {
                            onItemSelected(item) // 아이템 클릭 시 선택된 아이템 설정
                            onExpandedChange(false) // 드롭다운 닫기
                        }
                    )
                }
            }
        }
    }
}



// 위치 목록 UI 컴포넌트
@Composable
fun LocationItemList() {
    Column(
        modifier = Modifier.fillMaxSize(), // Column이 부모 Box에 맞게 채워지도록 설정
        verticalArrangement = Arrangement.Center, // 수직 정렬 중앙
        horizontalAlignment = Alignment.CenterHorizontally // 수평 정렬 중앙
    ) {
        // "찜한 장소" 항목
        LocationItem(text = "찜한 장소", icon = { RedMarkerIcon(modifier = Modifier.size(25.dp).padding(start = 8.dp)) })
        // "방문 장소" 항목
        LocationItem(text = "방문 장소", icon = { YellowMarkerIcon(modifier = Modifier.size(25.dp).padding(start = 8.dp)) })
        // "기록 장소" 항목
        LocationItem(text = "기록 장소", icon = { BlueMarkerIcon(modifier = Modifier.size(25.dp).padding(start = 8.dp)) })
    }
}

// 항목 리스트 아이템
@Composable
fun LocationItem(text: String, icon: @Composable () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
    ) {
        icon()
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = White, style = AppTypography.bodyMedium)
    }
}