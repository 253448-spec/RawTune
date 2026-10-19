package com.upchiapas.kt_template.quiz.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.upchiapas.kt_template.quiz.data.repository.QuizRepository
import com.upchiapas.kt_template.quiz.presentation.screens.QuizUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QuizViewModel(
    private val repository: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        val questions = repository.getQuestions()
        _uiState.update { it.copy(questions = questions) }
    }

    fun selectOption(index: Int) {
        _uiState.update { it.copy(selectedOptionIndex = index) }
    }

    fun submitAnswer() {
        val currentState = _uiState.value
        val currentQuestion = currentState.currentQuestion ?: return
        val selectedIndex = currentState.selectedOptionIndex ?: return

        val isCorrect = selectedIndex == currentQuestion.correctAnswerIndex
        val newScore = if (isCorrect) currentState.score + 1 else currentState.score
        val nextIndex = currentState.currentQuestionIndex + 1
        val isFinished = nextIndex >= currentState.questions.size

        _uiState.update {
            it.copy(
                score = newScore,
                currentQuestionIndex = if (isFinished) currentState.currentQuestionIndex else nextIndex,
                selectedOptionIndex = null,
                isFinished = isFinished
            )
        }
    }

    fun restartQuiz() {
        _uiState.update {
            QuizUiState(questions = repository.getQuestions())
        }
    }
}