package com.example.android.data.remote

import com.example.android.data.remote.dto.*
import com.example.android.domain.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ApiResponse<AuthResponseData>>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<AuthResponseData>>

    @POST("api/auth/google-login")
    suspend fun loginWithGoogle(
        @Body request: GoogleLoginRequest
    ): Response<ApiResponse<AuthResponseData>>

//    // Lấy thông tin cá nhân
//    @GET("api/auth/me")
//    suspend fun getMe(
//        @Header("Authorization") token: String
//    ): Response<ApiResponse<UserRemoteModel>>

    // --- THÊM HÀM LOGOUT Ở ĐÂY ---
    // Gửi yêu cầu đăng xuất lên Server (nếu Server cần xóa session/token)
    @POST("api/auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<ApiResponse<Unit>>

    @Multipart
    @PUT("api/auth/me")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<ApiResponse<User>>
}