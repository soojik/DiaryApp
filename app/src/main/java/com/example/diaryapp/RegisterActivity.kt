package com.example.diaryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

//회원가입 진행하는 창
class RegisterActivity : AppCompatActivity() {
    private val TAG : String = "RegisterActivity"
    private lateinit var auth: FirebaseAuth

    lateinit var register_email : EditText
    lateinit var register_password : EditText
    lateinit var register_passwordChk : EditText
    lateinit var register_name : EditText
    lateinit var register_num : EditText

    lateinit var btn_register : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_email = findViewById(R.id.register_email)
        register_password = findViewById(R.id.register_password)
        register_passwordChk = findViewById(R.id.register_passwordChk)
        register_name = findViewById(R.id.register_name)
        register_num = findViewById(R.id.register_num)

        btn_register = findViewById(R.id.btn_register)

        auth = FirebaseAuth.getInstance()

        btn_register.setOnClickListener {
            if(validForm())
                createUser(register_email.text.toString(), register_password.text.toString())
        }
    }

    //이메일+비밀번호 매개변수로 받아와 회원가입
    //잘 됐는지 결과에 따라 Toast랑 Log 작성
    private fun createUser(email: String, password: String) {
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) { //회원가입 잘 됐을 때
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        Toast.makeText(baseContext, "회원가입 완료", Toast.LENGTH_SHORT).show()
                        finish() //Activity 닫음
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "회원가입 오류\n>올바른 이메일 형식\n>비밀번호는 영문+숫자 6자리 이상(공백 허용않음)\n>이메일 중복 허용않음", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    //모든 폼이 조건을 충족했는지 확인하는 함수
    private fun validForm() : Boolean {
        var validck = true

        if (register_email.text.toString().isEmpty()){
            Toast.makeText(baseContext, "이메일을 입력하세요", Toast.LENGTH_SHORT).show()
            validck = false
        }

        if (register_password.text.toString().isEmpty()){
            Toast.makeText(baseContext, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
            validck = false
        }

        if (register_passwordChk.text.toString().isEmpty()){
            Toast.makeText(baseContext, "비밀번호 확인을 진행하세요", Toast.LENGTH_SHORT).show()
            validck = false
        }

        if (register_name.text.toString().isEmpty()){
            Toast.makeText(baseContext, "이름을 입력하세요", Toast.LENGTH_SHORT).show()
            validck = false
        }

        if (register_num.text.toString().isEmpty()){
            Toast.makeText(baseContext, "전화번호를 입력하세요", Toast.LENGTH_SHORT).show()
            validck = false
        }

        if (register_password.text.toString() != register_passwordChk.text.toString()){
            Toast.makeText(baseContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            validck = false
        }

        if (register_password.text.toString().contains(" ")){
            Toast.makeText(baseContext, "비밀번호는 영문+숫자 6자리 이상(공백 허용않음)", Toast.LENGTH_SHORT).show()
            validck = false
        }

        return validck
    }
}