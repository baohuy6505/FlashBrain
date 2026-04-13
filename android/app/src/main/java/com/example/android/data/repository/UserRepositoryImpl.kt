package com.example.android.data.repository

import android.util.Log
import com.example.android.data.local.SessionManager
import com.example.android.data.remote.AuthApi
import com.example.android.data.remote.dto.*
import com.example.android.domain.model.User
import com.example.android.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authApi: AuthApi, // Biến này tên là authApi
    private val sessionManager: SessionManager
) : UserRepository {
    //register
    override suspend fun register(request: RegisterRequest): Result<AuthResponseData> =
        withContext(Dispatchers.IO) {
            try {
                val response = authApi.register(request)
                val body = response.body()

                if (response.isSuccessful && body?.success == true) {
                    val token = body.data?.token ?: ""
                    sessionManager.saveToken(token)
                    Result.success(body.data!!)
                } else {
                    Result.failure(Exception(body?.message ?: "Đăng ký thất bại"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    //login
    override suspend fun login(request: LoginRequest): Result<AuthResponseData> =
        withContext(Dispatchers.IO) {
            try {
                val response = authApi.login(request)
                val body = response.body()

                if (response.isSuccessful && body?.success == true) {
                    body.data?.token?.let { sessionManager.saveToken(it) }
                    Result.success(body.data!!)
                } else {
                    Result.failure(Exception(body?.message ?: "Sai email hoặc mật khẩu"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    // update
    override suspend fun updateProfile(name: String, imagePath: String?): Result<User> =
        withContext(Dispatchers.IO) {
            try {
                val tokenFromSession = sessionManager.authToken ?: ""
                val myToken = if (tokenFromSession.startsWith("Bearer ")) tokenFromSession else "Bearer $tokenFromSession"

                val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())

                val imagePart = imagePath?.let { path ->
                    val file = File(path)
                    if (file.exists()) {
                        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("image", file.name, requestFile)
                    } else null
                }

                val response = authApi.updateProfile(myToken, nameBody, imagePart)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.data != null) {
                        sessionManager.saveUser(body.data)
                        Result.success(body.data)
                    } else {
                        Result.failure(Exception("Dữ liệu phản hồi trống"))
                    }
                } else {
                    Result.failure(Exception("Cập nhật thất bại"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    // Logout
    override suspend fun logout(): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Lấy token từ SessionManager
            val tokenFromSession = sessionManager.authToken ?: ""
            val myToken = if (tokenFromSession.startsWith("Bearer ")) tokenFromSession else "Bearer $tokenFromSession"

            // Gọi hàm logout từ authApi (đã đổi từ apiService sang authApi)
            val response = authApi.logout(myToken)

            if (response.isSuccessful) {
                val body = response.body()
                val message = body?.message ?: "Đăng xuất thành công"

                // Xóa dữ liệu cục bộ
                sessionManager.logout()

                Result.success(message)
            } else {
                Result.failure(Exception("Lỗi Server: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}