package com.example.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)



data class AuthResponseData(
    @SerializedName("token") val token: String
    // Vì BE chưa trả user nên không khai báo user ở đây
)

// Model này dùng để hứng dữ liệu từ API "Get Profile"
data class UserRemoteModel(
    @SerializedName("_id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("avatar") val avatar: String? = null
)