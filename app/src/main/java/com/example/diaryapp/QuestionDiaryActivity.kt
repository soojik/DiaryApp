package com.example.diaryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class QuestionDiaryActivity : AppCompatActivity() {
    lateinit var monthTextView:TextView
    lateinit var dayTextView:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_diary)

        monthTextView = findViewById<TextView>(R.id.monthTextView)
        dayTextView = findViewById<TextView>(R.id.monthTextView)
        var year = intent.getStringExtra("year")+"년"
        var month = intent.getStringExtra("month")+"월"
        var day = intent.getStringExtra("day")+"일"
        monthTextView.setText(month)
        dayTextView.setText(day)
    }
}
