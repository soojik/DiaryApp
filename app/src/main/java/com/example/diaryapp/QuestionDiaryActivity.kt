package com.example.diaryapp

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.AudioManager
import android.media.MediaParser
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth //삭제
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.* //삭제
import java.util.concurrent.TimeUnit

class QuestionDiaryActivity : AppCompatActivity() {

    //var fName : String = "" //삭제

    var userName : String = "kimswunie" //나중에 db에서 이름 정보 가져와서 바꿀 부분 //삭제

    lateinit var monthTextView:TextView
    lateinit var dayTextView:TextView
    lateinit var yearTextView:TextView
    lateinit var diaryEditView:EditText
    lateinit var questionTextView : TextView
    lateinit var answerEditText : EditText
    lateinit var answerBtn : Button
    lateinit var diarySaveBtn:Button
    lateinit var weatherImgView : ImageView
    lateinit var feelingImgView : ImageView
    lateinit var imgUploadBtn : Button
    lateinit var cameraBtn : Button
    lateinit var galleryBtn : Button
    lateinit var imageImgView : ImageView
    lateinit var recordBtn : Button
    lateinit var recStartBtn : Button
    lateinit var recStopBtn : Button
    lateinit var recHearStartBtn : ImageButton
    lateinit var recHearStopBtn : ImageButton
    lateinit var seekbar : SeekBar
    lateinit var recHearSoundBtn : ImageView
    lateinit var mr : MediaRecorder
    lateinit var mp : MediaPlayer
    lateinit var handler: Handler

    val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    val STORAGE_PERMISSION = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val FLAG_PERM_CAMERA = 98
    val FLAG_PERM_STORAGE = 99
    val FLAG_REQ_CAMERA = 101
    val FLAG_REQ_STORAGE = 102

    private lateinit var startTime: TextView
    private lateinit var songTime: TextView
    private var onTime: Int = 0
    private var playTime: Int = 0
    private var endTime: Int = 0

////////////////////////////////////////////
    lateinit var db : FirebaseFirestore
    ///////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_diary)





        monthTextView = findViewById<TextView>(R.id.monthTextView)
        dayTextView = findViewById<TextView>(R.id.dayTextView)
        yearTextView = findViewById<TextView>(R.id.yearTextView)
        questionTextView = findViewById<TextView>(R.id.questionTextView)
        diaryEditView = findViewById<EditText>(R.id.diaryEditView)
        answerEditText = findViewById<EditText>(R.id.answerEditText)
        answerBtn = findViewById(R.id.answerBtn)
        diarySaveBtn = findViewById(R.id.diarySaveBtn)
        weatherImgView = findViewById(R.id.weatherImgView)
        feelingImgView = findViewById(R.id.feelingImgView)
        imgUploadBtn = findViewById(R.id.imgUploadBtn)
        cameraBtn = findViewById(R.id.cameraBtn)
        galleryBtn = findViewById(R.id.galleryBtn)
        imageImgView = findViewById(R.id.imageImgView)
        recordBtn = findViewById(R.id.recordBtn)
        recStartBtn = findViewById(R.id.recStartBtn)
        recStopBtn = findViewById(R.id.recStopBtn)
        recHearStartBtn = findViewById(R.id.recHearStartBtn)
        recHearStopBtn = findViewById(R.id.recHearStopBtn)
        seekbar = findViewById(R.id.seekBar)
        recHearSoundBtn = findViewById(R.id.recHearSoundBtn)
        mr = MediaRecorder()
        mp = MediaPlayer()
        seekbar.isClickable = false
        seekbar.isEnabled = true

        //activity_question_diary.xml에서 선택한 년,월,일 변수에 저장
        startTime = findViewById(R.id.txtStartTime)
        songTime = findViewById(R.id.txtSongTime)

        var year = intent.getStringExtra("year").toString()
        var month = intent.getStringExtra("month").toString()
        var day = intent.getStringExtra("day").toString()
        var userName = intent.getStringExtra("userName").toString()


