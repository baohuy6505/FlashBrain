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
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) : UserRepository {

    override suspend fun register(request: RegisterRequest): Result<AuthResponseData> =
        withContext(Dispatchers.IO) {
            try {
                val response = authApi.register(request)
                val body = response.body()
                if (response.isSuccessful && body?.success == true) {
                    body.data?.token?.let { sessionManager.saveToken(it) }
                    Result.success(body.data!!)
                } else {
                    Result.failure(Exception(body?.message ?: "Đăng ký thất bại"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

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

    override suspend fun loginWithGoogle(idToken: String): Result<AuthResponseData> =
        withContext(Dispatchers.IO) {
            try {
                val request = GoogleLoginRequest(idToken = idToken)
                val response = authApi.loginWithGoogle(request)
                val body = response.body()
                if (response.isSuccessful && body?.success == true) {
                    body.data?.token?.let { sessionManager.saveToken(it) }
                    Result.success(body.data!!)
                } else {
                    Result.failure(Exception(body?.message ?: "Đăng nhập Google thất bại"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getMe(): Result<User> = withContext(Dispatchers.IO) {
        try {
            val token = sessionManager.authToken ?: return@withContext Result.failure(Exception("Chưa đăng nhập"))
            val myToken = if (token.startsWith("Bearer ")) token else "Bearer $token"

            val response = authApi.getMe(myToken)
            val body = response.body()

            if (response.isSuccessful && body?.data != null) {
                val user = body.data.toDomain()
                sessionManager.saveUser(user)
                Result.success(user)
            } else {
                Result.failure(Exception("Lỗi lấy thông tin: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(name: String, imagePath: String?): Result<User> =
        withContext(Dispatchers.IO) {
            try {
                val token = sessionManager.authToken ?: ""
                val myToken = if (token.startsWith("Bearer ")) token else "Bearer $token"

                val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
                val imagePart = imagePath?.let { path ->
                    val file = File(path)
                    if (file.exists()) {
                        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("image", file.name, requestFile)
                    } else null
                }

                val response = authApi.updateProfile(myToken, nameBody, imagePart)
                val body = response.body()

                if (response.isSuccessful && body?.data != null) {
                    sessionManager.saveUser(body.data)
                    Result.success(body.data)
                } else {
                    Result.failure(Exception(body?.message ?: "Cập nhật thất bại"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun logout(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val token = sessionManager.authToken ?: ""
            val myToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val response = authApi.logout(myToken)

            if (response.isSuccessful) {
                val message = response.body()?.message ?: "Đăng xuất thành công"
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