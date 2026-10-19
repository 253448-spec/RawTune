package com.upchiapas.kt_template.quiz.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upchiapas.kt_template.KtApplication
import com.upchiapas.kt_template.quiz.data.Question
import com.upchiapas.kt_template.quiz.presentation.components.QuestionContent
import com.upchiapas.kt_template.quiz.presentation.components.ResultContent
import com.upchiapas.kt_template.quiz.presentation.viewmodels.QuizViewModel

/**
 * Stateful Composable: Se encarga de conectar el ViewModel con el estado de la UI
 * y delegar la renderización al Composable Stateless [QuizContent].
 */
@Composable
fun QuizPage(
    viewModel: QuizViewModel = viewModel(
        factory = (LocalContext.current.applicationContext as KtApplication).quizModule.factoryQuiz
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    QuizContent(
        uiState = uiState,
        onOptionSelected = { index -> viewModel.selectOption(index) },
        onSubmit = { viewModel.submitAnswer() },
        onRestart = { viewModel.restartQuiz() }
    )
}

/**
 * Stateless Composable: No conoce el ViewModel ni el origen de datos.
 * Recibe únicamente el [QuizUiState] e invoca callbacks ante eventos del usuario.
 * Facilita pruebas de UI y renderizado en Previews de Compose.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizContent(
    uiState: QuizUiState,
    onOptionSelected: (Int) -> Unit,
    onSubmit: () -> Unit,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Quiz App - Compose & MVVM") }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isFinished) {
                ResultContent(
                    score = uiState.score,
                    totalQuestions = uiState.questions.size,
                    onRestart = onRestart
                )
            } else {
                uiState.currentQuestion?.let {
                    QuestionContent(
                        uiState = uiState,
                        onOptionSelected = onOptionSelected,
                        onSubmit = onSubmit
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizContentPreview() {
    val sampleQuestions = listOf(
        Question(
            id = 1,
            text = "¿Cuál es el lenguaje oficial recomendado para desarrollar Android?",
            options = listOf("Java", "Kotlin", "Python"),
            correctAnswerIndex = 1
        )
    )
    QuizContent(
        uiState = QuizUiState(
            questions = sampleQuestions,
            currentQuestionIndex = 0,
            selectedOptionIndex = 1
        ),
        onOptionSelected = {},
        onSubmit = {},
        onRestart = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun QuizContentFinishedPreview() {
    QuizContent(
        uiState = QuizUiState(
            questions = emptyList(),
            score = 3,
            isFinished = true
        ),
        onOptionSelected = {},
        onSubmit = {},
        onRestart = {}
    )
}
