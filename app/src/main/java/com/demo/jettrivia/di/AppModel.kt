package com.demo.jettrivia.di

import com.demo.jettrivia.network.QuestionAPI
import com.demo.jettrivia.repositorys.QuestionRepository
import com.demo.jettrivia.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModel {

    @Singleton
    @Provides
    fun provideQuestionRepository(api: QuestionAPI): QuestionRepository = QuestionRepository(api)

    @Singleton
    @Provides
    fun provider(): QuestionAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestionAPI::class.java)
    }
}
