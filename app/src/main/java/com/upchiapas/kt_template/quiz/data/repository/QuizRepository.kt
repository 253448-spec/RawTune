package com.upchiapas.kt_template.quiz.data.repository

import com.upchiapas.kt_template.quiz.data.Question

interface QuizRepository {
    fun getQuestions(): List<Question>
}