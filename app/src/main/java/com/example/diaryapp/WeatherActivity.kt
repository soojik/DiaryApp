package com.example.diaryapp


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

//날씨 선택창
class WeatherActivity : AppCompatActivity() {

    lateinit var imgViewSun : ImageView
    lateinit var imgViewCloudy1: ImageView
    lateinit var imgViewCloudy2: ImageView
    lateinit var imgViewWindy: ImageView
    lateinit var imgViewRainy: ImageView
    lateinit var imgViewSnowy: ImageView
    lateinit var imgViewThunder: ImageView
    lateinit var imgViewRainbow: ImageView
    lateinit var imgViewHailstorm: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)


        imgViewSun = findViewById<ImageView>(R.id.imgViewSun)
        imgViewCloudy1= findViewById<ImageView>(R.id.imgViewCloudy1)
        imgViewCloudy2= findViewById<ImageView>(R.id.imgViewCloudy2)
        imgViewWindy= findViewById<ImageView>(R.id.imgViewWindy)
        imgViewRainy= findViewById<ImageView>(R.id.imgViewRainy)
        imgViewSnowy= findViewById<ImageView>(R.id.imgViewSnowy)
        imgViewThunder= findViewById<ImageView>(R.id.imgViewThunder)
        imgViewRainbow= findViewById<ImageView>(R.id.imgViewRainbow)
        imgViewHailstorm= findViewById<ImageView>(R.id.imgViewHail)

        val intent = Intent(this, QuestionDiaryActivity::class.java)

        imgViewSun.setOnClickListener {
            intent.putExtra("weather", "sunny")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        imgViewCloudy1.setOnClickListener {
            intent.putExtra("weather", "cloudy1")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewCloudy2.setOnClickListener {
            intent.putExtra("weather", "cloudy2")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        imgViewWindy.setOnClickListener {
            intent.putExtra("weather", "windy")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewRainy.setOnClickListener {
            intent.putExtra("weather", "rainy")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewSnowy.setOnClickListener {
            intent.putExtra("weather", "snowy")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewThunder.setOnClickListener {
            intent.putExtra("weather", "thunderstorm")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        imgViewRainbow.setOnClickListener {
            intent.putExtra("weather", "rainbow")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        imgViewHailstorm.setOnClickListener {
            intent.putExtra("weather", "hail")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }



    }

}
