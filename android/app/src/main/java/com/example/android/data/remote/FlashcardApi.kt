package com.example.android.data.remote

import com.example.android.data.remote.dto.DeckDto
import com.example.android.data.remote.dto.FlashcardDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.Response

interface FlashcardApi {
    // --- DECKS ---
    @GET("api/decks")
    suspend fun getAllDecks(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<DeckDto>>>

    @POST("api/decks")
    suspend fun createDeck(
        @Header("Authorization") token: String,
        @Body deck: DeckDto
    ): Response<ApiResponse<DeckDto>>

    @PUT("api/decks/{id}")
    suspend fun updateDeck(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body deck: DeckDto
    ): Response<ApiResponse<DeckDto>>

    // --- FLASHCARDS ---
    @GET("api/flashcards/deck/{deckId}")
    suspend fun getFlashcardsByDeck(
        @Header("Authorization") token: String,
        @Path("deckId") deckId: String
    ): Response<ApiResponse<List<FlashcardDto>>>

    @POST("api/flashcards")
    suspend fun createFlashcard(
        @Header("Authorization") token: String,
        @Body card: FlashcardDto
    ): Response<ApiResponse<FlashcardDto>>

    @PUT("api/flashcards/{id}")
    suspend fun updateFlashcard(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body card: FlashcardDto
    ): Response<ApiResponse<FlashcardDto>>
}