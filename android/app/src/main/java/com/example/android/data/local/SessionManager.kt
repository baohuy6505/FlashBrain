package com.example.android.data.local

import com.example.android.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    // Để var để có thể cập nhật giá trị mới
    var currentUser: User? = User(
        id = "huy_id_001",
        email = "baohuy6505@gmail.com",
        name = "Nguyễn Bảo Huy",
        subscriptionType = "PREMIUM",
        balance = 1240.50
    )

    fun fetchUser(): User? = currentUser

    fun saveUser(user: User) {
        this.currentUser = user
    }
}