package com.upchiapas.kt_template.quiz.data.datasource

import com.upchiapas.kt_template.quiz.data.Question

class QuizLocalDataSource {
    private val questions = mutableListOf(
        Question(
            id = 1,
            text = "¿Cuál es el lenguaje oficial recomendado para desarrollar Android de forma nativa?",
            options = listOf("Java", "Kotlin", "Python"),
            correctAnswerIndex = 1
        ),
        Question(
            id = 2,
            text = "¿Qué función cumple Jetpack Compose en el desarrollo de Android?",
            options = listOf("Gestionar la base de datos", "Diseñar la UI de forma declarativa", "Realizar peticiones de red"),
            correctAnswerIndex = 1
        ),
        Question(
            id = 3,
            text = "¿Qué componente del patrón MVVM expone el estado hacia la interfaz gráfica?",
            options = listOf("ViewModel", "Repository", "DataSource"),
            correctAnswerIndex = 0
        )
    )

    fun getQuestions(): List<Question> = questions.toList()
}