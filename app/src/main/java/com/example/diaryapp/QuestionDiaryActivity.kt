package com.example.diaryapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class QuestionDiaryActivity : AppCompatActivity() {

    var fName : String = ""
    var userName : String = "kimswunie" //나중에 db에서 이름 정보 가져와서 바꿀 부분

    lateinit var monthTextView:TextView
    lateinit var dayTextView:TextView
    lateinit var yearTextView:TextView
    lateinit var diaryEditView:EditText
    lateinit var questionTextView : TextView
    lateinit var answerEditText : EditText
    lateinit var answerBtn : Button
    lateinit var  diarySaveBtn:Button
    lateinit var weatherView : ImageView
    lateinit var feelingImgView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_diary)

        //이건 액티비티였을 때 받아와서 하는거고 프레그먼트로 계속 갈거면 이 파일 자체를 삭제해도 될 것 같아요~
        //왜냐면 navigation diaryFragment에서 말했던것처럼 calenderView누르면 DiaryFragment에서 questionDiary로 프레그먼트 전환되도록 해놔서요!
        monthTextView = findViewById<TextView>(R.id.monthTextView)
        dayTextView = findViewById<TextView>(R.id.dayTextView)
        yearTextView = findViewById<TextView>(R.id.yearTextView)
        questionTextView = findViewById<TextView>(R.id.questionTextView)
        diaryEditView = findViewById<EditText>(R.id.diaryEditView)
        answerEditText = findViewById<EditText>(R.id.answerEditText)
        answerBtn = findViewById(R.id.answerBtn)
        diarySaveBtn = findViewById(R.id.diarySaveBtn)
        weatherView = findViewById(R.id.weatherView)
        feelingImgView = findViewById(R.id.feelingImgView)


        var year = intent.getStringExtra("year").toString()
        var monthString = intent.getStringExtra("month").toString()
        var day = intent.getStringExtra("day").toString()

        yearTextView.setText(year + "년")
        monthTextView.setText(monthString + "월")
        dayTextView.setText(day + "일")


        checkedDayQuestion(year, monthString, day)
        checkedDayAnswer(year, monthString, day)

        readDiary(year,monthString,day)
        saveDiary( year + monthString + day + "_" + userName + "_" + "Diary" + ".txt",diaryEditView)
        setWeather(year, monthString, day)

        weatherView.setOnClickListener {
            val intent = Intent(this, WeatherActivity::class.java)
            startActivityForResult(intent,1)
            Toast.makeText(this, "엑티비티 변환", Toast.LENGTH_SHORT).show()
        }

        feelingImgView.setOnClickListener {
            var intent = Intent(this, FeelingActivity::class.java)
            //intent.putExtra("imgg", "")
            startActivityForResult(intent, 1)
            /*
            val feeling = intent.getStringExtra("img").toString()
            when{
                feeling == "imgViewHappy1" -> feelingImgView.setImageResource(R.drawable.happy_3)
                feeling == "imgViewHappy2" -> feelingImgView.setImageResource(R.drawable.happy_2)
                feeling == "imgViewHappy3" -> feelingImgView.setImageResource(R.drawable.happy_1)
                feeling == "imgViewLove" -> feelingImgView.setImageResource(R.drawable.in_love)
                feeling == "imgViewKiss" -> feelingImgView.setImageResource(R.drawable.kissing)
                feeling == "imgViewSoso" -> feelingImgView.setImageResource(R.drawable.confused)
                feeling == "imgViewCrying" -> feelingImgView.setImageResource(R.drawable.crying)
                feeling == "imgViewMad" -> feelingImgView.setImageResource(R.drawable.mad)
                feeling == "imgViewQuiet" -> feelingImgView.setImageResource(R.drawable.quiet)
            }
             */
        }


    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {

            var weather = data?.getStringExtra("weather").toString()
            var year = intent.getStringExtra("year").toString()
            var monthString = intent.getStringExtra("month").toString()
            var day = intent.getStringExtra("day").toString()
            Toast.makeText(this, "onActivityresult"+ weather, Toast.LENGTH_SHORT).show()
            //Toast.makeText(this, loc, Toast.LENGTH_SHORT).show()

            saveWeather(year, monthString, day,weather) // 날씨 저장 함수
        }


            when(requestCode){
                1 -> {
                    val feeling = data!!.getStringExtra("img").toString()
                    when(resultCode == Activity.RESULT_OK){
                        feeling == "imgViewHappy1" -> feelingImgView.setImageResource(R.drawable.happy_3)
                        feeling == "imgViewHappy2" -> feelingImgView.setImageResource(R.drawable.happy_2)
                        feeling == "imgViewHappy3" -> feelingImgView.setImageResource(R.drawable.happy_1)
                        feeling == "imgViewLove" -> feelingImgView.setImageResource(R.drawable.in_love)
                        feeling == "imgViewKiss" -> feelingImgView.setImageResource(R.drawable.kissing)
                        feeling == "imgViewSoso" -> feelingImgView.setImageResource(R.drawable.confused)
                        feeling == "imgViewCrying" -> feelingImgView.setImageResource(R.drawable.crying)
                        feeling == "imgViewMad" -> feelingImgView.setImageResource(R.drawable.mad)
                        feeling == "imgViewQuiet" -> feelingImgView.setImageResource(R.drawable.quiet)
                    }
                }
            }


    }

    //선택한 날씨 저장
    fun saveWeather(cYear : String, cMonth : String, cDay : String,weather : String){
        fName = "" + cYear + cMonth + cDay + "_" + userName + "_" + "weather"+".txt" //날씨 파일

            try{
                var fos : FileOutputStream? = null
                fos = openFileOutput(fName, Context.MODE_PRIVATE)
                var content : String = weather
                fos.write(content.toByteArray())
                fos.close()
            } catch(e : Exception){
                e.printStackTrace()
            }
        setWeather(cYear,cMonth,cDay)
    }

    //저장한 날씨 설정하기
    fun setWeather(cYear : String, cMonth : String, cDay : String){
        fName = "" + cYear + cMonth + cDay + "_" + userName + "_" + "weather"+".txt"
        var weather = readFile(fName)
        Toast.makeText(this, "set :"+ weather +"   날씨 ", Toast.LENGTH_LONG).show()

        if(weather == "")
             weatherView.setImageResource(R.drawable.sunny)

//한번에 이미지 불러오기
        else {
            val resName = "@drawable/$weather"
            val packName = this.packageName
            val resID = getResources().getIdentifier(resName, "drawable", packName);
            weatherView.setImageResource(resID)
        }


    }



    fun checkedDayQuestion(cYear : String, cMonth : String, cDay : String){ //질문
        fName = "" + cYear + cMonth + cDay + "_" + userName + "_" + "Question" + ".txt" //질문 파일 이름

        val randomNum = Random()
        val num = randomNum.nextInt(10)
        val qarray : Array<String> = resources.getStringArray(R.array.question)
        var qstr = qarray[num] //랜덤으로 질문 저장

        var str = readFile(fName)

        if(str==""){ //질문이 안만들어졌으면 새로 질문을 만들고 질문 파일 생성
            questionTextView.text = qstr
            saveFileTextView(fName, questionTextView)
        } else{ //질문 만들어져있으면 불러오기
            questionTextView.text = str
        }
    }



    fun checkedDayAnswer(cYear : String, cMonth : String, cDay : String){ //답변
        fName = "" + cYear + cMonth + cDay + "_" + userName + "_" + "Answer" + ".txt" //답변 파일 이름

        var str = readFile(fName)

        if(str==""){ //답변 없으면 answerEditText에 쓰여진 내용 파일로 저장
            answerBtn.setOnClickListener {
                saveFileEditText(fName, answerEditText)
                Toast.makeText(this, fName + " 데이터가 저장되었습니다.", Toast.LENGTH_SHORT).show()
            }
        } else{ //답변이 이미 존재하면 불러오기
            answerEditText.setText(str)
        }
    }

    fun readFile(fName : String) : String{ //파일 읽는 함수
        lateinit var str : String

        try {
            var fis : FileInputStream? = null
            fis = openFileInput(fName)
            val fileData = ByteArray(fis.available()) //fileData에 바이트 형식으로 저장
            fis.read(fileData)
            fis.close()

            str = String(fileData)
        } catch(e : Exception){
            str = ""
        }
        return str
    }

    fun saveFileTextView(fName : String, widget : TextView){ //TextView인 질문 저장하는 함수
        try{
            var fos : FileOutputStream? = null
            fos = openFileOutput(fName, Context.MODE_PRIVATE)
            var content : String = widget.text.toString()
            fos.write(content.toByteArray())
            fos.close()
        } catch(e : Exception){
            e.printStackTrace()
        }
    }

    fun saveFileEditText(fName : String, widget : EditText){ //EditText인 답변 저장하는 함수
        try{
            var fos : FileOutputStream? = null
            fos = openFileOutput(fName, Context.MODE_PRIVATE)
            var content : String = widget.getText().toString()
            fos.write(content.toByteArray())
            fos.close()
        } catch(e : Exception){
            e.printStackTrace()
        }
    }

    // 일기
    fun readDiary(cYear : String, cMonth : String, cDay : String){ //일기
        fName = "" + cYear + cMonth + cDay + "_" + userName + "_" + "Diary" + ".txt" //일기 파일 이름

        var str = readFile(fName)
        diaryEditView.setText(str)
    }

    fun saveDiary(fName : String, widget : EditText){
        diarySaveBtn.setOnClickListener{

            try{
                var fos : FileOutputStream? = null
                fos = openFileOutput(fName, Context.MODE_PRIVATE)
                var content : String = widget.getText().toString()
                fos.write(content.toByteArray())
                fos.close()
            } catch(e : Exception){
                e.printStackTrace()
            }

        }

    }



}
