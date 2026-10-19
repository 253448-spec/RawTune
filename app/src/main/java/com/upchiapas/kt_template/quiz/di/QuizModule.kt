package com.upchiapas.kt_template.quiz.di

import com.upchiapas.kt_template.quiz.data.datasource.QuizLocalDataSource
import com.upchiapas.kt_template.quiz.data.repository.QuizRepository
import com.upchiapas.kt_template.quiz.data.repository.QuizRepositoryImpl
import com.upchiapas.kt_template.quiz.presentation.viewmodels.QuizViewModelFactory

class QuizModule {
    private val localDataSource: QuizLocalDataSource by lazy {
        QuizLocalDataSource()
    }

    private val repository: QuizRepository by lazy {
        QuizRepositoryImpl(localDataSource)
    }

    val factoryQuiz: QuizViewModelFactory by lazy {
        QuizViewModelFactory(repository)
    }
}