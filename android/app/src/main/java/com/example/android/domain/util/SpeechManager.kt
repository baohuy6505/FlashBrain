package com.example.android.presentation.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class SpeechManager(context: Context) {
    private var tts: TextToSpeech? = null
    private var isReady = false

    init {
        // Khởi tạo engine đọc văn bản của Android
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Nếu thẻ là tiếng Việt thì dùng: Locale("vi", "VN")
                // Nếu là tiếng Anh thì dùng: Locale.US
                val result = tts?.setLanguage(Locale.US)
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    isReady = true
                }
            }
        }
    }

    fun speak(text: String) {
        if (isReady) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    //giai phong tai nguyen
    fun shutDown() {
        tts?.stop()
        tts?.shutdown()
    }
}