package com.example.diaryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

//회원가입 진행하는 창
class RegisterActivity : AppCompatActivity() {
    private val TAG : String = "RegisterActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore

    lateinit var register_email : EditText
    lateinit var register_password : EditText
    lateinit var register_passwordChk : EditText
    lateinit var register_name : EditText
    lateinit var register_num : EditText

    lateinit var email : String
    lateinit var password : String
    lateinit var passwordChk : String
    lateinit var name : String
    lateinit var num : String

    lateinit var btn_register : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_email = findViewById(R.id.registerEmail)
        register_password = findViewById(R.id.registerPassword)
        register_passwordChk = findViewById(R.id.registerPasswordChk)
        register_name = findViewById(R.id.registerName)
        register_num = findViewById(R.id.registerNum)

        email = register_email.text.toString()
        password = register_password.text.toString()

        btn_register = findViewById(R.id.btnRegister)

        auth = FirebaseAuth.getInstance()

        btn_register.setOnClickListener {
            if(validForm())
                createUser(email, password)
        }
    }

    //이메일+비밀번호 매개변수로 받아와 회원가입
    //잘 됐는지 결과에 따라 Toast랑 Log 작성
    private fun createUser(email: String, password: String) {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        name = register_name.text.toString()
        num = register_num.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) { //회원가입 잘 됐을 때
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        //db에 등록
                        var result = HashMap<String, String>()
                        result.put("email", email)
                        result.put("name", name)
                        result.put("PhoneNum", num)
                        db.collection("users").document(email)
                            .set(result)
                            .addOnSuccessListener {
                                //db 등록 성공
                                Log.d(TAG, "DocumentSnapshot added")
                                Toast.makeText(this, "회원가입 완료", Toast.LENGTH_LONG).show()
                                finish()
                            }
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

        email = register_email.text.toString()
        password = register_password.text.toString()
        passwordChk = register_passwordChk.text.toString()
        name = register_name.text.toString()
        num = register_num.text.toString()

        if (email.isEmpty()){
            Toast.makeText(baseContext, "이메일을 입력하세요", Toast.LENGTH_SHORT).show()
            validck = false
        }

        if (password.isEmpty()){
            Toast.makeText(baseContext, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
            validck = false
        }

        if (passwordChk.isEmpty()){
            Toast.makeText(baseContext, "비밀번호 확인을 진행하세요", Toast.LENGTH_SHORT).show()
            validck = false
        }

        if (name.isEmpty()){
            Toast.makeText(baseContext, "이름을 입력하세요", Toast.LENGTH_SHORT).show()
            validck = false
        }

        if (num.isEmpty()){
            Toast.makeText(baseContext, "전화번호를 입력하세요", Toast.LENGTH_SHORT).show()
            validck = false
        }

        if (password != passwordChk){
            Toast.makeText(baseContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            validck = false
        }

        if (password.contains(" ")){
            Toast.makeText(baseContext, "비밀번호는 영문+숫자 6자리 이상(공백 허용않음)", Toast.LENGTH_SHORT).show()
            validck = false
        }

        return validck
    }
}