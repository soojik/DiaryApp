package com.example.diaryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class QuestionDiaryActivity : AppCompatActivity() {
    lateinit var monthTextView:TextView
    lateinit var dayTextView:TextView
    lateinit var yearTextView:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_diary)

        //이건 액티비티였을 때 받아와서 하는거고 프레그먼트로 계속 갈거면 이 파일 자체를 삭제해도 될 것 같아요~
        //왜냐면 navigation diaryFragment에서 말했던것처럼 calenderView누르면 DiaryFragment에서 questionDiary로 프레그먼트 전환되도록 해놔서요!
        monthTextView = findViewById<TextView>(R.id.monthTextView)
        dayTextView = findViewById<TextView>(R.id.dayTextView)
        yearTextView = findViewById<TextView>(R.id.yearTextView)
        var year = intent.getStringExtra("year")+"년"
        var month = intent.getStringExtra("month")+"월"
        var day = intent.getStringExtra("day")+"일"
        yearTextView.setText(year)
        monthTextView.setText(month)
        dayTextView.setText(day)
    }
}
