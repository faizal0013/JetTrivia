package com.demo.jettrivia.repositorys

import com.demo.jettrivia.datas.DataOrException
import com.demo.jettrivia.models.QuestionItem
import com.demo.jettrivia.network.QuestionAPI
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionAPI) {
    private val dataOrException = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()

    suspend fun getAllQuestions() : DataOrException<ArrayList<QuestionItem>, Boolean, Exception>{
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllQuestions()

            if(dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false
        } catch (e: Exception) {
            dataOrException.e = e
        }

        return dataOrException
    }
}
