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

    // ── REGISTER ──
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

    // ── LOGIN (EMAIL/PASSWORD) ──
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

    // ── LOGIN WITH GOOGLE (MỚI) ──
    override suspend fun loginWithGoogle(idToken: String): Result<AuthResponseData> =
        withContext(Dispatchers.IO) {
            try {
                // Đóng gói idToken vào DTO
                val request = GoogleLoginRequest(idToken = idToken)
                val response = authApi.loginWithGoogle(request)
                val body = response.body()

                if (response.isSuccessful && body?.success == true) {
                    // Lưu Token hệ thống của BE trả về sau khi verify Google thành công
                    body.data?.token?.let { sessionManager.saveToken(it) }
                    Result.success(body.data!!)
                } else {
                    Result.failure(Exception(body?.message ?: "Đăng nhập Google thất bại"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    // ── UPDATE PROFILE ──
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

    // ── LOGOUT ──
    override suspend fun logout(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val tokenFromSession = sessionManager.authToken ?: ""
            val myToken = if (tokenFromSession.startsWith("Bearer ")) tokenFromSession else "Bearer $tokenFromSession"

            val response = authApi.logout(myToken)

            if (response.isSuccessful) {
                val body = response.body()
                val message = body?.message ?: "Đăng xuất thành công"

                // Xóa token và user cục bộ
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