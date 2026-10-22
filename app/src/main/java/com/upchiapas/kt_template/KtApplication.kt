package com.upchiapas.kt_template

import android.app.Application
import com.upchiapas.kt_template.quiz.di.QuizModule
import com.upchiapas.kt_template.rawtune.di.RawTuneModule

class KtApplication : Application() {
    val quizModule: QuizModule by lazy {
        QuizModule()
    }

    val rawTuneModule: RawTuneModule by lazy {
        RawTuneModule(this)
    }
}
