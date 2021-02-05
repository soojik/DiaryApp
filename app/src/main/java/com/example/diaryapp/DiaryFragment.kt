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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 * Use the [DiaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class DiaryFragment : Fragment() {
    lateinit var auth : FirebaseAuth
    lateinit var btnLogout : Button
    lateinit var btnFeeling : Button
    lateinit var btnWeather : Button
    lateinit var calendarView: CalendarView
    lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

        btnLogout = view.findViewById(R.id.btnLogout)
        btnFeeling = view.findViewById(R.id.btnFeeling)
        btnWeather = view.findViewById(R.id.btnWeather)
        calendarView = view.findViewById(R.id.calendarView)

        navController = Navigation.findNavController(view)

        calendarView.setOnDateChangeListener {  view, year, month, day->
            navController.navigate(R.id.action_navDiary_to_navQuestionDiary)
        }

        btnLogout.setOnClickListener {
            Toast.makeText(this.context, "로그아웃", Toast.LENGTH_SHORT).show()
            auth.signOut()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        btnWeather.setOnClickListener {
            val intent = Intent(activity, WeatherActivity::class.java)
            startActivity(intent)
        }

        btnFeeling.setOnClickListener {
            val intent = Intent(activity, FeelingActivity::class.java)
            startActivity(intent)
        }
    }
}