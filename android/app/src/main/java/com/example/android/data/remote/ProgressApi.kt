package com.example.android.data.remote

import com.example.android.data.remote.dto.UserProgressDto
import retrofit2.Response
import retrofit2.http.GET

interface ProgressApi {

    @GET("api/progress/me")
    suspend fun getMyProgress(): Response<ApiResponse<UserProgressDto>>
}