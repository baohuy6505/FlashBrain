package com.example.android.data.remote
import com.example.android.domain.model.User

import com.example.android.data.remote.ApiResponse

import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Header
interface AuthApi {
    @Multipart
    @PUT("api/auth/me")
    suspend fun updateProfile(
        @Header("Authorization") token: String, // <--- THÊM DÒNG NÀY
        @Part("name") name: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<ApiResponse<User>>
}