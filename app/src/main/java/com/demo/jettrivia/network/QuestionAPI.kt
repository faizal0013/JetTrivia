package com.demo.jettrivia.network

import com.demo.jettrivia.models.Question
import retrofit2.http.GET
import javax.inject.Singleton


@Singleton
interface QuestionAPI {
    @GET("world.json")
    suspend fun getAllQuestions() : Question
}