        //선택한 날짜 설정
        yearTextView.setText(year + "년")
        monthTextView.setText(month + "월")
        dayTextView.setText(day + "일")

        //해당 날짜에 저장된 질문과 답변 불러오기
        checkedDayQuestion(year, month, day)
        checkedDayAnswer(year, month, day)

        //해당 날짜에 저장된 일기 불러오기
        readDiary(year,month,day)
        saveDiary( year + month + day + "_" + userName + "_" + "Diary" + ".txt",diaryEditView)

        //답변 버튼 누르면 답변 저장하기
        answerBtn.setOnClickListener {
            var fName = "" + year + month + day + "_" + userName + "_" + "Answer" + ".txt"
            saveFileEditText(fName, answerEditText)
        }

        //해당 날짜에 저장된 날씨와 감정 불러오기
        setFeelingOrWeather(year, month, day, "feeling")
        setFeelingOrWeather(year, month, day, "weather")

        //감정 표현 이미지 변경하기
        feelingImgView.setOnClickListener {
            val intent = Intent(this, FeelingActivity::class.java) //감정 선택할 수 있는 액티비티로 변환
             startActivityForResult(intent, 1)
        }

        //날씨 이미지 변경하기
        weatherImgView.setOnClickListener {
            val intent = Intent(this, WeatherActivity::class.java) //날씨 선택할 수 있는 액티비티로 변환
            startActivityForResult(intent,2)
        }

        if (checkPermission(STORAGE_PERMISSION, FLAG_PERM_STORAGE)) {
            setViews()
        }

        imgUploadBtn.setOnClickListener {
            imgUploadBtn.visibility = View.GONE
            cameraBtn.visibility = View.VISIBLE
            galleryBtn.visibility = View.VISIBLE
            imageImgView.visibility = View.VISIBLE
        }


        recordBtn.setOnClickListener {
            recordBtn.visibility = View.GONE
            recStartBtn.visibility = View.VISIBLE
        }

