package com.example.golmokstar.ui.screens


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.golmokstar.R
import com.example.golmokstar.network.dto.MapPinFavoredRequest
import com.example.golmokstar.network.dto.MapPinRecordRequest
import com.example.golmokstar.network.dto.MapPinVisitRequest
import com.example.golmokstar.ui.components.BlueBox
import com.example.golmokstar.ui.components.BlueMarkerIcon
import com.example.golmokstar.ui.components.CustomButton
import com.example.golmokstar.ui.components.NavyBox
import com.example.golmokstar.ui.components.NavyMarkerIcon
import com.example.golmokstar.ui.components.RedBox
import com.example.golmokstar.ui.components.RedMarkerIcon
import com.example.golmokstar.ui.components.YellowBox
import com.example.golmokstar.ui.components.YellowMarkerIcon
import com.example.golmokstar.ui.components.blueMarkerPin
import com.example.golmokstar.ui.components.navyMarkerPin
import com.example.golmokstar.ui.components.redMarkerPin
import com.example.golmokstar.ui.components.yellowMarkerPin
import com.example.golmokstar.ui.theme.AppTypography
import com.example.golmokstar.ui.theme.BlurBackgroundGray
import com.example.golmokstar.ui.theme.IconGray
import com.example.golmokstar.ui.theme.MainNavy
import com.example.golmokstar.ui.theme.MarkerBlue
import com.example.golmokstar.ui.theme.MarkerRed
import com.example.golmokstar.ui.theme.MarkerYellow
import com.example.golmokstar.ui.theme.TextDarkGray
import com.example.golmokstar.ui.theme.White
import com.example.golmokstar.viewmodel.MapViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
    TripDropdownScreen(viewModel = viewModel)

    val pinDataList by viewModel.pinDataList.observeAsState(emptyList())

    val trippinDataList by viewModel.trippinDataList.observeAsState(emptyList())

    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val activity = context as? Activity

    var currentLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var savedLatitude by remember { mutableDoubleStateOf(0.0) }
    var savedLongitude by remember { mutableDoubleStateOf(0.0) }

    var showLocationBox by remember { mutableStateOf(false) } // 박스 열기/닫기 상태 추가
    var permissionGranted by remember { mutableStateOf(false) } // 권한이 허용되었는지 여부
    var visibleBoxState by remember { mutableStateOf<String?>(null) }
    var markerColor by remember { mutableStateOf(MarkerRed) } // 기본 색상 설정

    var showDialog by remember { mutableStateOf(false) }

    var selectedMarkerLocation by remember { mutableStateOf<LatLng?>(null) }
    var selectedName by remember { mutableStateOf("") }
    var selectedAddress by remember { mutableStateOf("") }
    var selectedTypes by remember { mutableStateOf("") }
    var selectedTitles by remember { mutableStateOf<String?>(null) }
    var selectedRating by remember { mutableStateOf<Double?>(null) }  // nullable Double로 설정
    var selectedDate by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var selectedLat by remember { mutableStateOf(0.0) }
    var selectedLng by remember { mutableStateOf(0.0) }
    var selectedId by remember { mutableStateOf("") }

    // Observe placeRegisterResult with observeAsState
    val placeRegisterResult by viewModel.placeRegisterResult.observeAsState("")


    LaunchedEffect(Unit) {
        viewModel.dropdownApi()
    }

    LaunchedEffect(pinDataList) {
        // 핀 데이터가 변경될 때마다 지도에 반영하도록 함
    }

    LaunchedEffect(placeRegisterResult) {
        if (placeRegisterResult.isNotEmpty()) {
            Toast.makeText(context, placeRegisterResult, Toast.LENGTH_SHORT).show()
        }
    }

    // 박스 상태 및 색상 변경 함수
    fun changeBoxState(newState: String, newColor: Color) {
        visibleBoxState = newState
        markerColor = newColor
    }

    // Places API 초기화
    val apiKey = context.getString(R.string.google_API_key)
    if (apiKey.isEmpty() || apiKey == "DEFAULT_API_KEY") {
        Log.e("Places test", "API 키가 없습니다.")
        return
    }

    var placesClient: PlacesClient? by remember { mutableStateOf(null) }
    var isPlacesInitialized by remember { mutableStateOf(false) }  // Places 초기화 상태를 추적

    // Places 초기화
    LaunchedEffect(apiKey) {
        try {
            Places.initialize(context, apiKey)
            placesClient = Places.createClient(context)
            isPlacesInitialized = true
        } catch (e: Exception) {
            Log.e("Places test", "PlacesClient 초기화 실패: ${e.message}")
            isPlacesInitialized = false
        }
    }

    fun getPlaceDetails(context: Context, placeId: String) {
        val apiKey = context.getString(R.string.google_API_key) // API 키 가져오기
        val url = "https://maps.googleapis.com/maps/api/place/details/json" +
                "?place_id=$placeId" +
                "&language=ko" +
                "&key=$apiKey"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    // API 응답 상태 확인
                    val status = response.getString("status")
                    if (status == "OK") {
                        val result = response.getJSONObject("result")
                        val address = result.getString("formatted_address")

                        if (address.contains("대한민국")) {
                            selectedAddress = address.replace("대한민국", "").trim()
                        }

                        val placeTypes = result.getJSONArray("types")
                        val excludedTypes = setOf("point_of_interest", "establishment")

                        selectedTypes = (0 until placeTypes.length())
                            .map { placeTypes.getString(it).toUpperCase() }  // 대문자로 변환
                            .firstOrNull { it !in excludedTypes } ?: "CAFE"  // 대문자 "UNKNOWN"으로 설정

                        Log.d("POIPlace", "정확한 주소: $selectedAddress")
                        Log.d("POIPlace", "장소 유형: $selectedTypes")
                    } else if (status == "NOT_FOUND") {
                        Log.e("POIPlace", "장소를 찾을 수 없습니다. 제공된 place_id가 올바른지 확인하세요.")
                    } else {
                        Log.e("POIPlace", "API 요청 실패, 상태: $status")
                    }
                } catch (e: Exception) {
                    Log.e("POIPlace", "JSON 파싱 오류", e)
                }
            },
            { error ->
                Log.e("POIPlace", "API 요청 오류", error)
            })

        Volley.newRequestQueue(context).add(request)
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
    { isGranted ->
        permissionGranted = isGranted
        if (isGranted) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } else {
            Toast.makeText(context, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } else {
            activity?.let { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 18f)
    }

    LaunchedEffect(currentLocation) {
        if (currentLocation.latitude != 0.0 && currentLocation.longitude != 0.0) {
            if (cameraPositionState.position.target != currentLocation) {
                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(currentLocation, 18f))
            }
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
                onPOIClick = { poi ->
                    poi.let {
                        // POI 이름, 위치, 주소 가져오기
                        val poiName = it.name.lines().firstOrNull() ?: "이름 없음"
                        val poiLatLng = it.latLng
                        val poiLat = poiLatLng.latitude
                        val poiLng = poiLatLng.longitude
                        val placeId = it.placeId // placeId를 가져옴

                        getPlaceDetails(context, placeId)

                        Log.d("POIPlace", "이름: $poiName")
                        Log.d("POIPlace", "place ID: $placeId")
                        Log.d("POIPlace", "위치: $poiLat, $poiLng")

                        // 선택된 위치와 정보 설정
                        selectedMarkerLocation = poiLatLng
                        selectedName = poiName
                        selectedLocation = poiLatLng
                        selectedLat = poiLat
                        selectedLng = poiLng
                        selectedId = placeId
                        markerColor = MainNavy
                        visibleBoxState = "Navy"

                        // 마커 색상 및 정보 박스 표시
                        showLocationBox = true
                    } ?: run {
                        Log.e("POIPlace", "POI가 null입니다.")
                    } ?: run {
                        Log.e("POIPlace", "POI가 null입니다.")
                        Toast.makeText(context, "POI 정보가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {

                pinDataList.forEach { pin ->
                    // pinType에 따른 색상 또는 마커 아이콘 지정
                    val markerIcon = when (pin.pinType ?: "UNKNOWN") {
                        "VISITED_PENDING" -> yellowMarkerPin(context)
                        "RECORDED" -> blueMarkerPin(context)
                        "FAVORED" -> redMarkerPin(context)
                        else -> navyMarkerPin(context)
                    }

                    selectedTitles = pin.tripName
                    selectedRating = pin.rating
                    selectedDate = pin.createdAt
                    selectedName = pin.placeName

                    // 마커 추가
                    Marker(
                        state = MarkerState(position = LatLng(pin.latitude, pin.longitude)),
                        title = "선택한 위치",
                        icon = markerIcon
                    )
                }

                trippinDataList.forEach { pin ->
                    val markerIcon = when (pin.pinType ?: "UNKNOWN") {
                        "VISITED_PENDING" -> yellowMarkerPin(context)
                        "RECORDED" -> blueMarkerPin(context)
                        "FAVORED" -> redMarkerPin(context)
                        else -> navyMarkerPin(context)
                    }

                    selectedTitles = pin.tripName
                    selectedRating = pin.rating
                    selectedDate = pin.createdAt
                    selectedName = pin.placeName

                    // 마커 추가
                    Marker(
                        state = MarkerState(position = LatLng(pin.latitude, pin.longitude)),
                        title = "선택한 위치",
                        icon = markerIcon
                    )
                }

                // 저장된 위도, 경도가 있으면 마커 표시
                if (savedLatitude != 0.0 && savedLongitude != 0.0) {
                    Marker(
                        state = MarkerState(position = LatLng(savedLatitude, savedLongitude)),
                        title = "찜한 위치",
                        icon = navyMarkerPin(context) //
                    )
                }

                // 선택된 마커 위치에 핀 표시
                selectedMarkerLocation?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "선택한 위치",
                        icon = when (markerColor) {
                            MarkerRed -> redMarkerPin(context)
                            MarkerYellow -> yellowMarkerPin(context)
                            MarkerBlue -> blueMarkerPin(context)
                            MainNavy -> navyMarkerPin(context)
                            else -> navyMarkerPin(context)
                        }
                    )
                }
            }

            // 내 위치 버튼 → 오른쪽 하단에 배치
            FloatingActionButton(onClick = {
                // 내 위치 버튼을 눌렀을 때만 카메라 이동
                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(currentLocation, 18f))
            }, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp), containerColor = White) {
                Icon(painter = painterResource(R.drawable.my_location_icon), contentDescription = "내 위치 이동", Modifier.size(28.dp), tint = MainNavy)
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
                    when (visibleBoxState) {
                        "Red" -> {
                            RedBox(
                                address = selectedAddress,
                                date = "2025-02-25",
                                name = selectedName,
                                onBoxClick = { selectedMarkerLocation = null },
                                onButtonClick = {

                                    val mapPinVisitRequest = MapPinVisitRequest(
                                        tripId = 18,  // 예시로 tripId를 설정
                                        googlePlaceId = selectedId,
                                        placeName = selectedName,
                                        latitude = selectedLat,
                                        longitude = selectedLng,
                                        deviceLatitude = currentLocation.latitude,
                                        deviceLongitude = currentLocation.longitude,
                                        pinType = "VISITED_PENDING"
                                    )

                                    viewModel.visitApi(mapPinVisitRequest) // 여기서 mapViewModel은 ViewModel 인스턴스입니다.

                                    // RedBox 버튼 클릭 시 YellowBox로 전환
                                    changeBoxState("Yellow", MarkerYellow)
                                }
                            )
                        }
                        "Yellow" -> {
                            YellowBox(
                                address = selectedAddress,
                                date = "2025-02-25",
                                name = selectedName,
                                onBoxClick = { selectedMarkerLocation = null },
                                onButtonClick = {

                                    val mapPinRecordRequest = MapPinRecordRequest(
                                        pinId = 40,
                                        pinType = "RECORDED"
                                    )

                                    viewModel.recordApi(mapPinRecordRequest)

                                    // YellowBox 버튼 클릭 시 BlueBox로 전환
                                    changeBoxState("Blue", MarkerBlue)
                                    showDialog = true
                                },
                                topLeftText = "입닫고맛잇는빵먹기"
                            )
                        }
                        "Blue" -> {
                            BlueBox(
                                address = selectedAddress,
                                date = "2025-02-25",
                                name = selectedName,
                                onButtonClick = {},
                                onBoxClick = { selectedMarkerLocation = null }, // 클릭 시 처리
                                extraText = "입닫고맛잇는빵먹기"
                            )
                        }
                        else -> {
                            NavyBox(
                                address = selectedAddress,
                                date = "2025-02-25",
                                name = selectedName,
                                onButtonClick = {

                                    val mapPinVisitRequest = MapPinVisitRequest(
                                        tripId = 18,
                                        googlePlaceId = selectedId,
                                        placeName = selectedName,
                                        latitude = selectedLat,
                                        longitude = selectedLng,
                                        deviceLatitude = currentLocation.latitude,
                                        deviceLongitude = currentLocation.longitude,
                                        pinType = "VISITED_PENDING"  // 찜하기로 설정
                                    )

                                    // ViewModel을 통해 API 호출
                                    viewModel.visitApi(mapPinVisitRequest)

                                    changeBoxState("Yellow", MarkerYellow)
                                },
                                onBoxClick = { selectedMarkerLocation = null }, // 클릭 시 처리
                                extraText = "",
                                icon = { NavyMarkerIcon(Modifier.size(15.dp)) },
                                topLeftText = "",
                                topLeft = {
                                    CustomButton {
                                        // MapPinFavoredRequest 객체 생성
                                        val mapPinFavoredRequest = MapPinFavoredRequest(
                                            tripId = 18,  // 예시로 tripId를 설정
                                            googlePlaceId = selectedId,
                                            placeName = selectedName,
                                            placeType = selectedTypes,
                                            latitude = selectedLat,   // 예시로 가정한 변수
                                            longitude = selectedLng,  // 예시로 가정한 변수
                                            pinType = "FAVORED"  // 찜하기로 설정
                                        )

                                        // ViewModel을 통해 API 호출
                                        viewModel.favoredApi(mapPinFavoredRequest) // 여기서 mapViewModel은 ViewModel 인스턴스입니다.

                                        // 상태 변경 (changeBoxState 함수)
                                        changeBoxState("Red", MarkerRed)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // Report 다이얼로그 표시
            if (showDialog) {
                Report(onDismiss = { showDialog = false })
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
                TripDropdownScreen(
                    viewModel = viewModel(), // ViewModel 전달
                    modifier = Modifier
                        .align(Alignment.TopStart) // 드롭다운 위치를 Box 내에서 제어

                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDropdownScreen(viewModel: MapViewModel, modifier: Modifier = Modifier) {
    val dropdownItems by viewModel.dropdownItems.observeAsState(initial = emptyList()) // API 데이터 옵저빙
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("선택해주세요") } // 기본값

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier
            .padding(16.dp)
            .width(180.dp)
            .height(50.dp)
            .background(White)
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) ImageVector.vectorResource(R.drawable.arrow_drop_up_icon)
                        else ImageVector.vectorResource(R.drawable.arrow_drop_down_icon),
                        contentDescription = "Toggle dropdown"
                    )
                }
            },
            modifier = Modifier.menuAnchor(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = IconGray,  // 포커스가 없는 상태일 때 외곽선 색상
                focusedBorderColor = IconGray,    // 포커스가 있는 상태일 때 외곽선 색상
                focusedLabelColor = TextDarkGray,
                unfocusedLabelColor = TextDarkGray
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(White)

        ) {
            // API에서 받아온 데이터 표시
            dropdownItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.title, style = AppTypography.bodyMedium, color = TextDarkGray) },
                    onClick = {
                        selectedItem = item.title
                        expanded = false

                        if (item.title == "전체 여행") {
                            viewModel.mapPinApi()
                        } else {
                            viewModel.mapPinTripIdApi(item.tripId)
                        }
                    }
                )
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