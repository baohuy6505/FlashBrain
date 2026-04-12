package com.example.android.domain.repository
import com.example.android.domain.model.User
interface UserRepository {
    suspend fun updateProfile(
        name: String,
        imagePath: String?
    ): Result<User>
}