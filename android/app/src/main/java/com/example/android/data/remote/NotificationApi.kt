package com.example.android.data.remote

import com.example.android.data.remote.dto.NotificationDto
import retrofit2.Response
import retrofit2.http.GET

interface NotificationApi {
    @GET("api/notifications")
    suspend fun getNotifications(): Response<ApiResponse<List<NotificationDto>>>
}