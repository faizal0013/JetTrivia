package com.demo.jettrivia.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.jettrivia.datas.DataOrException
import com.demo.jettrivia.models.QuestionItem
import com.demo.jettrivia.repositorys.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val questionRepository: QuestionRepository
) : ViewModel() {
    val data: MutableState<DataOrException<ArrayList<QuestionItem>, Boolean, Exception>> = mutableStateOf(DataOrException(null, true))

    init {
        getAllQuestions()
    }

    private fun getAllQuestions(){
        viewModelScope.launch {
            data.value.loading = true

            data.value = questionRepository.getAllQuestions()

            if(data.value.data.toString().isNotEmpty()){
                data.value.loading = false
            }
        }
    }

    fun getTotalQuestionCount(): Int {
        return data.value.data?.toMutableList()?.size!!
    }

}