package com.example.golmokstar.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.golmokstar.R
import com.example.golmokstar.ui.theme.MarkerBlue
import com.example.golmokstar.ui.theme.MarkerRed
import com.example.golmokstar.ui.theme.MarkerYellow
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory


// ✅ 색상 변경 가능 벡터 → 비트맵 변환 함수 (구글맵마커용)
fun vectorToBitmap(context: Context, @DrawableRes vectorResId: Int, color: Int? = null, width: Int = 63, height: Int = 73): BitmapDescriptor {
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return BitmapDescriptorFactory.defaultMarker()

    // 색상 필터 적용 (색상이 null이 아니면 적용)
    color?.let {
        val colorFilter: ColorFilter = PorterDuffColorFilter(it, PorterDuff.Mode.SRC_IN)
        drawable.colorFilter = colorFilter
    }

    // 원하는 크기(size)로 비트맵 생성
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}


// ✅ 색상별 마커 생성 함수
fun redMarkerPin(context: Context): BitmapDescriptor {
    return vectorToBitmap(context, R.drawable.marker_icon, MarkerRed.toArgb()) // 빨간색 (MarkerRed)
}

fun blueMarkerPin(context: Context): BitmapDescriptor {
    return vectorToBitmap(context, R.drawable.marker_icon, MarkerBlue.toArgb()) // 파란색 (MarkerBlue)
}

fun yellowMarkerPin(context: Context): BitmapDescriptor {
    return vectorToBitmap(context, R.drawable.marker_icon, MarkerYellow.toArgb()) // 노란색 (MarkerYellow)
}

@Composable
fun RedMarkerIcon(
    modifier: Modifier = Modifier,
    size: Dp = 15.dp
) {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.marker_icon), // 벡터 이미지 로드
        contentDescription = "위치",
        modifier = modifier.size(size), // 크기 설정
        tint = MarkerRed // 빨간색 색상 적용
    )
}

@Composable
fun YellowMarkerIcon(
    modifier: Modifier = Modifier,
    size: Dp = 15.dp
) {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.marker_icon), // 벡터 이미지 로드
        contentDescription = "위치",
        modifier = modifier.size(size), // 크기 설정
        tint = MarkerYellow // 노란색 색상 적용
    )
}

@Composable
fun BlueMarkerIcon(
    modifier: Modifier = Modifier,
    size: Dp = 15.dp
) {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.marker_icon), // 벡터 이미지 로드
        contentDescription = "위치",
        modifier = modifier.size(size), // 크기 설정
        tint = MarkerBlue // 파란색 색상 적용
    )
}


@Composable
fun ColoredIcon(
    modifier: Modifier = Modifier,
    iconResId: Int,
    size: Dp = 15.dp,
    contentColor: Color = Color.Unspecified, // 기본값으로 Color.Unspecified
    contentDescription: String = "아이콘"
) {
    Icon(
        imageVector = ImageVector.vectorResource(id = iconResId), // 벡터 이미지 로드
        contentDescription = contentDescription,
        modifier = modifier.size(size), // 크기 설정
        tint = contentColor // 색상 적용
    )
}
