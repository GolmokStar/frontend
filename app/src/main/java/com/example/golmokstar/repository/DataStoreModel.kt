package com.example.golmokstar.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Context에 대한 preferencesDataStore 확장 함수 정의
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class DataStoreModule(private val context: Context) {

    // 위도, 경도를 저장할 키 정의
    private object PreferencesKeys {
        val LATITUDE = doublePreferencesKey("latitude")
        val LONGITUDE = doublePreferencesKey("longitude")
    }

    // Latitude 값을 가져오는 Flow
    val getLatitude: Flow<Double> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.LATITUDE] ?: 0.0 // 기본값 0.0
        }

    // Longitude 값을 가져오는 Flow
    val getLongitude: Flow<Double> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.LONGITUDE] ?: 0.0 // 기본값 0.0
        }

    // Latitude와 Longitude 값을 DataStore에 저장하는 함수
    suspend fun saveLocation(latitude: Double, longitude: Double) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LATITUDE] = latitude
            preferences[PreferencesKeys.LONGITUDE] = longitude
        }
    }
}
