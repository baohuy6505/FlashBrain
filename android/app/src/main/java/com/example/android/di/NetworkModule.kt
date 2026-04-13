package com.example.android.di

import com.example.android.data.remote.AuthApi
import com.example.android.data.remote.FlashcardApi
import com.example.android.data.remote.NotificationApi
import com.example.android.data.remote.ProgressApi
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        // 1. Máy ghi log để soi lỗi trong Logcat
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // 2. OkHttpClient với thời gian chờ lâu hơn (tránh lỗi Timeout)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        // 3. GSON đơn giản, không tự ý đổi tên biến
        val gson = GsonBuilder()
            .setLenient() // Chấp nhận JSON không chuẩn
            .create()

        return Retrofit.Builder()
            .baseUrl("http://192.168.1.14:3000/") // Đúng chuẩn cho Emulator
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideFlashcardApi(retrofit: Retrofit): FlashcardApi {
        return retrofit.create(FlashcardApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }


    @Provides
    @Singleton
    fun provideNotificationApi(retrofit: Retrofit): NotificationApi {
        return retrofit.create(NotificationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProgressApi(retrofit: Retrofit): com.example.android.data.remote.ProgressApi {
        return retrofit.create(com.example.android.data.remote.ProgressApi::class.java)
    }
}