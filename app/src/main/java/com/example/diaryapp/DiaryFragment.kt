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
    lateinit var auth : FirebaseAuth
    lateinit var calendarView: CalendarView
    lateinit var navController : NavController
    lateinit var db : FirebaseFirestore
    /*
    생명주기가 이 셋으로 따지자면 onCreate -> onCreateView -> onViewCreated 순인데 view 불러오고, onViewCreated에서는 만들어진 view에서
    레이아웃 요소 바인딩 해주는거. navController는 mobile_navigation에서 프레그먼트가 diary -> questiondiary로 넘어가는 행동을
    action_navDiary_to_navQuestionDiary라는 id로 설정해줬는데, 저번에 activity에서 구현했던 거랑 똑같이 calenderView를 누르면 넘어가도록
    말 그대로 프레그먼트 안내해주는 역할
     */

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

        calendarView = view.findViewById(R.id.calendarView)

        navController = Navigation.findNavController(view)

        calendarView.setOnDateChangeListener {  view, year, month, day->

            val intent = Intent(getActivity(), QuestionDiaryActivity::class.java)
            intent.putExtra("year", year.toString())
            intent.putExtra("month", (month+1).toString())
            intent.putExtra("day", day.toString())

            val user = FirebaseAuth.getInstance().currentUser
            db = FirebaseFirestore.getInstance()

            user?.let{
                db.collection("users")
                        .whereEqualTo("email", user.email)
                        .get()
                        .addOnSuccessListener { documents ->
                            if(documents != null){
                                for(document in documents){
                                    var get_user = document.data
                                    val name = get_user["name"].toString()
                                    intent.putExtra("userName", name)
                                    startActivity(intent)
                                }
                            }
                        }
            }
        }
    }
}