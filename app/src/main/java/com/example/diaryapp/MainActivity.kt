package com.example.diaryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    private val TAG : String = "MainActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    val RC_SIGN_IN = 9001

    lateinit var login_email : EditText
    lateinit var login_password : EditText

    lateinit var btn_signUp : Button
    lateinit var btn_login : Button
    lateinit var btnGoogle : SignInButton

    /*
    로그인하는 방법이 1.이메일+비밀번호(여기서는 이메일이 아이디와 같은 역할), 2.구글 로그인인데
    btn_login은 1번 방법,  btnGoogle은 2번 방법
     */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        login_email = findViewById(R.id.loginEmail)
        login_password = findViewById(R.id.loginPassword)

        btn_signUp = findViewById(R.id.btnSignUp)
        btn_login = findViewById(R.id.btnLogin)
        btnGoogle = findViewById(R.id.btnGoogle)

        //firebase_web_client_id는 원래 자동으로 string에 추가되어야하는데 안돼서 직접 (project로 바꿔서!) app->google-services.json에서 가져옴
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.firebase_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)

        //1.이메일+비밀번호 로그인
        btn_login.setOnClickListener {
            if(login_email.text.toString()=="" || login_password.text.toString()==""){
                Toast.makeText(this, "이메일, 비밀번호 칸을 모두 입력하세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                auth.signInWithEmailAndPassword(login_email.text.toString(), login_password.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success")
                                val intent = Intent(this, DiaryActivity::class.java)
                                startActivity(intent)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "아이디 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
            }
        }

        btn_signUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnGoogle.setOnClickListener {
            googleSignIn()
        }
    }

    //2.구글 로그인
    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "Google 로그인 성공")
                    loginSuccess()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    //로그인 성공하면 DiaryActivity로
    private fun loginSuccess(){
        val intent = Intent(this, DiaryActivity::class.java)
        startActivity(intent)
        finish()
    }
}