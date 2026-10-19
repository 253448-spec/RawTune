package com.upchiapas.kt_template.quiz.data

data class Question(
    val id: Int,
    val text: String,
    val options: List<String>, // Contendrá 3 opciones
    val correctAnswerIndex: Int
)
