package com.example.diaryapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.finishAffinity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MypageFragment : Fragment() {
    val TAG : String = "MyPageFragment"
    lateinit var name : TextView
    lateinit var email : TextView
    lateinit var btnEditpass : Button
    lateinit var btnLogout : Button
    lateinit var btnUnsubscribe : Button

    lateinit var auth : FirebaseAuth
    lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name = view.findViewById(R.id.AccountName)
        email = view.findViewById(R.id.Accountemail)
        btnLogout = view.findViewById(R.id.btnLogout)
        btnUnsubscribe = view.findViewById(R.id.btnUnsubscribe)
        btnEditpass = view.findViewById(R.id.btnEditpass)

        val user = FirebaseAuth.getInstance().currentUser
        db = FirebaseFirestore.getInstance()

        user?.let {
            email.text = user.email
            //이메일+비밀번호로 로그인했을 때 DB에 저장되는 이름 불러오기
            db.collection("users")
                .whereEqualTo("email", user.email)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents != null) {
                        for (document in documents) {
                            var get_user = document.data
                            var get_name = get_user["name"].toString()
                            name.text = get_name
                        }
                    }
                }
            //구글 로그인했을 때 파이어베이스에서 제공하는 메소드로 불러오기
            for (profile in it.providerData) {
                name.text = profile.displayName
            }
        }

        auth = FirebaseAuth.getInstance()

        btnEditpass.setOnClickListener {
            if (user != null) {
                auth.sendPasswordResetEmail(user.email.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Email sent.")
                            auth.signOut()
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
            } else {
                Log.d(TAG, "No such document")
                Toast.makeText(activity, "존재하지 않은 유저 입니다.", Toast.LENGTH_LONG).show()
            }
        }

        btnLogout.setOnClickListener {
            Toast.makeText(this.context, "로그아웃", Toast.LENGTH_SHORT).show()
            auth.signOut()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        btnUnsubscribe.setOnClickListener {
            if (user != null) {
                //users DB에 저장했던 유저 정보 이메일로 찾아서 먼저 삭제
                db.collection("users").document(user.email.toString()).delete()
                //계정 정보 삭제
                user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("delete user", "User account deleted.")
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
            }else{
                Toast.makeText(this.context, "현재 유저가 없습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }
}