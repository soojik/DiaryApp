package com.example.diaryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class DiaryActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    lateinit var btn_logout : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        auth = FirebaseAuth.getInstance()

        btn_logout = findViewById(R.id.btn_logout)

        btn_logout.setOnClickListener {
            Toast.makeText(baseContext, "로그아웃", Toast.LENGTH_SHORT).show()
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}