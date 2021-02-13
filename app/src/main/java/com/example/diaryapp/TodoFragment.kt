package com.example.diaryapp

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

//To-do 리스트 작성창
class TodoFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var btnTodo: ImageButton
    lateinit var edtTodo: EditText

    lateinit var db: FirebaseFirestore

    lateinit var myHelper: myDBHelper
    lateinit var sqlDB: SQLiteDatabase

    val list = ArrayList<TodoList>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        btnTodo = view.findViewById(R.id.btnTodo)
        edtTodo = view.findViewById(R.id.edtTodo)

        val user = FirebaseAuth.getInstance().currentUser
        db = FirebaseFirestore.getInstance()

        //DBhelper 클래스로 한 기기로 두개의 계정을 운영할 가능성이 있어, 사용자 이메일을 받아와 파일 이름에 저장
        user?.let{
            myHelper = myDBHelper(view.context, user.email + "_TodoDB")
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = RecyclerAdapter(list)
        }

        //처음 화면 불러올 때 내부 저장소에 저장된 DB 파일 모두 recyclerView에 올리기
        sqlDB = myHelper.readableDatabase
        var cursor: Cursor
        cursor = sqlDB.rawQuery("SELECT * FROM TodoTBL;", null)

        while (cursor.moveToNext()) {
            list.add(TodoList(cursor.getString(0)))
        }

        recyclerView.adapter?.notifyDataSetChanged()

        cursor.close()
        sqlDB.close()

        //+버튼 클릭하면 valid 함수로 같은 내용이 DB에 있는지 확인한 후 없으면 DB에 저장하고 현재 나타나있는 리스트 초기화한 다음 다시 DB에서 정보 불러오기
        btnTodo.setOnClickListener {
            val TodoContent = edtTodo.text.toString()

            if (TodoContent != "") {
                if(valid(TodoContent)){
                    sqlDB = myHelper.writableDatabase
                    sqlDB.execSQL("INSERT INTO TodoTBL VALUES ('" + TodoContent + "');")
                    sqlDB.close()

                    list.clear()    //먼저 리스트 초기화하고 DB에서 불러옴
                    sqlDB = myHelper.readableDatabase
                    var cursor: Cursor
                    cursor = sqlDB.rawQuery("SELECT * FROM TodoTBL;", null)

                    while (cursor.moveToNext()) {
                        list.add(TodoList(cursor.getString(0)))
                    }

                    recyclerView.adapter?.notifyDataSetChanged()
                    edtTodo.setText(null)

                    cursor.close()
                    sqlDB.close()
                }
            } else {    //edtTodo에 아무것도 입력하지 않았을 때
                Toast.makeText(activity, "칸을 채워주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //DB에 같은 내용이 올라와있으면 추가할 수 없도록
    private fun valid(TodoContent: String) : Boolean {
        var valid = true

        sqlDB = myHelper.readableDatabase
        var cursor: Cursor
        cursor = sqlDB.rawQuery("SELECT * FROM TodoTBL;", null)

        while (cursor.moveToNext()) {
            if (cursor.getString(0) == TodoContent){
                Toast.makeText(activity, "중복되는 할일은 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
                valid = false
            }
        }

        cursor.close()
        sqlDB.close()

        return valid
    }
}