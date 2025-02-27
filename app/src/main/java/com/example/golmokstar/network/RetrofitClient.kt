package com.example.golmokstar.network

import com.example.golmokstar.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor()) // ✅ 인증 Interceptor 적용
            .connectTimeout(30, TimeUnit.SECONDS) // 타임아웃 설정
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /**
     * ✅ 기본 Retrofit 인스턴스 (일반 API 용)
     */
    @Provides
    @Singleton
    @Named("DefaultRetrofit") // ✅ Retrofit 구분을 위한 `@Named` 추가
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) // ✅ 서버 API URL
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    /**
     * ✅ API 서비스 주입 (기본 Retrofit)
     */
    @Provides
    @Singleton
    fun provideAuthApiService(@Named("DefaultRetrofit") retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTravelApiService(@Named("DefaultRetrofit") retrofit: Retrofit): TravelApiService {
        return retrofit.create(TravelApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideMapPinAPI(@Named("DefaultRetrofit") retrofit: Retrofit): MapPinApiService {
        return retrofit.create(MapPinApiService::class.java)
    }

    /**
     * ✅ Google Places API 서비스 주입
     */

    @Provides
    @Singleton
    @Named("PlacesRetrofit")
    fun providePlacesRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://places.googleapis.com/")  // ✅ Google Places API 베이스 URL
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun providePlacesApiService(@Named("PlacesRetrofit") retrofit: Retrofit): PlacesApiService {
        return retrofit.create(PlacesApiService::class.java)
    }
}
