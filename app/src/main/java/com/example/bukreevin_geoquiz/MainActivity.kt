package com.example.bukreevin_geoquiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import android.view.View
import com.example.bukreevin_geoquiz.databinding.ActivityMainBinding
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.Bukeevin_GeoQuiz.Question
import com.bignerdranch.android.Bukeevin_GeoQuiz.QuizViewModel
import com.example.bukreevin_geoquiz.ResultActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView
    private val TAG = "MainActivity"

    private var currentIndex = 0

    private val questionBank = listOf(
        Question(R.string.question_oceans, true, false, false),
        Question(R.string.question_mideast, false, false, false),
        Question(R.string.question_africa, false, false, false),
        Question(R.string.question_americas, true, false, false),
        Question(R.string.question_asia, true, false, false)
    )

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
        if (currentIndex == 0) {
            prevButton.visibility = View.INVISIBLE
        } else {
            prevButton.visibility = View.VISIBLE
        }
        trueButton.isEnabled = true
        falseButton.isEnabled = true
        trueButton.visibility = View.VISIBLE
        falseButton.visibility = View.VISIBLE

    }

    companion object {
        private const val KEY_CURRENT_INDEX = "current_index"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")

        val correctAnswers = intent.getIntExtra("CORRECT_ANSWERS", 0)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.previes_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        nextButton.setOnClickListener { view: View ->
            quizViewModel.moveToNext()
            currentIndex = quizViewModel.currentIndex
            updateQuestion()

            if (currentIndex == questionBank.size - 1) {
                nextButton.isEnabled = false
                nextButton.visibility = View.INVISIBLE

                val correctAnswers = calculateCorrectAnswers()
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("CORRECT_ANSWERS", correctAnswers)
                startActivity(intent)
            }


        }

        prevButton.setOnClickListener { view: View ->
            if (currentIndex > 0) {
                currentIndex = (currentIndex - 1) % questionBank.size
            } else {
                currentIndex = questionBank.size - 1
            }
            val questionTextResId = questionBank[currentIndex].textResId
            questionTextView.setText(questionTextResId)

            prevButton.visibility = View.INVISIBLE

            nextButton.isEnabled = true
            nextButton.visibility = View.VISIBLE
        }

        quizViewModel.currentIndex = currentIndex

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX)
            quizViewModel.currentIndex = currentIndex
        }

        updateQuestion()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_INDEX, currentIndex)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX)
        quizViewModel.currentIndex = currentIndex
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        trueButton.isEnabled = false
        falseButton.isEnabled = false
        trueButton.visibility = View.INVISIBLE
        falseButton.visibility = View.INVISIBLE

        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        if (currentIndex < questionBank.size - 1) {
            nextButton.isEnabled = true
            nextButton.visibility = View.VISIBLE
        }
    }
    private fun calculateCorrectAnswers(): Int {
        var correctCount = 0
        for (question in questionBank) {
            if (question.userAnswer == question.correctAnswer) {
                correctCount++
            }
        }
        return correctCount
    }

}
