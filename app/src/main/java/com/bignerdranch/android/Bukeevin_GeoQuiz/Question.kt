package com.bignerdranch.android.Bukeevin_GeoQuiz;

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean, val userAnswer: Boolean, val correctAnswer: Boolean)

