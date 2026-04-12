package com.example.android.di

import com.example.android.data.remote.AuthApi
import com.example.android.data.remote.RetrofitClient // Import cái Client của bạn vào
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        // Thay vì tự xây mới, ta lấy luôn cái instance đã cấu hình chuẩn ở RetrofitClient
        return RetrofitClient.instance
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}