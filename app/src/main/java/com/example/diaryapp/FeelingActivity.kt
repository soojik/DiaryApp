package com.example.diaryapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ImageView

//기분 선택창
class FeelingActivity : AppCompatActivity() {

    lateinit var imgViewHappy1 : ImageView
    lateinit var imgViewHappy2 : ImageView
    lateinit var imgViewHappy3 : ImageView
    lateinit var imgViewLove : ImageView
    lateinit var imgViewKiss : ImageView
    lateinit var imgViewSoso : ImageView
    lateinit var imgViewCrying : ImageView
    lateinit var imgViewMad : ImageView
    lateinit var imgViewQuiet : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_feeling)

        imgViewHappy1 = findViewById(R.id.imgViewHappy1)
        imgViewHappy2 = findViewById(R.id.imgViewHappy2)
        imgViewHappy3 = findViewById(R.id.imgViewHappy3)
        imgViewLove = findViewById(R.id.imgViewLove)
        imgViewKiss = findViewById(R.id.imgViewKiss)
        imgViewSoso = findViewById(R.id.imgViewSoso)
        imgViewCrying = findViewById(R.id.imgViewCrying)
        imgViewMad = findViewById(R.id.imgViewMad)
        imgViewQuiet = findViewById(R.id.imgViewQuiet)

        val intent = Intent(this, QuestionDiaryActivity::class.java)

        imgViewHappy1.setOnClickListener {
            intent.putExtra("feeling", "happy_3")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewHappy2.setOnClickListener {
            intent.putExtra("feeling", "happy_2")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewHappy3.setOnClickListener {
            intent.putExtra("feeling", "happy_1")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewLove.setOnClickListener {
            intent.putExtra("feeling", "in_love")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewKiss.setOnClickListener {
            intent.putExtra("feeling", "kissing")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewSoso.setOnClickListener {
            intent.putExtra("feeling", "confused")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewCrying.setOnClickListener {
            intent.putExtra("feeling", "crying")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewMad.setOnClickListener {
            intent.putExtra("feeling", "mad")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewQuiet.setOnClickListener {
            intent.putExtra("feeling", "quiet")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }
}