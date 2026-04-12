package com.example.android.data.repository

import com.example.android.data.remote.AuthApi
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
    private val authApi: AuthApi
) : UserRepository {

    override suspend fun updateProfile(name: String, imagePath: String?): Result<User> =
        withContext(Dispatchers.IO) {
            try {
                val myToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2ZjZjNWQxYi0zYzQ3LTQ1NTUtOWUyNi1lOGVjZDRmMmFhZjUiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc3NTk4Mjk0MCwiZXhwIjoxNzc2NTg3NzQwfQ.mFbKTeJKGqn6uAqz9Sp4PMg-XWudqvs6aacW0n3d6Fs"

                val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())

                val imagePart = imagePath?.let { path ->
                    val file = File(path)
                    if (file.exists()) {
                        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("image", file.name, requestFile)
                    } else null
                }

                val response = authApi.updateProfile(
                    token = myToken,
                    name = nameBody,
                    image = imagePart
                )

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!.data)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Lỗi server: ${response.code()}"
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }
}