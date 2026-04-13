package com.example.android.domain.repository

import com.example.android.data.remote.dto.AuthResponseData
import com.example.android.data.remote.dto.LoginRequest
import com.example.android.data.remote.dto.RegisterRequest
import com.example.android.domain.model.User

interface UserRepository {
    suspend fun register(request: RegisterRequest): Result<AuthResponseData>
    suspend fun login(request: LoginRequest): Result<AuthResponseData>
    suspend fun loginWithGoogle(idToken: String): Result<AuthResponseData>
    suspend fun getMe(): Result<User>
    suspend fun logout(): Result<String>
    suspend fun updateProfile(name: String, imagePath: String?): Result<User>
}