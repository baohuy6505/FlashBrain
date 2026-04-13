package com.example.android.domain.repository

import com.example.android.data.remote.dto.AuthResponseData
import com.example.android.data.remote.dto.LoginRequest
import com.example.android.data.remote.dto.RegisterRequest
import com.example.android.domain.model.User

interface UserRepository {
    // Đăng ký
    suspend fun register(request: RegisterRequest): Result<AuthResponseData>

    // Đăng nhập
    suspend fun login(request: LoginRequest): Result<AuthResponseData>

    // --- THÊM HÀM ĐĂNG XUẤT TẠI ĐÂY ---
    suspend fun logout(): Result<String>

    // Cập nhật profile (Phần của thành viên khác)
    suspend fun updateProfile(name: String, imagePath: String?): Result<User>
}