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
            intent.putExtra("img", "imgViewHappy1")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewHappy2.setOnClickListener {
            intent.putExtra("img", "imgViewHappy2")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewHappy3.setOnClickListener {
            intent.putExtra("img", "imgViewHappy3")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewLove.setOnClickListener {
            intent.putExtra("img", "imgViewLove")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewKiss.setOnClickListener {
            intent.putExtra("img", "imgViewKiss")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewSoso.setOnClickListener {
            intent.putExtra("img", "imgViewSoso")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewCrying.setOnClickListener {
            intent.putExtra("img", "imgViewCrying")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewMad.setOnClickListener {
            intent.putExtra("img", "imgViewMad")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewQuiet.setOnClickListener {
            intent.putExtra("img", "imgViewQuiet")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }
}