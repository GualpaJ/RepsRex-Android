package com.javier.repsrex.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Modelo de datos
data class Quote(
    val q: String,   // la frase
    val a: String    // el autor
)

// Interface con el endpoint
interface QuoteService {

    // Con corutina (suspend fun)
    @GET("api/random")
    suspend fun getRandomQuote(): List<Quote>

    companion object {
        fun getInstance(): QuoteService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://zenquotes.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(QuoteService::class.java)
        }
    }
}