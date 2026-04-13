package com.example.android.domain.repository

import com.example.android.data.remote.dto.AuthResponseData
import com.example.android.data.remote.dto.LoginRequest
import com.example.android.data.remote.dto.RegisterRequest
import com.example.android.domain.model.User

interface UserRepository {
    // Đăng ký
    suspend fun register(request: RegisterRequest): Result<AuthResponseData>

    // Đăng nhập email/mật khẩu
    suspend fun login(request: LoginRequest): Result<AuthResponseData>

    // Đăng nhập bằng Google
    suspend fun loginWithGoogle(idToken: String): Result<AuthResponseData>

    // Đăng xuất
    suspend fun logout(): Result<String>

    // Cập nhật profile
    suspend fun updateProfile(name: String, imagePath: String?): Result<User>
}