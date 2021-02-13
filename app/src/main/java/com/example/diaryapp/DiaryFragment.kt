package com.example.diaryapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * A simple [Fragment] subclass.
 * Use the [DiaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class DiaryFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var calendarView: CalendarView
    lateinit var navController: NavController
    lateinit var db: FirebaseFirestore

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        auth = FirebaseAuth.getInstance()

        return inflater.inflate(R.layout.fragment_diary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView = view.findViewById(R.id.calendarView)

        navController = Navigation.findNavController(view)

        //calendar 선택하면 년, 월, 일 정보 받아와 QuestionDiaryActivity에 데이터 전달
        //사용자 이메일을 이용해 내부 저장소에 일기 내용을 저장하기 때문에 함께 QuestionDiaryActivity로 데이터 전달
        calendarView.setOnDateChangeListener { view, year, month, day ->

            val intent = Intent(activity, QuestionDiaryActivity::class.java)
            intent.putExtra("year", year.toString())
            intent.putExtra("month", (month + 1).toString())
            intent.putExtra("day", day.toString())

            val user = FirebaseAuth.getInstance().currentUser
            db = FirebaseFirestore.getInstance()

            user?.let {
                intent.putExtra("userEmail", user.email)
                startActivity(intent)
            }
        }
    }
}