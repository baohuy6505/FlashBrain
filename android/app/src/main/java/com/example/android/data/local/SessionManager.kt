package com.example.android.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.android.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    var authToken: String?
        get() = prefs.getString("auth_token", null)
        set(value) = prefs.edit().putString("auth_token", value).apply()

    // THÊM DỮ LIỆU MẶC ĐỊNH Ở ĐÂY
    var currentUser: User? = User(
        id = "huy_001",
        email = "baohuy6505@gmail.com",
        name = "Nguyễn Bảo Huy",
        subscriptionType = "PREMIUM",
        balance = 1240.50,
        image = null // Bạn có thể thêm link ảnh nếu muốn
    )

    fun saveToken(token: String) {
        this.authToken = token
    }

    // Hàm trả về user (luôn có dữ liệu vì đã khởi tạo ở trên)
    fun fetchUser(): User? = currentUser

    fun saveUser(user: User) {
        this.currentUser = user
    }

    fun logout() {
        prefs.edit().clear().apply()
        // Sau khi logout có thể để lại null hoặc giữ mock data tùy bạn
        // currentUser = null
    }
}