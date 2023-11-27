package com.demo.jettrivia.models

data class QuestionItem(
    val answer: String,
    val category: String,
    val choices: List<String>,
    val question: String
)