        recStartBtn.isEnabled = false
        recStopBtn.isEnabled = false

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)
            recStartBtn.isEnabled = true

        recStartBtn.setOnClickListener {
            startRecording()
        }
        recStopBtn.setOnClickListener {
            stopRecording()
        }

        recHearStartBtn.setOnClickListener {
            playRecord()
        }

        recHearStopBtn.setOnClickListener {
            stopRecord()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var year = intent.getStringExtra("year").toString()
        var month = intent.getStringExtra("month").toString()
        var day = intent.getStringExtra("day").toString()
        var userName = intent.getStringExtra("userName").toString()

        when (requestCode) {
            1 -> {
                val feeling = data!!.getStringExtra("feeling").toString()
                var fName = "" + year + month + day + "_" + userName + "_" + "feeling" + ".txt" //기분 파일
                if (resultCode == Activity.RESULT_OK) {
                    saveFeelingOrWeather(fName, feeling, "feeling")
                }
            }
            2 -> {
                val weather = data?.getStringExtra("weather").toString()
                var fName = "" + year + month + day + "_" + userName + "_" + "weather" + ".txt" //날씨 파일
                if (resultCode == Activity.RESULT_OK) {
                    saveFeelingOrWeather(fName, weather, "weather")
                }
            }
        }

        if(resultCode == Activity.RESULT_OK) {
            when(requestCode){
                FLAG_REQ_CAMERA -> {
                    if (data?.extras?.get("data") != null) {
                        val bitmap = data?.extras?.get("data") as Bitmap
//                        imagePreview.setImageBitmap(bitmap)
                        val uri = saveImageFile(newFileName(), "image/jpg", bitmap)
                        imageImgView.setImageURI(uri)
                    }
                }
                FLAG_REQ_STORAGE -> {
                    val uri = data?.data
                    imageImgView.setImageURI(uri)
                }
            }
        }

    }

    //선택한 기분 또는 날씨 저장
    fun saveFeelingOrWeather(fName : String, whatForW : String, ForW : String){

        var year = intent.getStringExtra("year").toString()
        var month = intent.getStringExtra("month").toString()
        var day = intent.getStringExtra("day").toString()

        try{
            var fos : FileOutputStream? = null
            fos = openFileOutput(fName, Context.MODE_PRIVATE)
            var content : String = whatForW
            fos.write(content.toByteArray())
            fos.close()
        } catch(e : Exception){
            e.printStackTrace()
        }
        setFeelingOrWeather(year, month, day, ForW)
    }

    //기분, 날씨 화면에 설정
    fun setFeelingOrWeather(Year : String, Month : String, Day : String, ForW : String) {
        var userName = intent.getStringExtra("userName").toString()

        var fName : String
        if (ForW == "feeling") {
            fName = "" + Year + Month + Day + "_" + userName + "_" + "feeling" + ".txt"
        } else {
            fName = "" + Year + Month + Day + "_" + userName + "_" + "weather" + ".txt"
        }

        var whatForW = readFile(fName) //저장된 이미지 파일명 텍스트 불러오기

        if(whatForW == ""){ //저장된 이미지가 없을 경우 기본 이미지 설정
            if(ForW=="feeling"){
                feelingImgView.setImageResource(R.drawable.happy_3)
            }else{
                weatherImgView.setImageResource(R.drawable.sunny)
            }
        }else{ // 저장된 이미지가 있을 경우 해당 이미지 설정
            var loc = "@drawable/$whatForW"
            var resID = getResources().getIdentifier(loc, "drawable", this.packageName)
            if (ForW == "feeling") {
                feelingImgView.setImageResource(resID)
            } else {
                weatherImgView.setImageResource(resID)
            }
        }
    }

    fun checkedDayQuestion(cYear : String, cMonth : String, cDay : String){ //질문 설정
        var userName = intent.getStringExtra("userName").toString() //삭제
        var fName = "" + cYear + cMonth + cDay + "_" + userName + "_" + "Question" + ".txt" //삭제



        db = FirebaseFirestore.getInstance()

        val docRef = db.collection("question").document(cMonth)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        var get_user = document.data
                        var qstr = get_user?.get("$cDay").toString()
                        questionTextView.setText(qstr)


                    }
                }
                .addOnFailureListener { exception ->

                }
