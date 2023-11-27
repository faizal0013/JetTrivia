package com.demo.jettrivia.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.jettrivia.components.Questions


@Composable
fun TriviaHome(viewModel: QuestionViewModel = hiltViewModel()){
    Questions(viewModel = viewModel)
}

