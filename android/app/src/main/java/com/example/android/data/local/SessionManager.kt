package com.example.android.data.local

import com.example.android.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    //hiện tại ta hardcode thông tin Huy ở đây
    var currentUser: User? = User(
        id = "huy_id_001", // Đây là ID sẽ dùng chung cho mọi Deck/Flashcard
        email = "baohuy6505@gmail.com",
        name = "Nguyễn Bảo Huy"
    )

    fun isLoggedIn(): Boolean = currentUser != null
}