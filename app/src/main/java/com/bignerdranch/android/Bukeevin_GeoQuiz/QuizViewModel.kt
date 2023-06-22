package com.bignerdranch.android.Bukeevin_GeoQuiz

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.bukreevin_geoquiz.R

class QuizViewModel : ViewModel() {
    private val TAG = "QuizViewModel"

    var currentIndex = 0
    private val questionBank = listOf(
        Question(R.string.question_oceans, true, false, false),
        Question(R.string.question_mideast, false, false, false),
        Question(R.string.question_africa, false, false, false),
        Question(R.string.question_americas, true, false, false),
        Question(R.string.question_asia, true, false, false)
    )
    val currentQuestionAnswer: Boolean
        get() =
            questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() =
            questionBank[currentIndex].textResId
    fun moveToNext() {
        currentIndex = (currentIndex + 1) %
                questionBank.size
    }

}



