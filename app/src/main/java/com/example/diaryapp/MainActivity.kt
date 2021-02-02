package com.example.diaryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private val TAG : String = "MainActivity"
    private lateinit var auth: FirebaseAuth

    lateinit var login_email : EditText
    lateinit var login_password : EditText

    lateinit var btn_signUp : Button
    lateinit var btn_login : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        login_email = findViewById(R.id.login_email)
        login_password = findViewById(R.id.login_password)

        btn_signUp = findViewById(R.id.btn_signUp)
        btn_login = findViewById(R.id.btn_login)

        btn_login.setOnClickListener {
            auth.signInWithEmailAndPassword(login_email.text.toString(), login_password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        Toast.makeText(baseContext, "로그인 완료", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, DiaryActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "아이디 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        btn_signUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}