//삭제
//        val randomNum = Random()
//        val num = randomNum.nextInt(30)
//        val qarray : Array<String> = resources.getStringArray(R.array.question)
//        var qstr = qarray[num] //랜덤으로 질문 저장
//
//        var str = readFile(fName)
//
//        if(str==""){ //질문이 안만들어졌으면 새로 질문을 만들고 질문 파일 생성
//            questionTextView.text = qstr
//            saveFileTextView(fName, questionTextView)
//        } else{ //질문 만들어져있으면 불러오기
//            questionTextView.text = str
//        }
    }

    fun checkedDayAnswer(cYear : String, cMonth : String, cDay : String){ //답변 설정
        var userName = intent.getStringExtra("userName").toString()
        var fName = "" + cYear + cMonth + cDay + "_" + userName + "_" + "Answer" + ".txt" //답변 파일 이름

        var str = readFile(fName)

        if(str!=""){
            answerEditText.setText(str)
        }
/*
        if(str==""){ //답변 없으면 answerEditText에 쓰여진 내용 파일로 저장
            answerBtn.setOnClickListener {
                saveFileEditText(fName, answerEditText)
                Toast.makeText(this, fName + " 데이터가 저장되었습니다.", Toast.LENGTH_SHORT).show()
            }
        } else{ //답변이 이미 존재하면 불러오기
            answerEditText.setText(str)
            answerBtn.setOnClickListener{
                saveFileEditText(fName, answerEditText)
            }
        }
        */
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

    //삭제
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

    // 저장된 일기 읽는 함수
    // 이미지 업로드
    fun setViews(){ // 이미지 업로드 함수
        cameraBtn.setOnClickListener {
            openCamera()
        }
        galleryBtn.setOnClickListener {
            openGallery()
        }
    }

    fun openCamera() { // 카메라 여는 함수
        if (checkPermission(CAMERA_PERMISSION, FLAG_PERM_CAMERA)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, FLAG_REQ_CAMERA)
        }
    }

    fun openGallery() { // 갤러리 여는 함수
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, FLAG_REQ_STORAGE)
    }


    fun saveImageFile(filename:String, mimeType:String, bitmap: Bitmap) : Uri? { // 사진 저장 함수
        var values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        try {
            if(uri != null) {
                var descriptor = contentResolver.openFileDescriptor(uri, "w")
                if(descriptor != null) {
                    val fos = FileOutputStream(descriptor.fileDescriptor)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    fos.close()
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        values.clear()
                        values.put(MediaStore.Images.Media.IS_PENDING, 0)
                        contentResolver.update(uri, values, null, null)
                    }
                }
            }
        }catch (e:java.lang.Exception) {
            Log.e("File", "error=${e.localizedMessage}")
        }
        return uri
    }

    fun newFileName() : String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())

        return "$filename.jpg"
    }

    fun checkPermission(permissions: Array<out String>, flag:Int) : Boolean { // 사진 권한 처리
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, flag)
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) { // 사진 권한 처리
        when (requestCode) {
            FLAG_PERM_STORAGE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "저장소 권한 필요", Toast.LENGTH_LONG).show()
                        finish()
                        return
                    }
                }
                setViews()
            }
            FLAG_PERM_CAMERA -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "카메라 권한 승인 필요", Toast.LENGTH_LONG).show()
                        return
                    }
                }
                openCamera()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            recStartBtn.isEnabled = true
    }

    // 일기
    fun readDiary(cYear : String, cMonth : String, cDay : String){ //일기
        var userName = intent.getStringExtra("userName").toString()
        var fName = "" + cYear + cMonth + cDay + "_" + userName + "_" + "Diary" + ".txt" //일기 파일 이름

        var str = readFile(fName)
        diaryEditView.setText(str)
    }

    //일기 저장하는 함수
    fun saveDiary(fName : String, widget : EditText){
        diarySaveBtn.setOnClickListener{ //저장 버튼을 누를 시 저장

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


    // 음성녹음
    fun startRecording(){
        mr.setAudioSource(MediaRecorder.AudioSource.MIC)
        mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mr.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
        var path = Environment.getExternalStorageDirectory().toString()+"/myrec.mp3"
        mr.setOutputFile(path)
        mr.prepare()
        mr.start()
        Toast.makeText(this, "녹음 시작", Toast.LENGTH_SHORT).show()
        recStopBtn.isEnabled = true
        recStartBtn.isEnabled = false
        recStartBtn.visibility = View.GONE
        recStopBtn.visibility = View.VISIBLE
    }

    fun stopRecording(){
        mr.stop()
        recStopBtn.visibility = View.GONE
        recStartBtn.visibility = View.VISIBLE
        recHearStartBtn.visibility = View.VISIBLE
        recHearStopBtn.visibility = View.VISIBLE
        seekbar.visibility = View.VISIBLE
        recHearSoundBtn.visibility = View.VISIBLE
        recStartBtn.isEnabled = true
        recStopBtn.isEnabled = false
    }

    fun playRecord(){
        Toast.makeText(this, "음성 재생", Toast.LENGTH_SHORT).show()
        mp.start()
        endTime = mp.duration
        playTime = mp.currentPosition
        seekbar.max = endTime
        onTime = 1
        seekbar.progress = playTime
        handler.postDelayed(updateSongTime, 100)
        recHearStopBtn.isEnabled = true
        recHearStartBtn.isEnabled = false
    }

    fun stopRecord() {
        mp.pause()
        recHearStopBtn.isEnabled = false
        recHearStartBtn.isEnabled = true
        Toast.makeText(applicationContext, "음성 멈춤", Toast.LENGTH_SHORT).show()
    }

    private val updateSongTime = object : Runnable {
        override fun run() {
            playTime = mp.currentPosition
                        seekbar.progress = playTime
                        handler.postDelayed(this, 100)
        }
    }

}